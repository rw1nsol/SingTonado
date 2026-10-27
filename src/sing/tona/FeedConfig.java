package sing.tona;

import android.content.SharedPreferences;

/** Public, credential-free resource feed hosted with SingTonado releases. */
public final class FeedConfig {
    public static final String REPOSITORY = "rw1nsol/SingTonado";
    public static final String MANIFEST_URL = "https://github.com/rw1nsol/SingTonado/releases/latest/download/manifest.json";
    private FeedConfig() {}

    public static void prepare(SharedPreferences preferences) {
        if (!MANIFEST_URL.equals(preferences.getString("singtonado_feed_url", ""))) {
            preferences.edit().putString("singtonado_feed_url", MANIFEST_URL)
                    .putLong("assets_last_check", 0L).apply();
        }
    }
}
