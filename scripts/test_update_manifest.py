import copy
import json
import unittest
from pathlib import Path
from validate_manifest import validate_manifest

class UpdateManifestTests(unittest.TestCase):
    def setUp(self):
        self.manifest = json.loads((Path(__file__).resolve().parent.parent/'manifest.json').read_text(encoding='utf-8'))

    def test_current_manifest_matches_android_release_schema(self):
        validate_manifest(self.manifest)

    def test_text_notes_reproduce_rejected_feed(self):
        broken = copy.deepcopy(self.manifest)
        broken['app']['notes'] = 'A release note encoded as a single string'
        with self.assertRaisesRegex(ValueError, 'array of strings'):
            validate_manifest(broken)

    def test_numeric_notes_are_rejected(self):
        broken = copy.deepcopy(self.manifest)
        broken['app']['notes'] = [42]
        with self.assertRaisesRegex(ValueError, 'array of strings'):
            validate_manifest(broken)

if __name__ == '__main__':
    unittest.main()
