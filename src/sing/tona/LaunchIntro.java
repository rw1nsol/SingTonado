package sing.tona;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.io.IOException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/** A non-blocking launch overlay: the library initializes behind it. */
public final class LaunchIntro {
    public static final String TITLE = "SingTonado";
    public static final String TAGLINE = "Birit kung birit, kahit sintunado!";
    private static final String TAG = "singtonado.launch.intro";
    public static final long READABLE_HOLD_MS = 4800L;
    public static final long EXIT_MS = 450L;

    private LaunchIntro() {}

    public static void show(Activity activity) {
        LibraryDownloads.attach(activity);
        ViewGroup content = activity.findViewById(android.R.id.content);
        if (content == null || content.findViewWithTag(TAG) != null || activity.isFinishing()) return;
        IntroFrame intro = new IntroFrame(activity, content);
        intro.setTag(TAG);
        activity.addContentView(intro, new ViewGroup.LayoutParams(-1, -1));
        intro.bringToFront();
        intro.requestFocus();
        intro.post(intro::begin);
    }

    public static boolean consumeKey(Activity activity, KeyEvent event) {
        ViewGroup content = activity.findViewById(android.R.id.content);
        if (content == null || content.findViewWithTag(TAG) == null) return false;
        int key = event.getKeyCode();
        return key != KeyEvent.KEYCODE_VOLUME_UP && key != KeyEvent.KEYCODE_VOLUME_DOWN
                && key != KeyEvent.KEYCODE_VOLUME_MUTE;
    }

    private static final class IntroFrame extends FrameLayout {
        private final ImageView logo;
        private final TextView title;
        private final TextView tagline;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final ArrayList<View> coveredViews = new ArrayList<>();
        private final ArrayList<Integer> accessibilityModes = new ArrayList<>();
        private final Runnable exit = this::leave;
        private AnimatorSet entrance;
        private AnimatorSet floating;
        private boolean finishedEntrance;
        private boolean detached;
        private boolean exiting;
        private long remainingHold = READABLE_HOLD_MS;
        private long holdStart;

        IntroFrame(Activity activity, ViewGroup content) {
            super(activity);
            for (int i = 0; i < content.getChildCount(); i++) {
                View child = content.getChildAt(i);
                coveredViews.add(child);
                accessibilityModes.add(child.getImportantForAccessibility());
                child.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
            }
            setWillNotDraw(false);
            setClickable(true);
            setFocusable(true);
            setFocusableInTouchMode(true);
            setElevation(dp(24));
            setPadding(dp(24), dp(24), dp(24), dp(24));
            setOnApplyWindowInsetsListener((view, insets) -> {
                view.setPadding(dp(24) + insets.getSystemWindowInsetLeft(),
                        dp(24) + insets.getSystemWindowInsetTop(),
                        dp(24) + insets.getSystemWindowInsetRight(),
                        dp(24) + insets.getSystemWindowInsetBottom());
                return insets;
            });
            LinearLayout stack = new LinearLayout(activity);
            stack.setOrientation(LinearLayout.VERTICAL);
            stack.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams stackParams = new FrameLayout.LayoutParams(-1, -2, Gravity.CENTER);
            addView(stack, stackParams);

            logo = new ImageView(activity);
            // Load the supplied picture directly, independent of resource-name lookup.
            Bitmap startupPicture = null;
            try (InputStream image = activity.getAssets().open("singtonado-startup.png")) {
                startupPicture = BitmapFactory.decodeStream(image);
            } catch (IOException ignored) { }
            if (startupPicture != null) logo.setImageBitmap(startupPicture);
            else logo.setImageResource(0x7f050021);
            logo.setImageAlpha(255);
            logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
            logo.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(dp(280), dp(280));
            logoParams.bottomMargin = dp(20);
            stack.addView(logo, logoParams);

            title = new TextView(activity);
            title.setText(TITLE);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTypeface(Typeface.create("sans-serif-black", Typeface.BOLD));
            title.setLetterSpacing(0.015f);
            title.setMaxLines(1);
            title.setAutoSizeTextTypeUniformWithConfiguration(26, 48, 1, 2);
            title.setShadowLayer(dp(18), 0, dp(2), 0xB04063FF);
            stack.addView(title, new LinearLayout.LayoutParams(-1, dp(66)));

            tagline = new TextView(activity);
            tagline.setText(TAGLINE);
            tagline.setGravity(Gravity.CENTER);
            tagline.setTextColor(0xFFFFD76D);
            tagline.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            tagline.setTextSize(20);
            tagline.setMaxLines(2);
            tagline.setLineSpacing(dp(4), 1.08f);
            tagline.setAutoSizeTextTypeUniformWithConfiguration(16, 21, 1, 2);
            LinearLayout.LayoutParams taglineParams = new LinearLayout.LayoutParams(-1, dp(68));
            taglineParams.topMargin = dp(8);
            stack.addView(tagline, taglineParams);
            // ImageView.setAlpha(int) changes the bitmap's own opacity. Use the
            // float View property so the entrance animation can reveal it.
            logo.setAlpha(0f);
            title.setAlpha(0);
            tagline.setAlpha(0);

            AccessibilityManager accessibility = (AccessibilityManager) activity.getSystemService(Context.ACCESSIBILITY_SERVICE);
            if (accessibility != null && accessibility.isTouchExplorationEnabled()) remainingHold = 8000L;
        }

