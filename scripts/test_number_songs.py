import copy, random, unittest
from number_songs import assign_numbers, assign_additions, validate_registry
from validate_manifest import validate_manifest


class NumberingTests(unittest.TestCase):
    def test_renumbered_upstream_additions_cannot_overwrite_legacy_ids(self):
        registry = assign_numbers({}, ['10001', '10002'], rng=random.Random(1))
        old = copy.deepcopy(registry['songs'])
        new = {'sha256': 'a' * 64, 'source_code': '10001', 'file': '10001 - New - Singer.mid',
               'source_version': 'upstream-new', 'title': 'New', 'artist': 'Singer'}
        assign_additions(registry, [new], random.Random(3))
        self.assertEqual(registry['songs'], old)
        number = registry['additions'][new['sha256']]['number']
        self.assertNotEqual(number, '10001')
        assign_additions(registry, [dict(new, source_code='40000')], random.Random(4))
        self.assertEqual(registry['additions'][new['sha256']]['number'], number)
        self.assertEqual(len(registry['additions']), 1)
        assign_numbers(registry, ['10003'], rng=random.Random(5))
        self.assertNotEqual(registry['songs']['10003']['number'], number)
        validate_registry(registry)

    def test_unique_different_and_reserved_numbers(self):
        blocked = [str(n) for n in range(60000, 78000)]
        registry = assign_numbers({}, range(10000, 58000), blocked, random.Random(1))
        rows = registry['songs']
        values = [int(row['number']) for row in rows.values()]
        self.assertEqual(len(set(values)), 48000)
        self.assertTrue(all(10000 <= n < 90000 for n in values))
        self.assertFalse(set(values) & set(map(int, blocked)))
        self.assertTrue(all(source != row['number'] for source, row in rows.items()))

    def test_existing_numbers_stay_when_new_songs_arrive(self):
        registry = assign_numbers({}, ['10001', '10002'], rng=random.Random(1))
        before = copy.deepcopy(registry['songs'])
        assign_numbers(registry, ['10002', '10001', '80001'], rng=random.Random(9))
        self.assertTrue(all(registry['songs'][key] == value for key, value in before.items()))
        self.assertNotIn(registry['songs']['80001']['number'], [r['number'] for r in before.values()])
        after = copy.deepcopy(registry)
        assign_numbers(registry, ['80001'], rng=random.Random(2))
        self.assertEqual(registry, after)

    def test_manifest_rejects_collisions_and_changed_migration_id(self):
        manifest = {'items': [], 'renumber': {'id': 'singtonado-numbering-v1',
                    'map': {'10001': '80001', '10002': '80002'}, 'gone': []}}
        validate_manifest(manifest)
        invalid = copy.deepcopy(manifest); invalid['renumber']['map']['10002'] = '80001'
        with self.assertRaises(ValueError): validate_manifest(invalid)
        invalid = copy.deepcopy(manifest); invalid['renumber']['id'] = 'reshuffle'
        with self.assertRaises(ValueError): validate_manifest(invalid)
        invalid = copy.deepcopy(manifest); invalid['retired'] = ['80001']
        with self.assertRaises(ValueError): validate_manifest(invalid)


if __name__ == '__main__': unittest.main()
