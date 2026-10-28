# Download library lyrics filter

The 2026-10-28 library update reviews all 65,302 MIDI/KAR files and removes 3,239 song files without lyrics. Song **69194 — (219013)** is additionally removed at the user's request, bringing total removals to 3,240. The current catalogue retains 62,062 MIDI/KAR files, including sound effects, eight sound banks, 16 video backgrounds and the existing notices. App artwork is unchanged; no separate photo pack existed in the catalogue.

In SingTonado 1.6.12, open **DOWNLOAD SONGS**, then **Download / resume**. Allow it to finish: 17 song packs are replaced and their superseded cached files are removed after completion. Restart the app if an older list remains visible. No APK update is required. Personal imports are unchanged; disable an older manually imported folder separately if it contains unwanted songs.

[Current resource release](https://github.com/rw1nsol/SingTonado/releases/tag/library-cleanup-v1) | [Kept files](https://github.com/rw1nsol/SingTonado/releases/download/library-cleanup-v1/SONGS-KEPT.csv) | [Removed songs](https://github.com/rw1nsol/SingTonado/releases/download/library-cleanup-v1/SONGS-REMOVED.csv)

## Checks

The review scans MIDI text and lyric metadata across tracks. Header-only, credits-only, chord-only and arrangement-marker-only files are excluded. Sparse cases are reviewed to preserve short songs and real lyrics stored without timing. Fanfares and videoke applause remain as non-song sounds. Five original files have malformed MIDI structure but recoverable embedded lyrics; those are retained rather than discarded as instrumental files.

Retained entries in replacement ZIPs have byte-identical contents and the same filenames and song numbers. Unchanged packs are reused. Every replacement ZIP passes CRC checks, and its entries are compared with the retained source bytes. All 25 non-song manifest entries remain identical. The APK and update metadata remain unchanged. GitHub release digests and the public feed are checked after upload.

Lyric metadata does not guarantee complete lyrics, correct encoding or synchronization. Phone playback has not been tested. Older resource releases are historical snapshots and may still include instrumental songs. For manual setup, use only song packs listed in the current manifest and a fresh folder.
