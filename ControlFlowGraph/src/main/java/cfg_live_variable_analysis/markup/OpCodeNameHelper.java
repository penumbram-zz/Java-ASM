package cfg_live_variable_analysis.markup;

import cfg_live_variable_analysis.CFG;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;

/**
 * Created by tolgacaner on 08/11/16.
 */
public class OpCodeNameHelper {
    public static String[] sOpcodeNames = new String[255];

    public static void fillOpcodes() {
        try {
            Field[] fields = Opcodes.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == int.class) {
                    int val = field.getInt(null);
                    if (val >= 0 && val < sOpcodeNames.length) {
                        sOpcodeNames[val] = field.getName();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
