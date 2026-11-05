# SingTonado

**Birit kung birit, kahit sintunado!**

![SingTonado](images/app-icon.png)

Android karaoke with an animated opening, song numbers, MIDI/KAR playback, sound banks, and offline resources.

Version **1.9.1** adds three optional soundfonts and removes the four-bank limit from the downloaded sound selector. It includes all 1.9.0 features: stage-video recording, music playback, TV controls, remote administration/messages, storage options and sound/lyrics improvements. [Soundfont downloads](docs/SOUNDFONTS.md) | [1.9.0 features and verification limits](docs/UPDATE-1.9.0.md).

- [Download the latest Android app](https://github.com/rw1nsol/SingTonado/releases/latest/download/SingTonado.apk)
- [Current original-song library](https://github.com/rw1nsol/SingTonado/releases/tag/library-original-only-v1)
- [App releases](https://github.com/rw1nsol/SingTonado/releases)
- [Installation](docs/INSTALL.md)
- [Updates and publishing](docs/UPDATES.md)

The current in-app catalogue contains **47,679 MIDI/KAR files**, **11 sound banks**, and **16 video backgrounds**. All SongHub-derived song additions have been withdrawn from the app feed and their published song ZIPs have been deleted from GitHub releases. Original songs are retained with permanent random SingTonado numbers, with 15 original lyric-free or chord-only files excluded by the earlier filter. Sound banks, videos, notices and artwork are retained. [Library details](docs/LYRICS-FILTER.md) | [Current song-number index](https://github.com/rw1nsol/SingTonado/releases/download/library-update-190-v1/SONG-NUMBERS.csv).

[SingTonado numbering and migration](docs/SONG-NUMBERS.md): use app 1.8.0 or later, finish DOWNLOAD SONGS, and reopen the app. Current songs change numbers once; future additions get random unused numbers while existing numbers stay fixed. Favorites, history, playlists and saved lyric fixes use the app's one-time migration. Export Songbook again after downloading.

The latest song-library update adds **Bluer Than Blue — Barry Manilow (81056)** and **1963 — New Order (52024)**. Open DOWNLOAD SONGS → Download / resume, let it finish, then reopen the app. Existing song numbers stay fixed. [Addition audit](docs/LIBRARY-UPDATE-1.9.md).

Older library releases and the original base offline ZIP are historical snapshots. They include instrumental songs excluded from the current catalogue. Use the in-app downloader for the current filtered library.

In SingTonado 1.6.12 or later, tap **DOWNLOAD SONGS**, then **Download / resume** and allow the download to finish. The downloader retains 59 original song packs and removes the nine withdrawn SongHub packs from its managed cache on completion. Personal imports are unchanged; disable or remove a separately imported SongHub folder yourself. Extra sound banks remain available through Sound settings. The download panel shows progress and pause/resume controls. No new APK is required for this library change.

The app downloads resource packs from this repository's Releases. Downloads can resume, and their SHA-256 hashes are checked. It automatically checks the public release manifest for app updates; Android requires approval to install an update. Future app releases must use the same signing key.

This repository contains the distribution manifest, release tooling, documentation, artwork, and custom launch-animation/feed configuration source. Large media and APK files are Release attachments. Signing keys and account credentials are kept outside this repository.

Third-party notices are retained in `notices/`. Media files and song/artist metadata retain their respective ownership and license terms.
