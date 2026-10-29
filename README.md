# SingTonado

**Birit kung birit, kahit sintunado!**

![SingTonado](images/app-icon.png)

Android karaoke with an animated opening, song numbers, MIDI/KAR playback, sound banks, and offline resources.

Version **1.7.0** adds audio recording and a recordings library, camera backgrounds, Bluetooth remote connections, count-in cues, more lyric fonts, melody-volume controls and expanded settings/scoring options. The animated startup, full-color icon in every palette and original-only song library are preserved. [Update details and verification limits](docs/UPDATE-1.7.0.md).

- [Download the latest Android app](https://github.com/rw1nsol/SingTonado/releases/latest/download/SingTonado.apk)
- [Current original-song library](https://github.com/rw1nsol/SingTonado/releases/tag/library-original-only-v1)
- [App releases](https://github.com/rw1nsol/SingTonado/releases)
- [Installation](docs/INSTALL.md)
- [Updates and publishing](docs/UPDATES.md)

The current in-app catalogue contains **47,677 original-library MIDI/KAR files**, **eight sound banks**, and **16 video backgrounds**. All SongHub-derived song additions have been withdrawn from the app feed and their published song ZIPs have been deleted from GitHub releases. Original songs and song numbers are retained, with 15 original lyric-free or chord-only files excluded by the earlier filter. Sound banks, videos, notices and artwork are retained. [Library details](docs/LYRICS-FILTER.md) | [Current song-number index](https://github.com/rw1nsol/SingTonado/releases/download/library-original-only-v1/SONGS-KEPT.csv).

Older library releases and the original base offline ZIP are historical snapshots. They include instrumental songs excluded from the current catalogue. Use the in-app downloader for the current filtered library.

In SingTonado 1.6.12 or later, tap **DOWNLOAD SONGS**, then **Download / resume** and allow the download to finish. The downloader retains 59 original song packs and removes the nine withdrawn SongHub packs from its managed cache on completion. Personal imports are unchanged; disable or remove a separately imported SongHub folder yourself. Extra sound banks remain available through Sound settings. The download panel shows progress and pause/resume controls. No new APK is required for this library change.

The app downloads resource packs from this repository's Releases. Downloads can resume, and their SHA-256 hashes are checked. It automatically checks the public release manifest for app updates; Android requires approval to install an update. Future app releases must use the same signing key.

This repository contains the distribution manifest, release tooling, documentation, artwork, and custom launch-animation/feed configuration source. Large media and APK files are Release attachments. Signing keys and account credentials are kept outside this repository.

Third-party notices are retained in `notices/`. Media files and song/artist metadata retain their respective ownership and license terms.
