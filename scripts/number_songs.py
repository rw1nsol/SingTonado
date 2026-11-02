"""Assign permanent random SingTonado numbers and rebuild original-numbered ZIPs.

Always use original/source-numbered input packs, never already numbered output.
Keep the registry forever: existing assignments and retired numbers are not reused.
"""
from pathlib import Path, PurePosixPath
import argparse, copy, csv, datetime, hashlib, json, random, re, zipfile

MIGRATION_ID = 'singtonado-numbering-v1'


def assign_numbers(registry, sources, blocked=(), rng=None):
    rng = rng or random.SystemRandom()
    rows = registry.setdefault('songs', {})
    registry.setdefault('schema', 1)
    registry.setdefault('migration_id', MIGRATION_ID)
    if registry['migration_id'] != MIGRATION_ID:
        raise ValueError('Do not change the migration ID or shuffle existing assignments')
    used = {int(row['number']) for row in rows.values()}
    if len(used) != len(rows):
        raise ValueError('Duplicate number in permanent registry')
    forbidden = {int(n) for n in blocked}
    if used & forbidden:
        raise ValueError('A retirement/merge source overlaps an assigned number')
    unavailable = used | forbidden
    available = [n for n in range(10000, 90000) if n not in unavailable]
    rng.shuffle(available)
    for source in sources:
        key = str(source)
        if key in rows:
            if int(rows[key]['number']) == int(source):
                raise ValueError('SingTonado number must differ from source number')
            continue
        if not available:
            raise ValueError('Five-digit number space exhausted; do not reuse retired numbers')
        if available[-1] == int(source):
            other = next((i for i, n in enumerate(available) if n != int(source)), None)
            if other is None:
                raise ValueError('No number different from source remains')
            available[-1], available[other] = available[other], available[-1]
        rows[key] = {'number': str(available.pop())}
    return registry


def build(manifest_path, roots, registry_path, output, release):
    manifest = json.loads(manifest_path.read_text(encoding='utf-8'))
    if manifest.get('renumber'):
        raise ValueError('Input must be an original/source-numbered manifest')
    output.mkdir(parents=True, exist_ok=True)
    registry = json.loads(registry_path.read_text(encoding='utf-8')) if registry_path.exists() else {}
    blocked = set(manifest.get('retired', [])) | set(manifest.get('merged', {}))
    blocked.update(registry.get('blocked_source_numbers', []))
    packs = []; seen = set(); entries = []
    for item in manifest['items']:
        if item['kind'] != 'songs':
            continue
        filename = item['url'].rsplit('/', 1)[1]
        path = next((p / filename for p in roots if (p / filename).is_file()), None)
        if path is None:
            raise FileNotFoundError(filename)
        if hashlib.file_digest(path.open('rb'), 'sha256').hexdigest() != item['sha256']:
            raise ValueError('Source archive checksum mismatch: ' + filename)
        with zipfile.ZipFile(path) as z:
            pack_entries = []
            for info in z.infolist():
                if info.is_dir():
                    continue
                name = PurePosixPath(info.filename)
                match = re.match(r'^(\d{5}) - (.+)\.(mid|kar)$', name.name, re.I)
                if not match:
                    raise ValueError('Unexpected song filename: ' + info.filename)
                source = match[1]
                if source in seen:
                    raise ValueError('Source number appears more than once: ' + source)
                seen.add(source)
                row = {'source_number': source, 'pack': item['id'], 'source_file': info.filename}
                pack_entries.append(row); entries.append(row)
            packs.append((item, path, pack_entries))
    assign_numbers(registry, sorted(seen), blocked)
    registry['blocked_source_numbers'] = sorted(blocked, key=int)
    new_manifest = copy.deepcopy(manifest)
    new_items = {i['id']: i for i in new_manifest['items']}
    rows = []
    for item, path, pack_entries in packs:
        target = output / ('singtonado-' + item['id'] + '.zip')
        with zipfile.ZipFile(path) as src, zipfile.ZipFile(target, 'w', compression=zipfile.ZIP_DEFLATED, compresslevel=6) as dest:
            for row in pack_entries:
                source = row['source_number']; name = PurePosixPath(row['source_file'])
                assigned = registry['songs'][source]
                new_name = str(name.with_name(assigned['number'] + name.name[5:]))
                payload = src.read(row['source_file'])
                digest = hashlib.sha256(payload).hexdigest()
                previous_file = assigned.get('source_file')
                if previous_file and PurePosixPath(previous_file).name[5:] != name.name[5:]:
                    raise ValueError('Source number identity changed; review aliases before publishing: ' + source)
                assigned.update(source_file=row['source_file'], sha256=digest)
                info = zipfile.ZipInfo(new_name, (2026, 1, 1, 0, 0, 0)); info.compress_type = zipfile.ZIP_DEFLATED
                dest.writestr(info, payload)
                rows.append(dict(row, number=assigned['number'], file=new_name, sha256=digest))
        changed = new_items[item['id']]
        changed.update(bytes=target.stat().st_size, sha256=hashlib.file_digest(target.open('rb'), 'sha256').hexdigest(),
                       url=f'https://github.com/rw1nsol/SingTonado/releases/download/{release}/{target.name}')
        print('Built', target.name, len(pack_entries), 'songs', flush=True)
    # Keep this ID stable when future source songs are added. Existing marks are
    # already migrated and MUST NOT be passed through the old-to-new map again.
    new_manifest['renumber'] = {'id': MIGRATION_ID,
        'map': {s: r['number'] for s, r in sorted(registry['songs'].items())}, 'gone': []}
    fingerprint = hashlib.sha256(json.dumps(new_manifest['items'], sort_keys=True).encode()).hexdigest()[:16]
    new_manifest['version'] = 'resources-singtonado-numbers-' + fingerprint
    new_manifest['built'] = datetime.datetime.now(datetime.timezone.utc).isoformat().replace('+00:00', 'Z')
    new_manifest['bytes'] = sum(i['bytes'] for i in new_manifest['items'])
    new_manifest['app'] = None
    registry_path.parent.mkdir(parents=True, exist_ok=True)
    registry_path.write_text(json.dumps(registry, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    (output / 'resources.json').write_text(json.dumps(new_manifest, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    with (output / 'SONG-NUMBERS.csv').open('w', encoding='utf-8-sig', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['number', 'source_number', 'pack', 'file', 'source_file', 'sha256'])
        writer.writeheader(); writer.writerows(sorted(rows, key=lambda r: int(r['number'])))
    (output / 'NUMBERING-REPORT.json').write_text(json.dumps({
        'songs': len(rows), 'packs': len(packs), 'unique_numbers': len({r['number'] for r in rows}),
        'every_number_differs_from_source': all(r['number'] != r['source_number'] for r in rows),
        'song_payloads_unchanged': True, 'migration_id': MIGRATION_ID,
        'range': [10000, 89999], 'personal_import_range_reserved': [90001, 99999],
        'persistent_registry': True}, indent=2) + '\n', encoding='utf-8')
    return new_manifest


if __name__ == '__main__':
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--manifest', type=Path, required=True)
    p.add_argument('--pack-root', type=Path, action='append', required=True)
    p.add_argument('--registry', type=Path, required=True)
    p.add_argument('--output', type=Path, required=True)
    p.add_argument('--release', required=True)
    a = p.parse_args()
    build(a.manifest, a.pack_root, a.registry, a.output, a.release)
