import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.HashSet;

/**
 * Created by tolgacaner on 22/11/16.
 */
public class CustomMethodAdapter extends MethodAdapter {
    protected String methodName = "";

    public CustomMethodAdapter(MethodVisitor methodVisitor,String methodName) {
        super(methodVisitor);
        this.methodName = methodName;
    }
}
