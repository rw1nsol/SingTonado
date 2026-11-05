# Additional soundfonts and SingTonado 1.9.1

The supplied files are now optional downloadable Sound banks. Their original bytes and embedded creator metadata are preserved.

| Bank | Download size | Presets | General MIDI programs |
| --- | ---: | ---: | ---: |
| KaraOke King v2 | 98,645,536 bytes (about 99 MB) | 234 | 128 |
| Platinum 3.1 (custom made by Arkyn) | 141,454,402 bytes (about 141 MB) | 294 | 128 |
| Yamaha x Korg x Ketron x Dream Sam (custom made by JC) | 948,538,844 bytes (about 949 MB) | 362 | 128 |

## On the phone

1. Install SingTonado **1.9.1** using Settings → App → Check for an update, or install the release APK over the existing app.
2. Open DOWNLOAD SONGS once to refresh the resource catalogue, then return to Settings → Sound.
3. Tap Download beside the bank you want. Wait for its download to finish, then select the downloaded bank as the sound.
4. Reopen the app if the new names have not appeared after refreshing the catalogue.

These banks are downloaded separately from the APK and are not forced into the normal download. Once downloaded, they can be used offline. The normal resumable downloader verifies their byte sizes and SHA-256 hashes.

Version 1.9.1 removes the four-bank truncation in the downloaded Sound selector. It retains the existing bank order and playback/memory safeguards; the selector can now expose all downloaded banks rather than hiding entries after the fourth. It retains the text-only DOWNLOAD SONGS button and all 1.9.0 app features.

The catalogue grows from eight to **11 sound banks**, and from 84 to **87 total resource entries**. The existing 59 song packs, 16 videos, notice, sound banks and 47,677 permanent song numbers are retained. This release does not import new upstream song packs; the 1.9.0 app features and bundled resources were integrated in the prior app release.

## Validation

All three pass RIFF/SF2 structure, chunk bounds, preset-table, sample-table and sample-range checks. Each exposes all 128 General MIDI programs and drum presets. A native desktop FluidSynth render produced non-silent audio for each bank. File hashes and embedded metadata are retained in `manifests/user-soundfonts-validation.json` and the soundfont resource release. This verifies file compatibility with FluidSynth; it is not a phone playback or full preset-by-preset listening test.

The APK must pass signature/certificate, package/version, alignment, branding and compiled-code checks before publishing. The existing bank-selection code, download offers and public resource checksums are used. The permanent number registry and all prior resource entries must remain unchanged.
