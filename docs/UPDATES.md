# Resource and app updates

The app reads the public HTTPS feed at:

```text
https://github.com/rw1nsol/SingTonado/releases/latest/download/manifest.json
```

The feed contains the resource inventory and app-release metadata. Resource URLs are pinned to `library-v1`. App updates specify a version code, minimum Android SDK, APK URL, byte size and SHA-256 digest.

The updater checks that an APK is for `app.singtonado`, is newer than the installed app, and is signed by the same signing key. It downloads and verifies the APK, then hands installation to Android. Android's installation approval remains required. Update checks and resource sync follow the app's Settings.

## Publish a future app update

Build and sign the APK using the existing private SingTonado key. Increase the Android version code for every published update. Authenticate GitHub CLI as an account with repository write access, then run:

```text
python scripts/publish_release.py --apk PATH_TO_SIGNED_APK --version-name 1.6.5 --version-code 12 --notes PATH_TO_NOTES.md
```

The script creates a draft release, uploads `SingTonado.apk` and `manifest.json`, and publishes it as the latest release only after both uploads are complete. Existing installations discover the new version through the stable feed URL. Never publish the signing key, its password, account credentials, or a GitHub access token.

## Library hosting

`library-v1` contains the complete ZIP and all 66 individual resource files. `manifests/resources.json` holds public URLs and expected checksums. Keep the library release marked as not latest so that the latest-release feed continues to resolve to an app release.
