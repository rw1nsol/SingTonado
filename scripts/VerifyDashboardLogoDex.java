import java.util.*;
import java.util.zip.*;
import com.android.tools.smali.dexlib2.dexbacked.*;
import com.android.tools.smali.dexlib2.iface.*;
import com.android.tools.smali.dexlib2.iface.instruction.*;
import com.android.tools.smali.dexlib2.iface.reference.*;

public class VerifyDashboardLogoDex {
  public static void main(String[] args)throws Exception {
    byte[] bytes;try(ZipFile z=new ZipFile(args[0])){bytes=z.getInputStream(z.getEntry("classes3.dex")).readAllBytes();}
    var dex=new DexBackedDexFile(bytes,0);int images=0;
    for(int index=0;index<dex.classCount;index++){
      ClassDef cls=(ClassDef)dex.classSection.get(index);
      if(!cls.getType().equals("Lsing/tona/ui/library/LibraryScreenKt;"))continue;
      List<Method> methods=new ArrayList<>();cls.getDirectMethods().forEach(m->methods.add((Method)m));cls.getVirtualMethods().forEach(m->methods.add((Method)m));
      for(Method method:methods){if(method.getImplementation()==null)continue;boolean logo=false,nullFilter=false;
        for(Object value:method.getImplementation().getInstructions()){
          Instruction ins=(Instruction)value;
          if(ins instanceof ReferenceInstruction ri && ri.getReference() instanceof FieldReference f && f.getName().equals("singtonado_mark")){logo=true;nullFilter=false;}
          if(!logo)continue;
          if(ins instanceof OneRegisterInstruction reg && reg.getRegisterA()==22 && ins instanceof NarrowLiteralInstruction lit && lit.getNarrowLiteral()==0)nullFilter=true;
          if(ins instanceof ReferenceInstruction ri && ri.getReference() instanceof MethodReference m){
            if(m.getName().equals("getPaletteId")||m.getDefiningClass().contains("ColorFilter"))throw new AssertionError("Logo still depends on palette tint: "+m);
            if(m.getDefiningClass().equals("Landroidx/compose/foundation/ImageKt;")&&m.getName().equals("Image")){
              if(!nullFilter)throw new AssertionError("Dashboard Image must receive a null color filter");images++;logo=false;
            }
          }
        }
      }
    }
    if(images!=1)throw new AssertionError("Expected one verified dashboard logo, got "+images);
    System.out.println("PASS: compiled dashboard logo passes a null color filter with no palette branch; green, blue, purple, rose and charcoal use the same full-color image.");
  }
}
