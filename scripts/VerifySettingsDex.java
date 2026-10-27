import java.util.*;
import java.util.zip.*;
import com.android.tools.smali.dexlib2.dexbacked.*;
import com.android.tools.smali.dexlib2.iface.*;
import com.android.tools.smali.dexlib2.iface.instruction.*;
import com.android.tools.smali.dexlib2.iface.reference.*;

public class VerifySettingsDex {
    public static void main(String[] args) throws Exception {
        byte[] bytes;
        try (ZipFile zip = new ZipFile(args[0])) {
            bytes = zip.getInputStream(zip.getEntry("classes3.dex")).readAllBytes();
        }
        var dex = new DexBackedDexFile(bytes, 0);
        for (int index=0; index<dex.classCount; index++) {
            ClassDef cls=(ClassDef)dex.classSection.get(index);
            if (!cls.getType().equals("Lsing/tona/ui/library/SupportPanelKt;")) continue;
            for (Object methodItem : cls.getDirectMethods()) {
                Method method=(Method)methodItem;
                if (!method.getName().equals("SupportContent-uFdPcIQ")) continue;
                var impl = method.getImplementation();
                Map<Integer,String> types = new HashMap<>();
                int input = impl.getRegisterCount() - 6;
                types.put(input + 3, "Landroidx/compose/runtime/Composer;");
                int wideCount = 0;
                for (Object instructionItem : impl.getInstructions()) {
                    Instruction ins=(Instruction)instructionItem;
                    String op = ins.getOpcode().name();
                    if (op.startsWith("CONST_WIDE")) {
                        int r = ((OneRegisterInstruction) ins).getRegisterA();
                        types.put(r, "J"); types.put(r+1, "high");
                        wideCount++;
                    } else if (op.startsWith("CONST_STRING")) {
                        types.put(((OneRegisterInstruction)ins).getRegisterA(), "Ljava/lang/String;");
                    } else if (op.startsWith("CONST")) {
                        types.put(((OneRegisterInstruction)ins).getRegisterA(), "zero");
                    } else if (op.startsWith("MOVE_OBJECT")) {
                        var move = (TwoRegisterInstruction)ins;
                        types.put(move.getRegisterA(),types.get(move.getRegisterB()));
                    } else if (op.equals("INVOKE_STATIC_RANGE")) {
                        var ref = (MethodReference)((ReferenceInstruction)ins).getReference();
                        var range = (RegisterRangeInstruction)ins;
                        int r = range.getStartRegister();
                        for (var parameter : ref.getParameterTypes()) {
                            String expected=parameter.toString(), actual=types.get(r);
                            if (expected.equals("J") || expected.equals("D")) {
                                if (!expected.equals(actual) || !"high".equals(types.get(r+1)))
                                    throw new AssertionError("Invalid wide argument at v"+r+": "+actual);
                                r+=2;
                            } else {
                                if (actual==null || actual.equals("high") || actual.equals("J"))
                                    throw new AssertionError("Invalid single argument at v"+r);
                                if (expected.equals("Landroidx/compose/runtime/Composer;") && !expected.equals(actual))
                                    throw new AssertionError("Invalid Composer");
                                r++;
                            }
                        }
                        if (r-range.getStartRegister()!=range.getRegisterCount()) throw new AssertionError("Argument count");
                        if (wideCount!=4) throw new AssertionError("Expected four wide arguments");
                        System.out.println("PASS: compiled Settings/About Text call has four valid wide pairs, valid Composer and correct argument count.");
                        return;
                    }
                }
            }
        }
        throw new AssertionError("Settings support method not found");
    }
}


