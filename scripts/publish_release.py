"""Publish a signed APK and update feed using an authenticated GitHub CLI."""
from pathlib import Path
import argparse,hashlib,json,subprocess,shutil,os,re
from apk_metadata import inspect_apk
from validate_manifest import validate_manifest

def main():
    parser=argparse.ArgumentParser()
    parser.add_argument('--apk',type=Path,required=True)
    parser.add_argument('--version-name',required=True)
    parser.add_argument('--version-code',type=int,required=True)
    parser.add_argument('--notes',type=Path,required=True)
    parser.add_argument('--prepare-only',action='store_true')
    args=parser.parse_args()
    repo_root=Path(__file__).resolve().parent.parent
    settings=json.loads((repo_root/'repository.json').read_text(encoding='utf-8'))
    repository=settings['repository']; tag='v'+args.version_name
    if not re.fullmatch(r'[0-9A-Za-z._-]+',args.version_name): raise ValueError('Invalid version name')
    metadata=inspect_apk(args.apk)
    assert metadata['package']=='app.singtonado',metadata
    assert metadata['versionCode']==args.version_code,metadata
    assert metadata['versionName']==args.version_name,metadata
    staging=repo_root/'release-staging'/tag
    staging.mkdir(parents=True,exist_ok=True)
    apk=staging/'SingTonado.apk'; shutil.copyfile(args.apk,apk)
    digest=hashlib.file_digest(apk.open('rb'),'sha256').hexdigest()
    manifest=json.loads((repo_root/'manifests/resources.json').read_text(encoding='utf-8'))
    manifest['app']={
        'versionCode':args.version_code,'versionName':args.version_name,
        'apk':f'https://github.com/{repository}/releases/download/{tag}/SingTonado.apk',
        'bytes':apk.stat().st_size,'sha256':digest,
        'page':f'https://github.com/{repository}/releases/tag/{tag}',
        'notes':[line.strip() for line in args.notes.read_text(encoding='utf-8').splitlines() if line.strip()],
        'minSdk':metadata.get('minSdkVersion',26)}
    validate_manifest(manifest)
    manifest_path=staging/'manifest.json'
    manifest_path.write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
    shutil.copyfile(manifest_path,repo_root/'manifest.json')
    sums=staging/'SHA256SUMS.txt'
    sums.write_text(digest+'  SingTonado.apk\n'+hashlib.file_digest(manifest_path.open('rb'),'sha256').hexdigest()+'  manifest.json\n')
    print('Prepared',tag,'version code',args.version_code,'SHA-256',digest,flush=True)
    if args.prepare_only: return
    gh=os.environ.get('SINGTONADO_GH','gh')
    def call(arguments,check=True):
        p=subprocess.run([gh]+arguments,capture_output=True,text=True)
        if check and p.returncode: raise RuntimeError(p.stderr)
        return p
    existing=call(['release','view',tag,'--repo',repository,'--json','isDraft,assets'],False)
    if existing.returncode:
        call(['release','create',tag,'--repo',repository,'--title','SingTonado '+args.version_name,'--notes-file',str(args.notes),'--draft'])
    elif not json.loads(existing.stdout)['isDraft']:
        raise RuntimeError('This release is already public; publish a new version instead of changing it.')
    for path in (apk,manifest_path,sums):
        call(['release','upload',tag,str(path),'--repo',repository,'--clobber'])
    # GitHub does not resolve unpublished draft tags through the by-tag endpoint.
    releases=json.loads(call(['api',f'repos/{repository}/releases?per_page=100']).stdout)
    release=next(item for item in releases if item['tag_name']==tag)
    asset=next(a for a in release['assets'] if a['name']=='SingTonado.apk')
    assert asset['size']==apk.stat().st_size
    if asset.get('digest'): assert asset['digest']=='sha256:'+digest
    call(['release','edit',tag,'--repo',repository,'--draft=false','--latest=true'])
    print('Published: https://github.com/'+repository+'/releases/tag/'+tag,flush=True)

if __name__=='__main__': main()
