import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.ArrayList;
import java.util.HashMap;

import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;

/**
 * Created by tolgacaner on 27/11/16.
 */
public class ConditionalOperatorReplacer extends CustomMethodAdapter {
    boolean shouldChange = true;
    HashMap<Integer,ArrayList<Integer>> conditionalOperatorsByLine = new HashMap<Integer,ArrayList<Integer>>();
    int currentLine = 0;
    public ConditionalOperatorReplacer(MethodVisitor methodVisitor, String methodName) {
        super(methodVisitor, methodName);
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        super.visitLineNumber(i, label);
        currentLine = i;
        conditionalOperatorsByLine.put(i,new ArrayList<Integer>());
    }

    @Override
    public void visitJumpInsn(int i, Label label) {
        LabelNode temp = null;
        if (shouldChange) {
            if (i == IFEQ) {
                i = IFNE;

                LabelNode labelNode = (LabelNode)label.info;
                AbstractInsnNode abstractInsnNode = labelNode.getPrevious();
                while (abstractInsnNode != null) {
                    abstractInsnNode = abstractInsnNode.getPrevious();
                    if (abstractInsnNode instanceof LabelNode) {
                        if (abstractInsnNode.getNext() instanceof LineNumberNode) {
                            LineNumberNode lineNumberNode = (LineNumberNode) abstractInsnNode.getNext();
                            if (lineNumberNode.line == 35) {
                                temp = (LabelNode) abstractInsnNode;
                                break;
                            }
                        }
                    }
                }
                shouldChange = false;
            }
        }
        if (temp != null) {
            label = temp.getLabel();
        }
        super.visitJumpInsn(i, label);
        conditionalOperatorsByLine.get(currentLine).add(i);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        Runner.setAbc(conditionalOperatorsByLine,this.methodName);
    }
}
