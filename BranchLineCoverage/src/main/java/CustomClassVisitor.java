import org.objectweb.asm.*;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by tolgacaner on 20/11/16.
 */
public class CustomClassVisitor implements ClassVisitor {

    public String currentMethodName;

    public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {

    }

    public void visitSource(String s, String s1) {

    }

    public void visitOuterClass(String s, String s1, String s2) {

    }

    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return null;
    }

    public void visitAttribute(Attribute attribute) {

    }

    public void visitInnerClass(String s, String s1, String s2, int i) {

    }

    public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
        return null;
    }

    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
        CustomMethodVisitor customMethodVisitor = new CustomMethodVisitor();
        currentMethodName = s;
        customMethodVisitor.methodName = s;
        return  customMethodVisitor;
    }

    public void visitEnd() {

    }
}
