import java.util.*;
import java.util.zip.*;
import com.android.tools.smali.dexlib2.dexbacked.*;
import com.android.tools.smali.dexlib2.iface.*;
import com.android.tools.smali.dexlib2.iface.instruction.*;
import com.android.tools.smali.dexlib2.iface.reference.*;

/** Checks the compiled image-opacity regression, circular clipping and motion. */
public class VerifyStartupDex {
    public static void main(String[] args) throws Exception {
        byte[] bytes;
        try (ZipFile zip = new ZipFile(args[0])) {
            bytes = zip.getInputStream(zip.getEntry("classes4.dex")).readAllBytes();
        }
        var dex = new DexBackedDexFile(bytes, 0);
        Set<String> calls = new HashSet<>();
        int floatAlpha = 0;
        for (int index=0; index<dex.classCount; index++) {
            ClassDef cls=(ClassDef)dex.classSection.get(index);
            if (!cls.getType().startsWith("Lsing/tona/LaunchIntro")) continue;
            List<Method> methods = new ArrayList<>();
            for (Object item : cls.getDirectMethods()) methods.add((Method)item);
            for (Object item : cls.getVirtualMethods()) methods.add((Method)item);
            for (Method method : methods) {
                if (method.getImplementation()==null) continue;
                for (Object item : method.getImplementation().getInstructions()) {
                    if (!(item instanceof ReferenceInstruction)) continue;
                    Object reference=((ReferenceInstruction)item).getReference();
                    if (!(reference instanceof MethodReference)) continue;
                    MethodReference ref=(MethodReference)reference;
                    calls.add(ref.getName());
                    if (!ref.getName().equals("setAlpha")) continue;
                    if (ref.getParameterTypes().size()==1 && ref.getParameterTypes().get(0).toString().equals("I"))
                        throw new AssertionError("ImageView integer setAlpha hides the bitmap independently of the View fade animation");
                    floatAlpha++;
                }
            }
        }
        if (floatAlpha<2) throw new AssertionError("Missing float opacity calls");
        for (String call : List.of("setImageAlpha","setClipToOutline","setShape","floatMotion","setRepeatMode"))
            if (!calls.contains(call)) throw new AssertionError("Missing circular image/motion call: "+call);
        System.out.println("PASS: compiled startup code uses float opacity, explicit opaque bitmap, circular clipping and repeating text motion.");
    }
}
