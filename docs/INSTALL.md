# Install SingTonado

Requires Android 8.0 or later.

1. Download `SingTonado.apk` from the latest app release.
2. Open it and complete Android's installation prompts. If asked, allow installation from the app used to download this APK.
3. Open SingTonado. The name and tagline animate in, remain fully visible for about five seconds, and fade into the library.
4. Use the download control for the song packs and instrument sounds. Use Wi-Fi for the large sound banks. Optional Dreamsam is approximately 1.32 GB by itself.

## Manual offline setup

1. Download and extract `SingTonado-offline-library.zip` from the library release.
2. Copy the extracted `SingTonado` folder to your Android device's shared storage.
3. In Settings / Your songs and sounds, enable the SingTonado folder or add the extracted Songs folder. Select the sound bank in Sound.
4. Add or select the background videos if needed.

Allow approximately 4.08 GB for extracted contents, plus the ZIP if extracting directly on the phone. The archive is separate from the APK and is not preinstalled in the app's private download storage.

## Install from a connected computer

Enable Android developer options and USB debugging, connect a data-capable USB cable, unlock the phone, and approve the computer's debugging fingerprint. Then run:

```text
adb devices
adb install -r SingTonado.apk
adb shell am start -n app.singtonado/sing.tona.MainActivity
```

`-r` updates an existing SingTonado installation without clearing its data. Do not uninstall it unless you intend to remove its app data.
