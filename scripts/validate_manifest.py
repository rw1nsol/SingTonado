"""Validate the app-release JSON types expected by the Android serializer."""
import re

def validate_manifest(manifest):
    if not isinstance(manifest, dict) or not isinstance(manifest.get('items'), list):
        raise ValueError('Manifest must contain an items array')
    app = manifest.get('app')
    if app is None:
        return
    if not isinstance(app, dict):
        raise ValueError('app must be an object')
    for key in ('versionCode', 'minSdk', 'bytes'):
        if type(app.get(key)) is not int or app[key] <= 0:
            raise ValueError('app.' + key + ' must be a positive integer')
    for key in ('versionName', 'apk', 'sha256', 'page'):
        if not isinstance(app.get(key), str) or not app[key]:
            raise ValueError('app.' + key + ' must be a nonempty string')
    if not re.fullmatch(r'[0-9a-f]{64}', app['sha256']):
        raise ValueError('app.sha256 must be a lowercase SHA-256 digest')
    notes = app.get('notes')
    if not isinstance(notes, list) or any(not isinstance(note, str) for note in notes):
        raise ValueError('app.notes must be an array of strings, as required by Android AppRelease')
