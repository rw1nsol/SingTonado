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
        final ProgressBar progress;
        final Button start, pause, refresh;
        final AlertDialog dialog;
        final Runnable tick = this::poll;
        Object downloads, store, manifest;
        volatile boolean closed;
        boolean loading;

        Panel(Activity activity) {
            this.activity = activity;
            content = new LinearLayout(activity);
            content.setOrientation(LinearLayout.VERTICAL);
            content.setPadding(dp(20), dp(8), dp(20), dp(16));
            status = label("Preparing download...", 16);
            progress = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
            progress.setMax(100);
            content.addView(progress, new LinearLayout.LayoutParams(-1, dp(12)));
            start = button("Download / resume", () -> command(null, false));
            start.setEnabled(false);
            pause = button("Pause downloads", () -> command(null, true));
            pause.setEnabled(false);
            refresh = button("Retry", this::load);
            refresh.setVisibility(View.GONE);
            ScrollView scroll = new ScrollView(activity);
            scroll.addView(content);
            dialog = new AlertDialog.Builder(activity).setTitle("Download progress")
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
            refresh.setVisibility(View.GONE);
            status.setText("Checking download status...");
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
                        warning = "Offline. Downloaded files are kept.";
                    }
                    final String note = warning;
                    ui(() -> {
                        loading = false; refresh.setEnabled(true); start.setEnabled(true); pause.setEnabled(true);
                        status.setText(note == null ? "Ready. Tap Download / resume to begin." : note);
                        main.removeCallbacks(tick); main.postDelayed(tick, 1200);
                    });
                } catch (Exception exception) {
                    ui(() -> { loading = false; refresh.setEnabled(true); refresh.setVisibility(View.VISIBLE); status.setText("Could not connect. Check your internet connection and tap Retry."); });
                }
            });
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
            } catch (Exception exception) { status.setText("Could not change the download. Tap Retry."); refresh.setVisibility(View.VISIBLE); }
        }
        void poll() {
            if (closed || loading || downloads == null) return;
            worker.execute(() -> {
                try {
                    Object flow = call(downloads, "getProgress");
                    Object state = Class.forName("kotlinx.coroutines.flow.StateFlow").getMethod("getValue").invoke(flow);
                    int percent = (Integer) call(state, "getPercent");
                    long done = (Long) call(state, "getDoneBytes"), total = (Long) call(state, "getTotalBytes");
                    boolean complete = (Boolean) call(state, "getComplete");
                    boolean paused = (Boolean) call(downloads, "getUserPaused");
                    String work = workStatus();
                    String headline = complete ? "Download complete. Ready to sing offline."
                            : paused ? "Paused. Your downloaded files are kept."
                            : work;
                    final String message = headline + "\n" + percent + "%  " + size(done) + " / " + size(total);
                    ui(() -> {
                        progress.setProgress(percent); status.setText(message);
                    });
                } catch (Exception exception) { ui(() -> { status.setText("Progress unavailable. Tap Retry."); refresh.setVisibility(View.VISIBLE); }); }
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
                    if ("RUNNING".equals(state)) return "Downloading...";
                    if ("ENQUEUED".equals(state) || "BLOCKED".equals(state)) return "Queued. Waiting for an internet connection…";
                }
                if (!work.isEmpty()) {
                    Object info = work.get(work.size()-1);
                    if ("FAILED".equals(call(info, "getState").toString())) {
                        return "Download stopped. Tap Download / resume to retry.";
                    }
                }
            } catch (Exception ignored) { }
            return "Tap Download / resume to continue.";
        }
    }
}
