import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by tolgacaner on 27/11/16.
 */
public class RelationalOperatorReplacer extends CustomMethodAdapter{

    public RelationalOperatorReplacer(MethodVisitor methodVisitor, String methodName) {
        super(methodVisitor, methodName);
    }


    //negates all == to !=, != to ==, < to >= and > to <=
    @Override
    public void visitJumpInsn(int i, Label label) {
        int mutated = i;
        switch (i) {
            case IFNE:
                mutated = IFEQ;
                break;
            case IFEQ:
                mutated = IFNE;
                break;
            case IFLT:
                mutated = IFGE;
                break;
            case IFGE:
                mutated = IFLT;
                break;
            case IFGT:
                mutated = IFLE;
                break;
            case IFLE:
                mutated = IFGT;
                break;
            case IF_ICMPEQ:
                mutated = IF_ICMPNE;
                break;
            case IF_ICMPNE:
                mutated = IF_ICMPEQ;
                break;
            case IF_ICMPLT:
                mutated = IF_ICMPGE;
                break;
            case IF_ICMPGE:
                mutated = IF_ICMPLT;
                break;
            case IF_ICMPGT:
                mutated = IF_ICMPLE;
                break;
            case IF_ICMPLE:
                mutated = IF_ICMPGT;
                break;
            case IF_ACMPEQ:
                mutated = IF_ACMPNE;
                break;
            case IF_ACMPNE:
                mutated = IF_ACMPEQ;
                break;
            default:
                break;
        }
        super.visitJumpInsn(mutated, label);
    }
}
