import java.util.*;
import java.util.zip.*;
import com.android.tools.smali.dexlib2.dexbacked.*;
import com.android.tools.smali.dexlib2.iface.*;
import com.android.tools.smali.dexlib2.iface.instruction.*;
import com.android.tools.smali.dexlib2.iface.reference.*;

@SuppressWarnings({"unchecked","rawtypes"})
public class VerifyFeature170Dex {
  record Info(String parent,List<String> interfaces,Set<String> methods,Set<String> fields){}
  static Map<String,Info> classes=new HashMap<>();
  static String methodKey(MethodReference m){return m.getName()+"("+String.join("",m.getParameterTypes())+")"+m.getReturnType();}
  static boolean resolve(String owner,String key,boolean field,Set<String> seen){
    if(!seen.add(owner))return false;Info c=classes.get(owner);if(c==null)return false;
    if((field?c.fields:c.methods).contains(key))return true;
    if(c.parent!=null&&resolve(c.parent,key,field,seen))return true;
    for(String face:c.interfaces)if(resolve(face,key,field,seen))return true;return false;
  }
  static boolean externalParent(String owner,Set<String> seen){
    if(!seen.add(owner))return false;Info c=classes.get(owner);if(c==null)return !owner.equals("Ljava/lang/Object;");
    if(c.parent!=null&&externalParent(c.parent,seen))return true;
    for(String face:c.interfaces)if(externalParent(face,seen))return true;return false;
  }
  static List<Method> methods(ClassDef cls){List<Method> out=new ArrayList<>();for(Object x:cls.getDirectMethods())out.add((Method)x);for(Object x:cls.getVirtualMethods())out.add((Method)x);return out;}
  public static void main(String[] args)throws Exception{
    List<ClassDef> app=new ArrayList<>();List<DexBackedDexFile> dexes=new ArrayList<>();
    try(ZipFile z=new ZipFile(args[0])){
      for(String name:List.of("classes.dex","classes2.dex","classes3.dex","classes4.dex")){
        var dex=new DexBackedDexFile(z.getInputStream(z.getEntry(name)).readAllBytes(),0);dexes.add(dex);
        for(int i=0;i<dex.classCount;i++){
          ClassDef cls=(ClassDef)dex.classSection.get(i);Set<String> ms=new HashSet<>(),fs=new HashSet<>();
          for(Method m:methods(cls))ms.add(methodKey(m));
          for(Object x:cls.getStaticFields()){Field f=(Field)x;fs.add(f.getName()+":"+f.getType());}
          for(Object x:cls.getInstanceFields()){Field f=(Field)x;fs.add(f.getName()+":"+f.getType());}
          if(classes.put(cls.getType(),new Info(cls.getSuperclass(),new ArrayList<>(cls.getInterfaces()),ms,fs))!=null)throw new AssertionError("Duplicate class "+cls.getType());
          if(cls.getType().startsWith("Lsing/tona/"))app.add(cls);
        }
      }
    }
    Set<String> errors=new TreeSet<>();int refs=0;boolean paletteBranch=false,logoImage=false,launchHook=false,downloadHook=false,feedHook=false;
    for(ClassDef cls:app)for(Method method:methods(cls)){
      if(method.getImplementation()==null)continue;boolean logo=false;Set<Integer> zero=new HashSet<>();
      for(Object x:method.getImplementation().getInstructions()){
        Instruction op=(Instruction)x;
        if(op instanceof OneRegisterInstruction a){
          String opcode=op.getOpcode().name();
          if(op instanceof NarrowLiteralInstruction literal && opcode.startsWith("CONST")){
            if(literal.getNarrowLiteral()==0)zero.add(a.getRegisterA());else zero.remove(a.getRegisterA());
          }else if(op instanceof TwoRegisterInstruction b && opcode.startsWith("MOVE")){
            if(zero.contains(b.getRegisterB()))zero.add(a.getRegisterA());else zero.remove(a.getRegisterA());
          }else if(opcode.startsWith("MOVE_RESULT")||opcode.startsWith("SGET")||opcode.startsWith("IGET")||opcode.startsWith("NEW_INSTANCE"))zero.remove(a.getRegisterA());
        }
        if(!(x instanceof ReferenceInstruction ins))continue;Object ref=ins.getReference();
        if(ref instanceof FieldReference f){
          if(f.getName().equals("singtonado_mark"))logo=true;
          if(classes.containsKey(f.getDefiningClass())&&!resolve(f.getDefiningClass(),f.getName()+":"+f.getType(),true,new HashSet<>())&&!externalParent(f.getDefiningClass(),new HashSet<>()))errors.add("FIELD "+f);
        }
        if(!(ref instanceof MethodReference m))continue;refs++;
        if(logo&&m.getName().equals("getPaletteId"))paletteBranch=true;
        if(logo&&m.getDefiningClass().equals("Landroidx/compose/foundation/ImageKt;")&&m.getName().equals("Image")){
          if(!(x instanceof RegisterRangeInstruction range)||!zero.contains(range.getStartRegister()+6))errors.add("Logo Image does not receive a null color filter");
          logoImage=true;logo=false;
        }
        if(cls.getType().equals("Lsing/tona/MainActivity;")&&m.getDefiningClass().equals("Lsing/tona/LaunchIntro;")&&m.getName().equals("show"))launchHook=true;
        if(cls.getType().equals("Lsing/tona/DownloadAction;")&&m.getDefiningClass().equals("Lsing/tona/LibraryDownloads;"))downloadHook=true;
        if(cls.getType().equals("Lsing/tona/download/AssetDownloads;")&&m.getDefiningClass().equals("Lsing/tona/FeedConfig;"))feedHook=true;
        if(!classes.containsKey(m.getDefiningClass()))continue;
        String key=methodKey(m);
        if(!resolve(m.getDefiningClass(),key,false,new HashSet<>())&&!externalParent(m.getDefiningClass(),new HashSet<>())&&!Set.of("getClass","equals","hashCode","toString","notify","notifyAll","wait","clone","finalize").contains(m.getName()))errors.add("METHOD "+m);
      }
    }
    for(String type:List.of("record/SongRecorder","record/Recording","ui/recordings/RecordingsScreenKt","device/StageCamera","ui/perform/CameraBackdropKt","remote/BluetoothStage","remote/BluetoothRemote","audio/CountInPlayer","settings/LyricFont","settings/ScoreMode","settings/SettingsKt"))if(!classes.containsKey("Lsing/tona/"+type+";"))errors.add("MISSING FEATURE "+type);
    if(!launchHook||!downloadHook||!feedHook||!logoImage||paletteBranch)errors.add("Custom hooks/logo failure: "+launchHook+" "+downloadHook+" "+feedHook+" "+logoImage+" "+paletteBranch);
    if(!errors.isEmpty()){errors.forEach(System.out::println);throw new AssertionError(errors.size()+" unresolved references / feature errors");}
    System.out.println("PASS: "+app.size()+" SingTonado classes and "+refs+" method references; new feature classes and preserved startup/download/feed/logo hooks verified.");
  }
}
