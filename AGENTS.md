# SingTonado maintenance

The user requires song numbers to differ from the upstream app. Preserve the
permanent assignments in `manifests/song-number-registry.json` on every app and
resource update. New source songs receive random unused five-digit SingTonado
numbers using `scripts/number_songs.py`. Existing numbers must stay fixed; never
recreate the registry, recycle removed numbers or change the migration ID
`singtonado-numbering-v1`. Follow `docs/SONG-NUMBERS.md` and run the numbering tests.
When rebasing onto a newer upstream APK, retain compatible manifest decoding and
the one-time migration of marks and lyric fixes; test them before publication.

Reconcile upstream source-ID changes before publication. Keep the registry,
numbered pack filenames, migration map and current song index consistent. Upload
and verify all resource assets before replacing the app's live manifest.

All SongHub-derived song additions remain withdrawn. Preserve their exclusions
and the existing lyric filter when updating sources. Keep authorized non-song
resources and SingTonado branding. Never commit signing keys or credentials.
Keep DOWNLOAD SONGS text-only in every layout, with no heart icon, and retain its
progress-only download panel when integrating newer upstream app versions.

Keep all 11 downloadable sound banks available in Sound. Do not restore the
four-item truncation in SoundManager.options when rebasing on upstream updates.
The three user-supplied banks are optional downloads; retain their names, file
checksums and creator metadata. See docs/SOUNDFONTS.md.

For renumbered upstream catalogues, compare content and title/artist aliases first.
Keep original numeric registry rows and migration map untouched. Register new
content with SHA-256 identities in `additions` using `assign_additions()`; both
registry sections reserve numbers permanently. The current library has 47,679
songs; see docs/LIBRARY-UPDATE-1.9.md and the current complete release index.

Upstream lyric-search databases use upstream numeric docids. Do not publish a raw
upstream lyrics DB after SingTonado renumbering; rebuild or remap its search index
to the current SingTonado numbers first.
