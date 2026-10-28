# SingTonado

**Birit kung birit, kahit sintunado!**

![SingTonado](images/app-icon.png)

Android karaoke with an animated opening, song numbers, MIDI/KAR playback, sound banks, and offline resources.

Version **1.6.13** fixes the dashboard icon becoming a solid square in non-green Look themes. The full-color artwork now appears in every palette. [Icon fix and songbook refresh details](docs/DASHBOARD-ICON-FIX.md).

- [Download the latest Android app](https://github.com/rw1nsol/SingTonado/releases/latest/download/SingTonado.apk)
- [Current library and lyrics filter](https://github.com/rw1nsol/SingTonado/releases/tag/library-dedup-v1)
- [App releases](https://github.com/rw1nsol/SingTonado/releases)
- [Installation](docs/INSTALL.md)
- [Updates and publishing](docs/UPDATES.md)

The current in-app catalogue contains **62,060 MIDI/KAR files**, **eight sound banks**, and **16 video backgrounds**. The lyrics filter removes **3,239 songs without lyrics**, including files containing only credits, chords or arrangement markers. Song **69194 — (219013)** and two newer duplicate entries are also removed at the user's request. Older duplicate entries are retained: keep **14224**, remove **68523**; keep **73303**, remove **75431**. The 42 matching title/artist pairs already in the original library are preserved. Sound effects and other media are retained. Existing song numbers and retained file contents are unchanged. [Filter details](docs/LYRICS-FILTER.md) | [Current song-number index](https://github.com/rw1nsol/SingTonado/releases/download/library-dedup-v1/SONGS-KEPT.csv).

Older library releases and the original base offline ZIP are historical snapshots. They include instrumental songs excluded from the current catalogue. Use the in-app downloader for the current filtered library.

In SingTonado 1.6.12 or later, tap **DOWNLOAD SONGS**, then **Download / resume** and allow the download to finish. The downloader replaces 17 changed song packs, removes their superseded cached copies when complete, and refreshes the library. Personal imports are unchanged. Extra sound banks remain available through Sound settings. The download panel shows progress and pause/resume controls. No new APK is required for this library change.

The app downloads resource packs from this repository's Releases. Downloads can resume, and their SHA-256 hashes are checked. It automatically checks the public release manifest for app updates; Android requires approval to install an update. Future app releases must use the same signing key.

This repository contains the distribution manifest, release tooling, documentation, artwork, and custom launch-animation/feed configuration source. Large media and APK files are Release attachments. Signing keys and account credentials are kept outside this repository.

Third-party notices are retained in `notices/`. Media files and song/artist metadata retain their respective ownership and license terms.
