import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by tolgacaner on 20/11/16.
 */
public class CustomMethodVisitor implements MethodVisitor {
    String methodName;
    public AnnotationVisitor visitAnnotationDefault() {
        return null;
    }

    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return null;
    }

    public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
        return null;
    }

    public void visitAttribute(Attribute attribute) {

    }

    public void visitCode() {

    }

    public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {

    }

    public void visitInsn(int i) {

    }

    public void visitIntInsn(int i, int i1) {

    }

    public void visitVarInsn(int i, int i1) {

    }

    public void visitTypeInsn(int i, String s) {

    }

    public void visitFieldInsn(int i, String s, String s1, String s2) {

    }

    public void visitMethodInsn(int i, String s, String s1, String s2) {

    }

    public void visitJumpInsn(int i, Label label) {

    }

    public void visitLabel(Label label) {

    }

    public void visitLdcInsn(Object o) {

    }

    public void visitIincInsn(int i, int i1) {
        if (i == 0) {
            System.out.println(methodName + " incrementor is incremented by:" + i1);
        }
    }

    public void visitTableSwitchInsn(int i, int i1, Label label, Label[] labels) {
    }

    public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {
    }

    public void visitMultiANewArrayInsn(String s, int i) {
    }

    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
    }

    public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i) {
    }

    public void visitLineNumber(int i, Label label) {
    }

    public void visitMaxs(int i, int i1) {
    }

    public void visitEnd() {
    }
}
