# Download library lyrics filter

The current catalogue contains **47,677 original-library MIDI/KAR files** in 59 packs. All **17,610 SongHub-added MIDI/KAR files**, assigned numbers 60212–77821, are withdrawn at the user's request. This removes the remaining 14,383 active entries; the others had already been filtered or individually removed. Fifteen original files without usable lyrics remain excluded. Total exclusions from the former 65,302-file catalogue are 17,625.

The eight sound banks, 16 video backgrounds, notices and app artwork are preserved. All 21 published SongHub song ZIPs, including historical filtered variants, are deleted from GitHub releases. CSV indexes and audits on older releases are historical records, not song downloads.

In SingTonado 1.6.12 or later, open **DOWNLOAD SONGS**, then **Download / resume**. Allow it to finish: the downloader removes all nine withdrawn packs from its managed cache on completion. Restart the app if an older list remains visible. No APK update is required. Personal imports are unchanged; disable or remove a separately imported SongHub folder yourself. Reopen Songbook and export a new PDF if needed.

[Current resource release](https://github.com/rw1nsol/SingTonado/releases/tag/library-original-only-v1) | [Kept files](https://github.com/rw1nsol/SingTonado/releases/download/library-original-only-v1/SONGS-KEPT.csv) | [Removed songs](https://github.com/rw1nsol/SingTonado/releases/download/library-original-only-v1/SONGS-REMOVED.csv)

## Historical duplicate cleanup

The earlier 62,062-file catalogue contained no byte-identical files and 44 normalized title/artist pairs. The 42 pairs in the original library are retained. The subsequent withdrawal removes every SongHub addition, including 68523, 69194, 73303 and 75431; original Creep / Radiohead entry 14224 remains. Original song numbers are unchanged.

## Checks

The earlier review scanned MIDI text and lyric metadata across tracks. Header-only, credits-only, chord-only and arrangement-marker-only files were excluded. Sparse cases were reviewed to preserve short songs and real lyrics stored without timing. SongHub fanfare and applause MIDI entries are withdrawn along with their packs. Five original files have malformed MIDI structure but recoverable embedded lyrics; those are retained rather than discarded as instrumental files.

Retained original packs and song numbers are unchanged by the withdrawal. All 25 non-song manifest entries remain identical. The APK and app-update metadata remain unchanged. The current feed and the older 1.6.12 app-release feed exclude all nine SongHub song packs. Historical asset inventories are checked to confirm all 21 SongHub song ZIPs are deleted while original song packs, banks, videos and APKs remain intact.

Lyric metadata does not guarantee complete lyrics, correct encoding or synchronization. Phone playback has not been tested. Older resource releases are historical snapshots and may still include instrumental songs. For manual setup, use only song packs listed in the current manifest and a fresh folder.
