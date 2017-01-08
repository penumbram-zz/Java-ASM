import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by tolgacaner on 27/11/16.
 */
public class ArithmeticOperatorReplacer extends CustomMethodAdapter {

    public ArithmeticOperatorReplacer(MethodVisitor methodVisitor, String methodName) {
        super(methodVisitor, methodName);
    }

    //turns '*' into '/'
    @Override
    public void visitInsn(int i) {
        if (i == Opcodes.IMUL)
            i = Opcodes.IDIV;
        else if (i == Opcodes.IDIV)
            i = Opcodes.IMUL;
        super.visitInsn(i);
    }


    //turns '+' into '-'
    @Override
    public void visitIincInsn(int var1, int var2) {
        super.visitIincInsn(var1, -var2);
    }
}
