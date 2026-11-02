# Permanent SingTonado song numbers

The managed catalogue uses unique random five-digit SingTonado numbers from 10000 through 89999. Every initial assignment differs from its original source number. Personal imports use their existing numbers; the app's 90001-and-up import range is reserved. Titles, artists, categories, lyric data and MIDI bytes are unchanged.

## On the phone

Use SingTonado 1.8.0 or later so saved song references can migrate. Open DOWNLOAD SONGS → Download / resume, let all packs finish, then reopen the app. The app maps existing favorites, history, playlists and saved lyric fixes to the new numbers once. During the refresh, songs whose replacement pack is still pending may be temporarily unavailable. Personally imported or manually copied folders are outside the managed library and keep their own filenames/numbers; disable an old copied library folder if it appears alongside the managed catalogue.

Search by title or artist to find a song again. Reopen Songbook and export a fresh PDF; existing printed or exported songbooks keep their old numbers. [Current SingTonado number index](https://github.com/rw1nsol/SingTonado/releases/download/library-singtonado-numbers-v1/SONG-NUMBERS.csv) includes the old-to-new mapping.

This is a resource-library update. The APK version can remain 1.8.0 while the song packs refresh.

## Future upstream app and song updates

`manifests/song-number-registry.json` is permanent. Preserve it in every future app/resource update. Existing songs keep their assigned SingTonado number. Only newly added source songs receive a new random unused number. Never reshuffle the catalogue, recycle a removed song's assigned number, copy upstream numbers directly, or change the migration ID `singtonado-numbering-v1`.

Before publishing source-numbered song additions or replacements, run `scripts/number_songs.py` using the permanent registry and the checksum-verified source manifest/packs. The tool rewrites ZIP filenames, preserves the MIDI payload, updates hashes/URLs, emits an old-to-new index and the migration map, and rejects duplicate source numbers or an unexpected title change for an existing source number. Input must be original/source-numbered archives, not the already-renumbered outputs. Use a new resource release tag for changed archives; keep historical release assets immutable.

```text
python scripts/number_songs.py --manifest SOURCE_MANIFEST.json --pack-root SOURCE_PACK_DIRECTORY --registry manifests/song-number-registry.json --output NUMBERED_RELEASE_DIRECTORY --release NEW_RESOURCE_TAG
python scripts/test_number_songs.py
```

Review any upstream renumbering or reused source ID before accepting it. Reconcile its source identity to the existing registry instead of treating an existing song as new. The registry retains a source filename and content hash to help that review. For a future retirement, reserve its assigned number forever and ensure the outgoing retirement/merge lists refer to the correct identities and cannot remove another active song's assigned number. The tool refuses assigned numbers overlapping retirement/merge sources.

Preserve prior exclusions and the withdrawal of SongHub-derived song additions. Numbering changes do not authorize restoring excluded songs. Sound banks, videos and other non-song resources are untouched.

Publish and verify every new ZIP before changing the live feed. Keep the registry, resource manifest and CSV together. The app publisher refuses to use a resource feed whose migration map disagrees with the permanent registry. Resource releases must remain marked as not latest; the latest app release hosts the feed.

The migration ID stays fixed when the map gains entries: existing users have already migrated and must not pass their current numbers through the original-code map again. New songs arrive already numbered in their ZIP filenames. New installations have no old favorites/history to move.

## Verification

The release rebuild is checked against all 47,677 current files, requiring unique changed numbers, unchanged song bytes, no reserved/retired-number collisions, and the same 25 non-song resources. Automated tests cover permanent assignments when adding songs, idempotent reruns, uniqueness and invalid manifests. The existing 1.8.0 code contains the one-time migration of favorites, history, playlists and lyric fixes. Phone migration has not been runtime-tested.
