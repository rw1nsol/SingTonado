package sing.tona;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/** A discoverable front end for the app's existing resumable library worker. */
public final class LibraryDownloads {
    private static WeakReference<Activity> host = new WeakReference<>(null);
    private static Panel active;
    private LibraryDownloads() { }
    public static void attach(Activity activity) { host = new WeakReference<>(activity); }
    public static void show() {
        Activity activity = host.get();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        if (active != null && active.dialog.isShowing()) return;
        active = new Panel(activity);
        active.open();
    }

    // These public JVM methods are retained in the original app DEX. Keeping this
    // adapter in a separate DEX avoids bundling duplicate Kotlin/application classes.
    private static Object call(Object object, String method) throws Exception {
        return object.getClass().getMethod(method).invoke(object);
    }
    private static Object textCall(Object object, String method, String value) throws Exception {
        return object.getClass().getMethod(method, String.class).invoke(object, value);
    }
    private static String error(Exception exception) {
        Throwable cause = exception;
        while (cause instanceof InvocationTargetException && cause.getCause() != null) cause = cause.getCause();
        String message = cause.getMessage();
        return message == null ? cause.getClass().getSimpleName() : message;
    }

    private static final class Panel {
        final Activity activity;
        final Handler main = new Handler(Looper.getMainLooper());
        final ExecutorService worker = Executors.newSingleThreadExecutor();
        final LinearLayout content;
        final TextView status;
        final TextView summary;
        final ProgressBar progress;
        final Button start, pause, refresh;
        final LinearLayout items;
        final AlertDialog dialog;
        final List<Object> catalog = new ArrayList<>();
        final List<TextView> rows = new ArrayList<>();
        final Runnable tick = this::poll;
        Object downloads, store, manifest;
        volatile boolean closed;
        boolean loading;

