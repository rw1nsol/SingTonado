# Additional resources

The current catalogue has since been filtered to remove songs without lyrics. After additionally removing song 69194 and two newer duplicate entries at the user's request, it retains 62,060 MIDI/KAR files, including sound effects, plus all banks and videos. Older duplicate entries are retained. The historical counts below describe the initial expansion. Use the current app downloader and [lyrics filter guide](LYRICS-FILTER.md).

The catalogue linked by the supplied SongHub Downloads APK was inspected on 2026-10-28. The APK opens a website; the media is hosted externally.

## Added to SingTonado

- 17,610 playable MIDI/KAR files in nine packs; 65,302 total files in the catalogue.
- New song numbers 60212-77821. Existing numbers, retired/merged IDs and the personal-import range starting at 90001 are unchanged.
- Twelve MP4 backgrounds and five optional sound banks: FluidR3 GM, FluidR3 GM (Violin fix), GeneralUser GS 1.471, Symphony Hall and Aspirin GM/GS.
- A notices file is downloaded alongside the media to preserve bank licences and author credits.

In SingTonado 1.6.12, open **DOWNLOAD SONGS**, then **Download / resume**. The catalogue refreshes and completed packs appear automatically under Additional Songs. Search by title, artist or new number. Download and select optional banks through **Settings / Sound**. Downloaded videos become available through the existing background controls.

Existing base-library users need about 1.14 GB for the standard expansion. The five optional banks total about 373 MB. No APK update is needed if 1.6.12 is installed. The original base offline ZIP is unchanged and excludes this expansion.

[Resource release](https://github.com/rw1nsol/SingTonado/releases/tag/library-songhub-v1) | [Song-number CSV](https://github.com/rw1nsol/SingTonado/releases/download/library-songhub-v1/ADDITIONAL-SONGS.csv)

## Verification

50 available files (7,021,241,609 bytes) were downloaded. Source ZIP CRCs, SF2 structures and MP4 containers passed checks. Every downloaded sound bank loaded and rendered sample notes in FluidSynth 2.6.1 on the computer.

The three source packs contain 119,866 MIDI/KAR files. Duplicates were identified using exact content and normalized titles/artists. Candidate additions were parsed with Java MIDI; malformed, empty or unidentified sequences were excluded. Legacy ZIP names were repackaged for Android/Java, conflicting song numbers were remapped, and one RMID wrapper was converted to standard SMF without changing musical events. New packs passed CRC checks.

14,845 additions contain lyric/text events; the remainder have none and are instrumental MIDI files. Text events do not guarantee complete or correctly timed karaoke lyrics.

All 32 release assets match GitHub SHA-256 digests. Anonymous sample downloads of a notice, song pack, bank and video match local bytes. Phone playback has not been tested.

## Rights and selection

The user confirmed public redistribution permission for song packs and videos. Original musical data and credits were retained. FluidR3 includes the published MIT notice, GeneralUser includes its embedded License v2.0, and Symphony Hall/Aspirin declare Public Domain in their metadata. See notices/Additional-resource-notices.txt.

Other banks remain in the local downloads but are excluded from the public mirror where redistribution terms are unclear or restrictive. Identical bank files are not duplicated. The custom Yamaha/Korg/Ketron/Dream SAM link returned HTTP 403.

Printable songbooks, installers, activation links and external streaming links are not imported app resources. Intro/score sounds were inspected but not installed: the five score MP3s are identical, and the current resource feed does not bind MP3 intro/score effects.

[Source catalogue](https://songhubdownloads.blogspot.com/) | [Copyright notice](https://songhubdownloads.blogspot.com/2026/10/disclaimer-copyright-notice.html)

RESOURCE-AUDIT.json on the release records the per-file decisions. ADDITIONAL-SONGS.csv maps the new numbers to original archives and files.
