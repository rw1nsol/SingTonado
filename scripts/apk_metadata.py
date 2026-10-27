"""Read package/version/min-SDK from a compiled Android manifest, without aapt."""
import struct
import zipfile

def inspect_apk(path):
    with zipfile.ZipFile(path) as z:
        data=z.read('AndroidManifest.xml')
    strings=[]; result={}; cursor=8
    def u16(p): return struct.unpack_from('<H',data,p)[0]
    def u32(p): return struct.unpack_from('<I',data,p)[0]
    while cursor<len(data):
        kind,header,size=struct.unpack_from('<HHI',data,cursor)
        if size<8 or cursor+size>len(data): raise ValueError('Invalid compiled XML chunk')
        if kind==1:
            count=u32(cursor+8); utf8=bool(u32(cursor+16)&256); base=cursor+u32(cursor+20)
            for i in range(count):
                p=base+u32(cursor+header+i*4)
                if utf8:
                    first=data[p]; p+=2 if first&128 else 1
                    length=data[p]; p+=1
                    if length&128: length=((length&127)<<8)|data[p]; p+=1
                    strings.append(data[p:p+length].decode('utf-8'))
                else:
                    length=u16(p); p+=2
                    if length&32768: length=((length&32767)<<16)|u16(p); p+=2
                    strings.append(data[p:p+length*2].decode('utf-16le'))
        elif kind==0x102:
            element=strings[u32(cursor+20)]
            if element in ('manifest','uses-sdk'):
                start=u16(cursor+24); width=u16(cursor+26); count=u16(cursor+28)
                for i in range(count):
                    p=cursor+16+start+i*width
                    name=strings[u32(p+4)]; raw=u32(p+8); value_type=data[p+15]; value=u32(p+16)
                    if name not in ('package','versionName','versionCode','minSdkVersion'): continue
                    decoded=strings[raw] if raw!=0xffffffff else (strings[value] if value_type==3 else value)
                    result[name]=int(decoded) if name in ('versionCode','minSdkVersion') else decoded
        cursor+=size
    if not all(k in result for k in ('package','versionName','versionCode')): raise ValueError('Incomplete APK metadata')
    return result