        Panel(Activity activity) {
            this.activity = activity;
            content = new LinearLayout(activity);
            content.setOrientation(LinearLayout.VERTICAL);
            content.setPadding(dp(20), dp(8), dp(20), dp(16));
            summary = label("Download once from GitHub, then sing offline. Songs appear automatically as packs finish. Best on Wi-Fi.", 16);
            status = label("Connecting to the library…", 15);
            progress = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
            progress.setMax(100);
            content.addView(progress, new LinearLayout.LayoutParams(-1, dp(12)));
            start = button("Download / resume library", () -> command(null, false));
            start.setEnabled(false);
            pause = button("Pause downloads", () -> command(null, true));
            pause.setEnabled(false);
            refresh = button("Refresh library", this::load);
            items = new LinearLayout(activity);
            items.setOrientation(LinearLayout.VERTICAL);
            content.addView(items);
            ScrollView scroll = new ScrollView(activity);
            scroll.addView(content);
            dialog = new AlertDialog.Builder(activity).setTitle("SingTonado offline library")
                    .setView(scroll).setPositiveButton("Back to songs", (d, which) -> { })
                    .create();
            dialog.setOnDismissListener(d -> {
                closed = true;
                main.removeCallbacks(tick);
                worker.shutdownNow();
                if (active == this) active = null;
            });
        }
        int dp(int value) { return Math.round(value * activity.getResources().getDisplayMetrics().density); }
        String size(long bytes) { return Formatter.formatFileSize(activity, bytes); }
        TextView label(String text, int sp) {
            TextView view = new TextView(activity);
            view.setText(text); view.setTextSize(sp);
            view.setPadding(0, dp(8), 0, dp(8));
            content.addView(view);
            return view;
        }
        Button button(String text, Runnable action) {
            Button button = new Button(activity);
            button.setText(text); button.setAllCaps(false);
            button.setOnClickListener(v -> action.run());
            content.addView(button, new LinearLayout.LayoutParams(-1, -2));
            return button;
        }
        void open() { dialog.show(); load(); }
        void ui(Runnable update) { main.post(() -> { if (!closed && !activity.isDestroyed()) update.run(); }); }
        void load() {
            if (closed || loading) return;
            loading = true; refresh.setEnabled(false);
            status.setText("Checking GitHub for songs and sounds…");
            worker.execute(() -> {
                try {
                    Object container = call(activity.getApplication(), "getContainer");
                    downloads = call(container, "getDownloads");
                    store = call(downloads, "getStore");
                    String warning = null;
                    try { manifest = call(downloads, "fetchManifest$app_release"); }
                    catch (Exception fetchError) {
                        manifest = call(store, "manifest");
                        if (manifest == null) throw fetchError;
                        warning = "Using the saved catalog. GitHub is currently unreachable: " + error(fetchError);
                    }
                    List<?> entries = (List<?>) call(manifest, "getItems");
                    catalog.clear(); catalog.addAll(entries);
                    long normal = 0, optional = 0; int packs = 0;
                    for (Object item : catalog) {
                        long bytes = (Long) call(item, "getBytes");
                        if ((Boolean) call(item, "getOptional")) optional += bytes; else normal += bytes;
                        if ("songs".equals(call(item, "getKind"))) packs++;
                    }
                    final long total = normal, extra = optional;
                    final int packCount = packs;
                    final String note = warning;
                    ui(() -> {
                        loading = false; refresh.setEnabled(true); start.setEnabled(true); pause.setEnabled(true);
                        summary.setText(packCount + " song packs, Basic and Platinum Major sounds, and video backgrounds.\nLibrary download: " + size(total)
                                + ". Optional sounds: " + size(extra) + ".\nDownloads are saved inside SingTonado; no ZIP extraction or folder setup is needed.");
                        start.setText("Download / resume library · " + size(total));
                        status.setText(note == null ? "Library ready. Tap Download / resume library to begin." : note);
                        populate();
                        main.removeCallbacks(tick); main.postDelayed(tick, 1200);
                    });
                } catch (Exception exception) {
                    ui(() -> { loading = false; refresh.setEnabled(true); status.setText("Could not load the library. Check your internet connection and tap Refresh.\n" + error(exception)); });
                }
            });
        }
        void populate() {
            items.removeAllViews(); rows.clear();
            for (Object item : catalog) {
                try {
                    String name = (String) call(item, "getName");
                    String kind = (String) call(item, "getKind");
                    String id = (String) call(item, "getId");
                    boolean optional = (Boolean) call(item, "getOptional");
                    if (optional) {
                        Button extra = new Button(activity);
                        extra.setText("Download " + name + " · " + size((Long) call(item, "getBytes")) + " (optional)");
                        extra.setAllCaps(false); extra.setOnClickListener(v -> command(id, false));
                        items.addView(extra);
                    }
                    TextView row = new TextView(activity);
                    row.setTextSize(14); row.setPadding(0, dp(7), 0, dp(7));
                    row.setText(name + " · " + kind);
                    items.addView(row); rows.add(row);
                } catch (Exception exception) { status.setText("Catalog error: " + error(exception)); }
            }
        }
        void command(String id, boolean stopping) {
            if (downloads == null || closed) return;
            try {
                if (stopping) call(downloads, "pause");
                else if (id != null) textCall(downloads, "want", id);
                else call(downloads, "start");
                status.setText(stopping ? "Paused. Downloaded files are kept; tap Download / resume to continue."
                        : "Download queued. You can return to the song list while it runs.");
                main.removeCallbacks(tick); main.postDelayed(tick, 1200);
            } catch (Exception exception) { status.setText("Could not change the download: " + error(exception)); }
        }
        void poll() {
            if (closed || loading || downloads == null) return;
            worker.execute(() -> {
                try {
                    Object flow = call(downloads, "getProgress");
                    Object state = Class.forName("kotlinx.coroutines.flow.StateFlow").getMethod("getValue").invoke(flow);
                    int percent = (Integer) call(state, "getPercent");
                    long done = (Long) call(state, "getDoneBytes"), total = (Long) call(state, "getTotalBytes");
                    int packs = (Integer) call(state, "getPacksReady");
                    String current = (String) call(state, "getCurrent");
                    boolean complete = (Boolean) call(state, "getComplete");
                    boolean paused = (Boolean) call(downloads, "getUserPaused");
                    String work = workStatus();
                    String headline = complete ? "Library downloaded. Ready to sing offline."
                            : paused ? "Paused. Your downloaded files are kept."
                            : work;
                    final String message = headline + "\n" + size(done) + " / " + size(total) + " · " + packs + " song packs ready"
                            + (current == null ? "" : "\n" + current);
                    List<String> captions = new ArrayList<>();
                    Class<?> itemType = Class.forName("sing.tona.download.AssetItem");
                    for (Object item : catalog) {
                        boolean ready = (Boolean) store.getClass().getMethod("isDone", itemType).invoke(store, item);
                        captions.add((ready ? "✓ Ready · " : "○ ") + call(item, "getName") + " · " + size((Long) call(item, "getBytes")));
                    }
                    ui(() -> {
                        progress.setProgress(percent); status.setText(message);
                        for (int i=0; i<rows.size() && i<captions.size(); i++) rows.get(i).setText(captions.get(i));
                    });
                } catch (Exception exception) { ui(() -> status.setText("Progress unavailable. Tap Refresh to retry.\n" + error(exception))); }
                finally { ui(() -> main.postDelayed(tick, 2000)); }
            });
        }
        String workStatus() {
            try {
                Class<?> type = Class.forName("androidx.work.WorkManager");
                Object manager = type.getMethod("getInstance", Context.class).invoke(null, activity.getApplicationContext());
                Future<?> future = (Future<?>) type.getMethod("getWorkInfosForUniqueWork", String.class).invoke(manager, "assets");
                List<?> work = (List<?>) future.get(3, TimeUnit.SECONDS);
                for (Object info : work) {
                    String state = call(info, "getState").toString();
                    if ("RUNNING".equals(state)) return "Downloading from GitHub…";
                    if ("ENQUEUED".equals(state) || "BLOCKED".equals(state)) return "Queued. Waiting for an internet connection…";
                }
                if (!work.isEmpty()) {
                    Object info = work.get(work.size()-1);
                    if ("FAILED".equals(call(info, "getState").toString())) {
                        String reason = (String) textCall(call(info, "getOutputData"), "getString", "error");
                        return "Download stopped. Tap Download / resume to retry." + (reason == null ? "" : "\n" + reason);
                    }
                }
            } catch (Exception ignored) { }
            return "Tap Download / resume library to continue.";
        }
    }
}
