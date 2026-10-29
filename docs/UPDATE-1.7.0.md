# SingTonado 1.7.0

This release integrates the app code, native audio engine, Android dependencies, bundled sounds and font resources from the user-supplied 1.7.0 APK. SingTonado's package, signing key, branding, launch animation, update feed and original-only resource catalogue are retained. This is a feature update; the version label is not the only change.

## Features

- Audio recording mixes the microphone with the backing track and saves M4A takes. The recordings library supports playback, sharing and deletion. Recording settings include voice level and timing adjustment.
- A live camera can appear behind the lyrics, with lens selection, mirroring and dimming. It is a local preview; the recording feature keeps audio, not camera video. The upstream thermal safeguard is retained.
- Bluetooth remote/stage discovery and connections supplement local-network remotes.
- Count-in cues support numbers or dots and sounds, including cues after a break. Number-key speech and scoring sounds are bundled.
- Eleven lyric font options include Arimo, Poppins, easy-reading, heavy, narrow, rounded, comic, playful, script, slab and marker styles.
- Melody volume choices, Off/Random/Mic scoring, an improved idle song-entry screen, settings navigation/search, full-library versus personal-song choice, and stay-on-stage versus return-to-list behavior after the final song are included.

Camera, microphone and Nearby devices permissions are requested by their respective controls. A take shorter than ten seconds is not kept by the integrated recorder.

## Preserved SingTonado behavior

The supplied icon/photo and animated startup remain unchanged. The dashboard icon receives no theme tint, so it stays full-color in Green, Blue, Purple, Rose and Charcoal. The DOWNLOAD SONGS action opens the existing progress-only dialog. The native 1.7.0 first-run and personal-library choice controls remain available.

The public resource feed continues to provide 47,677 original-library MIDI/KAR files in 59 song packs, eight sound banks and 16 videos. All SongHub song additions remain withdrawn from GitHub and the feed. Bundled count/keypad/score sounds and fonts do not add song packs.

## Installation

Install [SingTonado.apk](https://github.com/rw1nsol/SingTonado/releases/download/v1.7.0/SingTonado.apk) over the existing installation. Do not uninstall it first. The package is `app.singtonado`, version code 21, with the same certificate as 1.6.13. Android 8.0 or later is required.

The app update is separate from resource downloads. Use DOWNLOAD SONGS to finish any pending library cleanup. Reopen Songbook and export again to obtain a PDF for the current on-device list.

## Verification and limits

The APK passes signature, certificate-continuity, alignment, archive CRC, package/version and branding checks. A compiled DEX check covers 2,788 SingTonado/custom classes and 81,430 method references, verifies required feature classes and the startup/download/feed hooks, and confirms the dashboard Image receives a null color filter. Duplicate class definitions are rejected.

Upstream framework DEX files and the new bundled audio/font-license/support files are preserved. The three native audio libraries retain the supplied 1.7.0 machine code; JNI namespace changes rebuild and verify ELF symbol lookup tables. The custom startup DEX and its picture are unchanged from 1.6.13. The public update feed preserves all 84 current resource entries.

No phone or emulator was available. These are desktop build/static checks, not end-to-end tests of microphone recording, camera preview, Bluetooth pairing, playback, permissions or settings migration.
