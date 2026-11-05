# SingTonado 1.9.0

This release integrates the complete application classes, dependencies, native audio engine, bundled sounds, fonts and remote pages from the user-supplied 1.9.0 APK. SingTonado's package, existing signing certificate, branding, startup animation, progress-only download button, public updater and permanent song-number registry are retained.

## Features from the update

- **Stage video recording:** the optional Video too setting saves the singing stage as MP4 with the voice/music recording. Camera imagery can be included when the camera is enabled. Menus and buttons are not part of the stage recording. Videos appear in the recordings library for local playback, sharing and deletion. The upstream encoder, storage and thermal safeguards remain; unavailable video recording falls back to keeping audio. Video requires substantial free space (the app warns below about 600 MB).
- **Music player:** add MP3/video folders from the phone or connected USB/SD storage. Includes search, shuffle, repeat and full-screen playback. Karaoke fades music out; a setting resumes music when singing ends.
- **TV stage:** a separate stage can appear on a compatible connected Android display while controls remain on the phone. Display availability depends on Android and the connected hardware; this does not add support for every casting protocol.
- **Remote administration:** admin codes and guest limits, optional lyrics on remotes, short phone messages on the stage and local file sending. The supplied local upload handler accepts MIDI, KAR, MP3 and video files, with a 400 MB limit. Admin codes are not put in the QR. Bluetooth and local-network remotes remain included.
- **Downloads and storage:** Light/Everything choices, free-space and memory guidance, background-video selection, sound-bank cleanup and moving downloaded resources to a detected SD/USB location. Storage controls remain accessible in Settings; DOWNLOAD SONGS keeps the native progress-only SingTonado panel. External storage must remain connected for its managed resources to be available.
- **Sound:** Auto/High/Light quality, even loudness and optional third-party equalizer integration. The native FluidSynth engine and Android fallback replace the 1.8 BASS implementation. Auto can lower quality when a device cannot sustain High. Equalizer-app effects are not baked into the recording.
- **Library and playlists:** lyric-text search, sorting by title, singer, number, newest, file size or file type, letter navigation, playlist ordering and bulk additions.
- **Lyrics and stage options:** custom count-in pictures, photo slideshow timing, skip-button visibility, additional lyric color choices, idle quick picks and a choice of the startup song-entry screen or song list. Recording, camera, fonts, scoring, count-in and mixer controls remain integrated. Microphone scoring uses the supplied detection/fallback behavior.

This follows the updated 1.9 interface: the previous flashlight shortcut was removed upstream. The calculator and other quick tools present in 1.9 are retained. No singing guide voice has been added.

## Preserved SingTonado behavior

The supplied square image remains above SingTonado and **Birit kung birit, kahit sintunado!**, with text motion, reading time and no circular startup indicator. The dashboard logo receives no palette tint. DOWNLOAD SONGS remains text-only with no heart and opens the progress-only panel.

The resource manifest and permanent number registry are unchanged. All **47,677** current songs keep their assigned SingTonado numbers. The feed retains **59 song packs, eight sound banks, 16 videos and a notice** (84 entries). All withdrawn SongHub-derived song additions remain excluded. The APK contains no new managed song pack; future song additions must pass through the [permanent numbering workflow](SONG-NUMBERS.md).

The supplied 1.9 manifest decoder and one-time mark/lyric-fix migration remain compatible with `singtonado-numbering-v1`. Existing favorites, playlists, history and saved lyric fixes are not subjected to another shuffle.

## Installation

Open Settings → App → Check for an update, or [download SingTonado.apk](https://github.com/rw1nsol/SingTonado/releases/download/v1.9.0/SingTonado.apk). Install over the existing app without uninstalling. Package: `app.singtonado`; version: 1.9.0; version code: 24; minimum Android: 8.0. The existing signing certificate is retained.

Resource downloads are separate from APK updates. If the earlier numbering refresh is still pending, finish DOWNLOAD SONGS → Download / resume, reopen the app, and export a fresh Songbook.

## Verification and limits

Signature, certificate continuity, 16 KB APK alignment, archive CRC, package/version and branding checks pass. The compiled verifier covers **3,565 SingTonado/custom classes and 104,318 method references**, checking duplicate/missing application classes, known method/field references, feature presence and startup/download/feed/logo hooks. Two constructors omitted by bundled Android API stubs were verified against the installed Android 36 SDK. Compiled checks confirm the startup's square picture and text motion and the download button's absence of an icon.

All three upstream framework DEX files are unchanged. The custom startup DEX is unchanged and moves from classes4 to classes5 to accommodate the new dependency layout. Fonts, bundled audio and licenses remain intact. All three native karaoke engines retain their machine code, with JNI names rebranded and ELF lookup tables rebuilt/verified. The three FluidSynth libraries retain their machine code; only compiled-in upstream build-path prefixes were replaced with equal-length branding. Other native libraries are byte-identical to the supplied APK.

The public release manifest must retain all 84 resources and the exact 47,677-number mapping. Numbering tests pass. No Android phone or emulator was available: these are desktop build/static checks, not runtime tests of audio/video synchronization, microphones, cameras, TV displays, Bluetooth, removable storage, permission prompts or settings migration.
