# Additional resources

All SongHub-derived MIDI/KAR song additions are now withdrawn at the user's request. The current catalogue contains **47,677 original-library files** and retains all eight sound banks and 16 video backgrounds. All 21 published SongHub song ZIPs have been deleted from GitHub releases. See the [current library guide](LYRICS-FILTER.md).

The catalogue linked by the supplied SongHub Downloads APK was inspected on 2026-10-28. The APK opens a website; the media is hosted externally.

## Retained media and withdrawn songs

- The 17,610 SongHub-added MIDI/KAR files in nine packs are withdrawn, including all songs assigned numbers 60212–77821. Older filtered copies on historical releases are also deleted.
- Original song numbers, retired/merged IDs and the personal-import range starting at 90001 are unchanged.
- Twelve MP4 backgrounds and five optional sound banks: FluidR3 GM, FluidR3 GM (Violin fix), GeneralUser GS 1.471, Symphony Hall and Aspirin GM/GS.
- A notices file is downloaded alongside the media to preserve bank licences and author credits.

In SingTonado 1.6.12 or later, open **DOWNLOAD SONGS**, then **Download / resume** and allow it to finish. The downloader removes the withdrawn packs from its managed cache on completion. Download and select optional banks through **Settings / Sound**. Downloaded videos remain available through the background controls. Separately imported folders are unchanged.

The five optional banks total about 373 MB. No APK update is needed for this library withdrawal. The original base offline ZIP is unchanged and does not contain SongHub additions; it predates the original-song lyrics filter.

[Retained banks and videos](https://github.com/rw1nsol/SingTonado/releases/tag/library-songhub-v1) | [Current song-number CSV](https://github.com/rw1nsol/SingTonado/releases/download/library-original-only-v1/SONGS-KEPT.csv)

## Verification

50 available files (7,021,241,609 bytes) were downloaded. Source ZIP CRCs, SF2 structures and MP4 containers passed checks. Every downloaded sound bank loaded and rendered sample notes in FluidSynth 2.6.1 on the computer.

The three source packs contain 119,866 MIDI/KAR files. Duplicates were identified using exact content and normalized titles/artists. Candidate additions were parsed with Java MIDI; malformed, empty or unidentified sequences were excluded. Legacy ZIP names were repackaged for Android/Java, conflicting song numbers were remapped, and one RMID wrapper was converted to standard SMF without changing musical events. New packs passed CRC checks.

14,845 additions contain lyric/text events; the remainder have none and are instrumental MIDI files. Text events do not guarantee complete or correctly timed karaoke lyrics.

At initial publication, all 32 release assets matched GitHub SHA-256 digests. Song packs have since been withdrawn; the retained media remains unchanged. Historical CSV inventories and source audits document the original import and are not the current song list. Phone playback has not been tested.

## Rights and selection

The user confirmed public redistribution permission for song packs and videos. Original musical data and credits were retained. FluidR3 includes the published MIT notice, GeneralUser includes its embedded License v2.0, and Symphony Hall/Aspirin declare Public Domain in their metadata. See notices/Additional-resource-notices.txt.

Other banks remain in the local downloads but are excluded from the public mirror where redistribution terms are unclear or restrictive. Identical bank files are not duplicated. The custom Yamaha/Korg/Ketron/Dream SAM link returned HTTP 403.

Printable songbooks, installers, activation links and external streaming links are not imported app resources. Intro/score sounds were inspected but not installed: the five score MP3s are identical, and the current resource feed does not bind MP3 intro/score effects.

[Source catalogue](https://songhubdownloads.blogspot.com/) | [Copyright notice](https://songhubdownloads.blogspot.com/2026/10/disclaimer-copyright-notice.html)

RESOURCE-AUDIT.json and ADDITIONAL-SONGS.csv on the older release are historical records of the withdrawn import. Use the current release's SONGS-KEPT.csv for available songs.
