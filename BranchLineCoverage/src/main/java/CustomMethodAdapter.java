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
    private HashSet<Integer> lines = new HashSet<Integer>();
    private String methodName = "";
    private HashSet<Integer> branchLines = new HashSet<Integer>();
    private boolean isNextBranchStart = false;

    public CustomMethodAdapter(MethodVisitor methodVisitor,String methodName) {
        super(methodVisitor);
        this.methodName = methodName;
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        super.visitLineNumber(i, label);
        super.visitLdcInsn(i);
        super.visitMethodInsn(Opcodes.INVOKESTATIC,
                "Main",
                "line",
                "(I)V");
        lines.add(i);
    }

    @Override
    public void visitJumpInsn(int i, Label label) {
        super.visitJumpInsn(i, label);
        if (i == Opcodes.IF_ICMPGE || i == Opcodes.IF_ICMPNE || i == Opcodes.IF_ICMPLE || i == Opcodes.IFLE) {
            LabelNode labelNode = (LabelNode)label.info;
            LineNumberNode lineNumberNode = (LineNumberNode) labelNode.getNext();
            //System.out.println(lineNumberNode.line);
            branchLines.add(lineNumberNode.line);
            isNextBranchStart = true;
        }
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        if (isNextBranchStart) {
            LabelNode labelNode = (LabelNode)label.info;
            LineNumberNode lineNumberNode = (LineNumberNode) labelNode.getNext();
            //System.out.println(lineNumberNode.line);
            branchLines.add(lineNumberNode.line);
            isNextBranchStart = false;
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        Main.setupMethod(methodName,lines);
        Main.addJumpInstructions(methodName,branchLines);
    }


    @Override
    public void visitIincInsn(int var1, int var2) {
        super.visitIincInsn(var1, 100);
                /*
                if (var1 == 0) {
                    visitedLines++;
                }
                */
    }
}
