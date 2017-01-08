import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.junit.runner.JUnitCore;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class Main {

    private static HashMap<String,HashSet<Integer> > data = new HashMap<String, HashSet<Integer> >();
    private static HashMap<String,HashSet<Integer> > branchData = new HashMap<String, HashSet<Integer> >();
    //static HashSet<Integer> branchBeginnings = new HashSet<Integer>();
    private static HashSet<Integer> currentTestedLines;
    private static HashSet<Integer> currentBranchLines;
    private static int visitedLines = 0;
    private static int visitedBranches = 0;
    private static int totalLines = 0;
    private static int totalBranches = 0;
    public static void main(String[] args) throws Exception {




        ClassNode classNode = new ClassNode();

        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        for (MethodNode method : methods) {
            if (method.name.equalsIgnoreCase("<init>"))
                continue;
            InsnList insnList = method.instructions;
//            InsnList firstList = new InsnList();
//            firstList.add(new InsnNode(ICONST_0));
//            firstList.add(new VarInsnNode(Opcodes.ISTORE,0));
//            insnList.insert(insnList.getFirst(),firstList);
//            method.maxStack += 2;
            ListIterator ite = insnList.iterator();
            boolean addIncrementNext = false;
            int noOfLabels = 0;
            while (ite.hasNext()) {
                AbstractInsnNode insn = (AbstractInsnNode) ite.next();

                if (addIncrementNext && noOfLabels > 1) {
                    addIncrementNext = false;
                    //    ite.add(new IincInsnNode(1,1));
                    InsnList tempList = new InsnList();
                    tempList.add(new IincInsnNode(0,1));
                    insnList.insert(insn.getPrevious(), tempList);
                    method.maxStack += 1;
                }


                if(insn instanceof LabelNode){
                    addIncrementNext = true;
                    noOfLabels++;
                }
/*
                if (insn.getOpcode() == RETURN) {
                    InsnList tempList = new InsnList();
                    tempList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    //tempList.add(new LdcInsnNode("Returning " + method.name));
                    tempList.add(new VarInsnNode(ILOAD,0));
                    tempList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V"));
                    insnList.insert(insn.getPrevious(), tempList);
                    method.maxStack += 2;

                }
                */
            }
        }

        ClassReader cr = new ClassReader("Calculator");
        cr.accept(classNode, 0);
        ClassWriter out = new CustomClassWriter();
        classNode.accept(out);
        output("Calculatorr.class",out.toByteArray());
        loadClass(out.toByteArray(), "Calculator");

        InputStream in = Main.class.getClassLoader().getResourceAsStream("Calculatorr.class");
        ClassReader classReader = new ClassReader(in);
        ClassVisitor classAnalyzer = new CustomClassVisitor();
        classReader.accept(classAnalyzer, 0);

        new TestRunnerz();

    }


    private static class TestRunnerz implements TestStateInterface {
        private int cTotalLines;
        private int cBranchLines;
        public TestRunnerz() {
            JUnitCore jUnitCore = new JUnitCore();
            jUnitCore.addListener(new CustomRunListener(this));
            jUnitCore.run(CalculatorTests.class);
        }

        public void testStarted(String functionName) {
            currentTestedLines = (HashSet<Integer>) data.get(getTestedMethodName(functionName)).clone();
            currentBranchLines = (HashSet<Integer>) branchData.get(getTestedMethodName(functionName)).clone();
            cTotalLines = currentTestedLines.size();
            cBranchLines = currentBranchLines.size();
        }

        public void testFinished(String functionName) {

            int y = currentTestedLines.size();
            int z = currentBranchLines.size();
            System.out.println("line coverage: " + (cTotalLines-y) + "/" +cTotalLines);
            System.out.println("branch coverage: " + (cBranchLines-z) + "/" +cBranchLines);
            visitedLines += cTotalLines-y;
            visitedBranches += cBranchLines-z;
        }

        public void allTestsFinished() {
            System.out.println("All tests are finished");
            System.out.println("Total line coverage: " + Main.getPercentage((double) visitedLines /(double) totalLines));
            System.out.println("Total branch coverage: " + (double) visitedBranches /(double) totalBranches);
        }

        private String getTestedMethodName(String str) {
            if (str.equalsIgnoreCase("testSub"))
                return "sub";
            else if (str.equalsIgnoreCase("testDivide"))
                return "div";
            else if (str.equalsIgnoreCase("testMult"))
                return "mult";
            else if (str.equalsIgnoreCase("complex"))
                return "other";
            else if (str.equalsIgnoreCase("uncoveredtest"))
                return "uncovered";
            else if (str.equalsIgnoreCase("testAdd"))
                return "add";
            else
                return null;
        }
    }

    public static void output(String filename, byte[] data) throws IOException {
        FileOutputStream out = new FileOutputStream("/Users/tolgacaner/Documents/workspace/BranchLineCoverage/src/main/resources/" + filename);
        out.write(data);
        out.close();
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

    public static void line(int x) {
//        System.out.println("line number: " + x);
        currentTestedLines.remove(x);
        currentBranchLines.remove(x);
    }

    public static void setupMethod(String methodName,HashSet<Integer> lines) {
        totalLines += lines.size();
        data.put(methodName,lines);
    }

    public static void addJumpInstructions(String methodName,HashSet<Integer> branches) {
        totalBranches += branches.size();
        branchData.put(methodName,branches);
    }

    public static double getPercentage(double value) {
        int places = 2;
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}