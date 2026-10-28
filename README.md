# SingTonado

**Birit kung birit, kahit sintunado!**

![SingTonado](images/app-icon.png)

Android karaoke with an animated opening, song numbers, MIDI/KAR playback, sound banks, and offline resources.

- [Download the latest Android app](https://github.com/rw1nsol/SingTonado/releases/latest/download/SingTonado.apk)
- [Download the base offline library](https://github.com/rw1nsol/SingTonado/releases/download/library-v1/SingTonado-offline-library.zip)
- [App releases](https://github.com/rw1nsol/SingTonado/releases)
- [Installation](docs/INSTALL.md)
- [Updates and publishing](docs/UPDATES.md)

The current in-app catalogue contains **65,302 MIDI/KAR files**, **eight sound banks**, and **16 video backgrounds**. The [additional resource release](https://github.com/rw1nsol/SingTonado/releases/tag/library-songhub-v1) adds 17,610 validated files without changing existing song numbers. [New song-number index](https://github.com/rw1nsol/SingTonado/releases/download/library-songhub-v1/ADDITIONAL-SONGS.csv).

The original base offline ZIP contains **47,692 songs**, **three sound banks** (Basic, Platinum Major, Dreamsam), and **four video backgrounds**. That ZIP is about **1.87 GB** and expands to approximately **4.08 GB**; it does not include the newer expansion.

Tap **DOWNLOAD SONGS** on the app's main library screen, then **Download / resume**. The expanded standard download is about **2.23 GB**, with additional sound banks available through Sound settings. Existing base-library users download about **1.14 GB** for the new song packs, videos and notices. The five new optional banks total about **373 MB**. Completed song packs appear automatically, and the download panel shows only progress and pause/resume controls.

The app downloads resource packs from this repository's Releases. Downloads can resume, and their SHA-256 hashes are checked. It automatically checks the public release manifest for app updates; Android requires approval to install an update. Future app releases must use the same signing key.

This repository contains the distribution manifest, release tooling, documentation, artwork, and custom launch-animation/feed configuration source. Large media and APK files are Release attachments. Signing keys and account credentials are kept outside this repository.

Third-party notices are retained in `notices/`. Media files and song/artist metadata retain their respective ownership and license terms.