        private int dp(float value) { return Math.round(value * getResources().getDisplayMetrics().density); }

        @Override protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
            super.onSizeChanged(width, height, oldWidth, oldHeight);
            if (logo == null) return;
            int availableHeight = height - getPaddingTop() - getPaddingBottom();
            int edge = Math.min(dp(320), Math.min((width - getPaddingLeft() - getPaddingRight()) * 6 / 7,
                    Math.max(dp(70), availableHeight - dp(185))));
            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) logo.getLayoutParams();
            if (p.width != edge || p.height != edge) { p.width = p.height = edge; logo.setLayoutParams(p); }
        }

        private ObjectAnimator animate(View target, String property, float from, float to, long delay, long duration) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(target, property, from, to);
            animator.setStartDelay(delay);
            animator.setDuration(duration);
            animator.setInterpolator(new DecelerateInterpolator(1.6f));
            return animator;
        }

        void begin() {
            if (detached || finishedEntrance) return;
            if (!ValueAnimator.areAnimatorsEnabled()) {
                logo.setAlpha(1f); title.setAlpha(1f); tagline.setAlpha(1f);
                entranceComplete();
                return;
            }
            entrance = new AnimatorSet();
            entrance.playTogether(
                    animate(logo, "alpha", 0, 1, 0, 700),
                    animate(logo, "scaleX", 0.82f, 1, 0, 800),
                    animate(logo, "scaleY", 0.82f, 1, 0, 800),
                    animate(title, "alpha", 0, 1, 350, 650),
                    animate(title, "translationY", dp(18), 0, 350, 650),
                    animate(tagline, "alpha", 0, 1, 1100, 750),
                    animate(tagline, "translationY", dp(12), 0, 1100, 750));
            entrance.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animator) { if (!detached) entranceComplete(); }
            });
            entrance.start();
        }

        private void entranceComplete() {
            finishedEntrance = true;
            if (ValueAnimator.areAnimatorsEnabled() && remainingHold == READABLE_HOLD_MS) {
                floating = new AnimatorSet();
                floating.playTogether(
                        floatMotion(logo, "translationY", 0f, -dp(6), 1700),
                        floatMotion(title, "translationY", 0f, -dp(5), 1400),
                        floatMotion(title, "scaleX", 1f, 1.018f, 1400),
                        floatMotion(title, "scaleY", 1f, 1.018f, 1400),
                        floatMotion(tagline, "translationY", 0f, dp(4), 1600));
                floating.start();
            }
            scheduleHold();
        }

        private ObjectAnimator floatMotion(View view, String property, float from, float to, long duration) {
            ObjectAnimator motion = ObjectAnimator.ofFloat(view, property, from, to);
            motion.setDuration(duration);
            motion.setRepeatCount(ValueAnimator.INFINITE);
            motion.setRepeatMode(ValueAnimator.REVERSE);
            motion.setInterpolator(new AccelerateDecelerateInterpolator());
            return motion;
        }

        private void scheduleHold() {
            if (detached || exiting || !finishedEntrance || getWindowVisibility() != View.VISIBLE) return;
            removeCallbacks(exit);
            holdStart = SystemClock.uptimeMillis();
            postDelayed(exit, remainingHold);
        }

        @Override protected void onWindowVisibilityChanged(int visibility) {
            super.onWindowVisibilityChanged(visibility);
            if (exit == null || !finishedEntrance || exiting) return;
            if (visibility == View.VISIBLE) scheduleHold();
            else {
                if (holdStart > 0) remainingHold = Math.max(0, remainingHold - (SystemClock.uptimeMillis() - holdStart));
                holdStart = 0;
                removeCallbacks(exit);
            }
        }

        private void leave() {
            if (detached || exiting) return;
            exiting = true;
            if (!ValueAnimator.areAnimatorsEnabled()) { removeOverlay(); return; }
            animate().alpha(0).setDuration(EXIT_MS).withEndAction(this::removeOverlay).start();
        }

        private void removeOverlay() {
            if (getParent() instanceof ViewGroup) ((ViewGroup) getParent()).removeView(this);
        }

        @Override protected void onDetachedFromWindow() {
            detached = true;
            removeCallbacks(exit);
            if (entrance != null) entrance.cancel();
            if (floating != null) floating.cancel();
            animate().cancel();
            for (int i = 0; i < coveredViews.size(); i++)
                coveredViews.get(i).setImportantForAccessibility(accessibilityModes.get(i));
            coveredViews.clear(); accessibilityModes.clear();
            super.onDetachedFromWindow();
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = getWidth(), h = getHeight();
            canvas.drawColor(0xFF050717);
            float radius = Math.max(w, h) * 0.65f;
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(new RadialGradient(w * 0.17f, h * 0.30f, radius,
                    new int[]{0x703344B8, 0x003344B8}, null, Shader.TileMode.CLAMP));
            canvas.drawRect(0, 0, w, h, paint);
            paint.setShader(new RadialGradient(w * 0.90f, h * 0.73f, radius * 0.9f,
                    new int[]{0x603F126D, 0x003F126D}, null, Shader.TileMode.CLAMP));
            canvas.drawRect(0, 0, w, h, paint);
            paint.setShader(null);

        }
    }
}
