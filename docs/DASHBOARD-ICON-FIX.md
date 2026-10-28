# SingTonado 1.6.13

The dashboard previously applied a theme tint to its bitmap for every Look palette except Green. Because the picture is opaque, the tint replaced its detail with a solid square. Version 1.6.13 removes that filter: Green, Blue, Purple, Rose and Charcoal all display the same full-color SingTonado artwork.

Install the update over the existing app; do not uninstall it. The package and signing key are unchanged. The current resource catalogue, song numbers and previous song removals are preserved.

## Songbook updates

The songbook is generated from the current on-device song index. Its cache key includes the app version, month, and each included song's number, title, artist and category. Completing library downloads and reopening Songbook produces a new PDF when that index changes. The APK does not bundle a fixed song list. Files already exported or shared are separate copies and must be exported again to reflect changes.

## Verification

The compiled dashboard image passes a null ColorFilter and no longer branches on palette selection. The signed APK's package/version, signature, alignment, archive integrity and branding checks pass. The signing certificate matches 1.6.12; the framework DEX files, startup DEX and image bytes remain unchanged. The update manifest preserves the complete current resource list. No Android phone or emulator runtime test was performed.
