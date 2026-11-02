# SingTonado 1.8.0

This release integrates the complete application classes, Android dependencies, BASS audio/MIDI libraries, fonts and bundled sounds from the user-supplied 1.8.0 APK. SingTonado's app identity, signing certificate, branding, animated opening and public resource/update feed are retained.

## Features added since 1.7.0

- Playlists: create, rename and delete lists, add songs from the list or a song's long-press menu, remove entries and play a whole playlist.
- Playback modes: in order, repeat one, repeat all and shuffle. Reserved songs and singing history can be cleared separately.
- Direct access to a selected USB/SD songs folder using Android's folder picker, alongside the existing import/copy options. The drive must remain connected when singing its songs.
- Song-title display: boxed, clear background or hidden. Background videos can continue between songs, with a stage control to change the background.
- Quick-settings shortcuts for stage/remote connections, sounds, songbook, flashlight and calculator, plus an improved problem report and crash log.
- The updated BASS MIDI/audio engine, MIDI handling and lyric timing/layout, including additional imported MIDI lyric formats. Format support does not restore any withdrawn song pack.

Recording, the recordings library, camera preview, Bluetooth/network remotes, lyric fonts, count-in cues, scoring and melody controls from 1.7.0 are retained. Camera preview is not video recording; microphone takes save audio.

## Preserved SingTonado behavior

The supplied square startup image remains above SingTonado and the tagline **Birit kung birit, kahit sintunado!**, with text motion and reading time, and no circular loading indicator. The dashboard logo is untinted in all Look palettes. DOWNLOAD SONGS opens the existing progress-only panel with download, pause and resume controls.

The resource inventory contains 47,677 original-library MIDI/KAR files in 59 song packs, eight sound banks, 16 videos and a notice (84 entries). All withdrawn SongHub-derived song additions remain excluded. The later [SingTonado numbering resource update](SONG-NUMBERS.md) changes source numbers once, then keeps existing SingTonado numbers permanent for future updates.

## Installation and updates

Use Settings → App → Check for an update, or download [SingTonado.apk](https://github.com/rw1nsol/SingTonado/releases/download/v1.8.0/SingTonado.apk). Approve Android's installation prompt and install over the existing app without uninstalling it. Package: `app.singtonado`; version: 1.8.0; version code: 22. The existing signing certificate and Android 8.0 minimum are retained.

Resources download separately from the APK. Reopen Songbook and export again to refresh an already saved PDF.

## Verification and limits

Signature, certificate continuity, 16 KB APK alignment, archive CRC, package/version and branding checks pass. The compiled DEX verifier checks 3,070 SingTonado/custom classes and 89,492 method references, including missing app-class owners, duplicate classes, feature presence and startup/download/feed hooks. It confirms the dashboard Image receives a null color filter and the startup contains no circular graphics.

The upstream framework DEX files, all 18 native libraries, fonts, bundled count/keypad/score sounds and supporting license files are preserved byte-for-byte. The custom startup DEX and picture remain unchanged. The feed uses the actual build number and retains all 84 resource entries; no upstream service credential is distributed.

No Android phone or emulator was available. These are desktop build/static checks, not runtime tests of playback, microphones, camera, Bluetooth, removable storage, settings migration or permissions.
