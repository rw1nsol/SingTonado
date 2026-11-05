# Songs added from the 1.9 catalogue

The catalogue accessed using the supplied 1.9.0 APK was downloaded and compared against SingTonado. Its manifest version was `91e7a76ff8f84b9b`. All 57 song ZIPs passed their expected byte-size/SHA-256 checks and ZIP CRC checks; the source contained 47,904 MIDI/KAR files.

## Added songs

| SingTonado number | Title | Source artist credit | Timed lyric events |
| --- | --- | --- | ---: |
| 81056 | Bluer Than Blue | Barry Manilow | 257 |
| 52024 | 1963 | New Order | 407 |

Artist credits follow the source filenames. Both files contain real lyric meta events rather than only chords or metadata. Song bytes are unchanged; only the ZIP filenames receive random unused SingTonado numbers.

The library now contains **47,679 songs** in **60 song packs**, **11 sound banks**, **16 videos** and a notice (88 total resource entries). All 47,677 previous song-number assignments and all 87 previous resource entries are retained. The APK stays at 1.9.1.

## Duplicate and exclusion review

The upstream numbering differs from SingTonado, so numbers alone were not used as song identities. The initial comparison found 27,507 files already present by content hash, 14,923 already represented by title/artist, 4,906 excluded by prior source/lyric rules, 567 candidates and one repeated candidate. Further checking resolved artist placeholders, leading artist-name variants and the upstream's legacy identity map. Another withdrawn title/artist alias was excluded. The Charli XCX entry for 1999 is already represented by the older Charli XCX & Troye Sivan copy, so the old copy was retained.

The three final candidates were also compared using strict normalized MIDI note-event fingerprints against 65,302 original/imported historical files. No exact fingerprint match was found. This supplementary comparison could not read or support 52 reference files and is not proof of musical or audible uniqueness. Combined with the content, metadata and source-identity checks, two additions were retained; all previously withdrawn SongHub-derived song additions remain excluded.

No source credentials or private signed source URLs are published. The sanitized audit is in `manifests/upstream-1.9-additions-report.json`. The one-time numbering migration map remains unchanged; new songs use content identities in the permanent registry's `additions` section to avoid overwriting legacy source IDs reused upstream.

## On the phone

Open **DOWNLOAD SONGS → Download / resume**, let it finish and reopen the app. Search by title/artist or enter the new numbers. Reopen Songbook and export again to include the additions. This is a resource update, so Check for an update may correctly report that APK 1.9.1 is current.

[New-song list](https://github.com/rw1nsol/SingTonado/releases/download/library-update-190-v1/NEW-SONGS.csv) · [Complete current index](https://github.com/rw1nsol/SingTonado/releases/download/library-update-190-v1/SONG-NUMBERS.csv)

The new ZIP passes CRC and every retained song payload matches its source SHA-256. The full existing resource inventory, migration map and all original number assignments are checked for equality before publishing. No phone runtime test was available.
