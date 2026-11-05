# Additional background videos

The four supplied MP4 videos are downloadable singing backgrounds. Their original bytes and resolutions are preserved.

| Background | Resolution | Approximate download |
| --- | --- | ---: |
| Caixão no Elevador | 1920 × 1080 | 112 MB |
| Cobra no Banheiro | 1920 × 1080 | 56 MB |
| Pegadinhas de Elevador — Compilados | 1280 × 720 | 126 MB |
| Buraco no Parque | 1920 × 1080 | 133 MB |

Total exact download size: **426,267,542 bytes**, about 426 MB. The background catalogue now contains **20 videos** and the complete resource feed contains **92 entries**.

## On the phone

Open **DOWNLOAD SONGS → Download / resume** and let the downloads finish. Then open **Settings → Background** and enable/use video backgrounds. If Light downloads are enabled, use the background-video download controls in Settings so videos are downloaded. Reopen the app if its list has not refreshed.

No APK update is required; SingTonado remains at 1.9.1. Completed files are available offline. The background player sets its volume to zero, so original clip audio does not play over the karaoke backing track. These are background resources, not numbered songs.

The current 47,679 songs, all permanent number assignments, the existing 11 soundfonts and all 88 prior resource entries are retained.

## Validation

All four were inspected with FFprobe: H.264 video with yuv420p pixel format in MP4 containers. FFmpeg decoded one-second samples at the beginning, middle and end of each clip without errors. The copied/uploaded files have matching SHA-256 hashes; source resolutions, audio streams and embedded metadata are preserved. The Android background-player mute behavior was confirmed in the compiled app source.

These are desktop media and download checks, not phone runtime tests or a full frame-by-frame review. The structured media report is in `manifests/user-backgrounds-validation.json`. Files and their checksums are hosted in [the background resource release](https://github.com/rw1nsol/SingTonado/releases/tag/backgrounds-user-v1).
