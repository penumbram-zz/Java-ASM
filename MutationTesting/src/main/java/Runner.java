import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import org.junit.runner.JUnitCore;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by tolgacaner on 27/11/16.
 */
public class Runner {
    static HashMap<String,HashMap<Integer,ArrayList<Integer>>> conditionalOperatorsByLine = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();

    public static void main(String[] args) throws IOException {

        JUnitCore jUnitCore = new JUnitCore();
//        jUnitCore.run(Method1Test.class);
//        jUnitCore.run(Method2Test.class);
//        jUnitCore.run(Method3Test.class);
//        jUnitCore.run(Method4Test.class);
        ClassNode classNode = new ClassNode();
        ClassReader cr = new ClassReader("Main");
        cr.accept(classNode, 0);
        ClassWriter out = new CustomClassWriter();
        classNode.accept(out);

    //    changeClassNode(classNode);

    //    ClassWriter out2 = new ClassWriter(0);
    //    classNode.accept(out2);
        output("Main2.class",out.toByteArray());
        loadClass(out.toByteArray(), "Main");
//        jUnitCore.run(Method1Test.class);
        jUnitCore.run(Method1Test.class);
        jUnitCore.run(Method2Test.class);
        jUnitCore.run(Method3Test.class);
        jUnitCore.run(Method4Test.class);
        //	new TestRunnerz();

    }

    public static void output(String filename, byte[] data) throws IOException {
        FileOutputStream out = new FileOutputStream("/Users/tolgacaner/Documents/workspace/MutationTesting/src/main/resources/" + filename);
        out.write(data);
        out.close();
    }

    public static void setAbc(HashMap<Integer,ArrayList<Integer>> abc,String methodName) {
        conditionalOperatorsByLine.put(methodName,abc);
    }

    public static Class loadClass(byte[] b, String name) {
        //override classDefine (as it is protected) and define the class.
        Class clazz = null;
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class cls = Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method =
                    cls.getDeclaredMethod("defineClass", new Class[]{String.class, byte[].class, int.class, int.class});

            // protected method invocaton
            method.setAccessible(true);
            try {
                Object[] args = new Object[]{name, b, new Integer(0), new Integer(b.length)};
                clazz = (Class) method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return clazz;
    }

    private static void changeClassNode(ClassNode classNode) {
        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        for (MethodNode method : methods) {
            if (method.name.equalsIgnoreCase("<init>"))
                continue;
            if (method.name.equalsIgnoreCase("Method1") || method.name.equalsIgnoreCase("Method2") || method.name.equalsIgnoreCase("Method3"))
                continue;
            HashMap<Integer,ArrayList<Integer>> temp = conditionalOperatorsByLine.get(method.name);
            if (temp != null) {
                boolean change = false;
                for (Integer key : temp.keySet()) {
                    ArrayList<Integer> arrayList = temp.get(key);
                    if (arrayList.size() == 2) {
                        if (arrayList.get(0) == Opcodes.IFEQ && arrayList.get(1) == Opcodes.IFEQ) { //&&
                            InsnList insnList = method.instructions;
                            ListIterator ite = insnList.iterator();
                            while (ite.hasNext()) {
                                AbstractInsnNode insn = (AbstractInsnNode) ite.next();
                                if (change) {
                                    if (insn.getOpcode() == Opcodes.IFEQ) {

                                        AbstractInsnNode tempo = insn;
                                        int count = 0;
                                        while (tempo instanceof LabelNode == false) {
                                            tempo = tempo.getNext();
                                        }
                                        //jumpInsnNode.label = (LabelNode) tempo;

                                        ite.remove();
                                        JumpInsnNode jumpInsnNode = (JumpInsnNode)insn;
                                        LabelNode labelNode = (LabelNode) tempo;
                                        ite.add(new JumpInsnNode(Opcodes.IFNE,labelNode));
                                        change = false;
                                        continue;
                                    }
                                }
                                if (insn instanceof LineNumberNode) {
                                    LineNumberNode lineNumberNode = (LineNumberNode) insn;
                                    if (lineNumberNode.line == key.intValue()) {
                                        change = true;
                                        System.out.println("YES AND THEN WHAT?");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }//((MethodNode)classNode.methods.get(5)).instructions.get(13)
    }

}
