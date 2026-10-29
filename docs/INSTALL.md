# Install SingTonado

Requires Android 8.0 or later.

The current APK is **1.7.0**, with recording, camera, Bluetooth remote, count-in and lyric/settings improvements. Install it over the existing app without uninstalling. The dashboard picture remains full-color in every Look theme. The songbook is regenerated from the current local library when reopened; export it again if a previously saved PDF is out of date. See [the feature update guide](UPDATE-1.7.0.md).

1. Download `SingTonado.apk` from the latest app release.
2. Open it and complete Android's installation prompts. If asked, allow installation from the app used to download this APK.
3. Open SingTonado. A large square SingTonado picture with transparent outer corners and margins appears above the name, with no black box or circular startup indicator. The name and tagline animate in and float gently, remain visible for about five seconds, and fade into the library.
4. On the main song library screen, tap **DOWNLOAD SONGS**, then **Download / resume**. The standard download includes 59 original song packs, Basic and Platinum Major sound banks, and 16 video backgrounds. All SongHub-added MIDI/KAR packs have been withdrawn. Downloads run in the background; completed packs appear automatically. No ZIP extraction or folder selection is required.
5. Reopen **DOWNLOAD SONGS** to check progress, pause or resume. The app shows download percentage and transferred size; resource listings and hosting details are hidden. Use Wi-Fi for these downloads.

The current library works with SingTonado 1.6.12 or later. Open **DOWNLOAD SONGS** to refresh the catalogue, then **Download / resume**. Let it finish so superseded and withdrawn SongHub packs are removed. Extra sound banks are available in **Settings / Sound**. See [the library guide](LYRICS-FILTER.md).

## Manual offline setup

1. Prefer the in-app downloader to obtain the current filtered song packs and media.
2. For manual imports, download song ZIPs referenced by the current `manifest.json`, extract into a fresh folder, and copy it to the phone. Do not combine filtered packs with older copies of the same packs.
3. In Settings / Your songs and sounds, select the new folder and disable an older unfiltered folder if it was previously added. Select the sound bank in Sound.
4. Add or select the background videos if needed. Personally imported folders are never automatically cleaned by the download update.

The original base `SingTonado-offline-library.zip` is a historical archive with 47,692 songs, including instrumental files now excluded. It is not the filtered library. Resource downloads are separate from the APK.

## Install from a connected computer

Enable Android developer options and USB debugging, connect a data-capable USB cable, unlock the phone, and approve the computer's debugging fingerprint. Then run:

```text
adb devices
adb install -r SingTonado.apk
adb shell am start -n app.singtonado/sing.tona.MainActivity
```

`-r` updates an existing SingTonado installation without clearing its data. Do not uninstall it unless you intend to remove its app data.
