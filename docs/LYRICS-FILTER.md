# Download library lyrics filter

The 2026-10-28 library update reviews all 65,302 MIDI/KAR files and removes 3,239 song files without lyrics. Song **69194 — (219013)** and two newer duplicate entries are additionally removed at the user's request, bringing total removals to 3,242. The current catalogue retains 62,060 MIDI/KAR files, including sound effects, eight sound banks, 16 video backgrounds and the existing notices. App artwork is unchanged; no separate photo pack existed in the catalogue.

In SingTonado 1.6.12, open **DOWNLOAD SONGS**, then **Download / resume**. Allow it to finish: 17 song packs are replaced and their superseded cached files are removed after completion. Restart the app if an older list remains visible. No APK update is required. Personal imports are unchanged; disable an older manually imported folder separately if it contains unwanted songs.

[Current resource release](https://github.com/rw1nsol/SingTonado/releases/tag/library-dedup-v1) | [Kept files](https://github.com/rw1nsol/SingTonado/releases/download/library-dedup-v1/SONGS-KEPT.csv) | [Removed songs](https://github.com/rw1nsol/SingTonado/releases/download/library-dedup-v1/SONGS-REMOVED.csv)

## New duplicate cleanup

The 62,062-file catalogue contained no byte-identical files and 44 normalized title/artist pairs. Of these, 42 pairs were already in the original library and are retained. For Creep / Radiohead, original entry 14224 is retained and addition 68523 is removed. Of the two Corazon Espinado / Santana additions, earlier-numbered entry 73303 is retained and 75431 is removed. Their filenames have the title and artist reversed; the retained filename is unchanged. Matching names do not establish identical arrangements, but the user requested retaining older entries and deleting newer duplicates. Other artists' covers are not removed. This cleanup replaces Additional Songs packs 5 and 8.

## Checks

The review scans MIDI text and lyric metadata across tracks. Header-only, credits-only, chord-only and arrangement-marker-only files are excluded. Sparse cases are reviewed to preserve short songs and real lyrics stored without timing. Fanfares and videoke applause remain as non-song sounds. Five original files have malformed MIDI structure but recoverable embedded lyrics; those are retained rather than discarded as instrumental files.

Retained entries in replacement ZIPs have byte-identical contents and the same filenames and song numbers. Unchanged packs are reused. Every replacement ZIP passes CRC checks, and its entries are compared with the retained source bytes. All 25 non-song manifest entries remain identical. The APK and update metadata remain unchanged. GitHub release digests and the public feed are checked after upload.

Lyric metadata does not guarantee complete lyrics, correct encoding or synchronization. Phone playback has not been tested. Older resource releases are historical snapshots and may still include instrumental songs. For manual setup, use only song packs listed in the current manifest and a fresh folder.
