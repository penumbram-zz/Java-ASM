package cfg_live_variable_analysis;

import cfg_live_variable_analysis.markup.DotConverter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public class Main {

    private boolean isInsnof(int[] arr,int insn) {
        for (int i : arr) {
            if (insn == i)
                return true;
        }
        return false;
    }

    int[ ] load = { ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, IINC, RETURN };
    int[ ] store = { ISTORE, LSTORE, FSTORE, DSTORE, ASTORE };
    private CFG mGraph;

    public static HashMap<String,HashSet<Integer>> useVariables = new HashMap<String, HashSet<Integer>>();
    public static HashMap<String,HashSet<Integer>> defVariables = new HashMap<String, HashSet<Integer>>();
    public static HashMap<String,HashSet<Integer>> inVariables = new HashMap<String, HashSet<Integer>>();
    public static HashMap<String,HashSet<Integer>> outVariables = new HashMap<String, HashSet<Integer>>();
    public static HashMap<Integer,String> dotBoxes = new HashMap<Integer, String>();
    ArrayList<Node> allNodes;

    public static void main(String[] args) {

        ClassReader classReader = null;
        try {
            classReader = new ClassReader("TestData");
        } catch (IOException e) {
            e.printStackTrace();
            assert true;
        }
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        List<MethodNode> methods = classNode.methods;
        for(MethodNode currentMethod : methods){
            if (currentMethod.name.contains("<init>")){
                continue;
            }
            try {
                CFG graph = CFG.construct(classNode, currentMethod);
                //System.out.println(dotRep);
                Main two = new Main();
                two.liveVariableAnalysis(graph);
                String dotRep = DotConverter.convert(currentMethod.name,graph);
                PrintWriter out = new PrintWriter("output/" + currentMethod.name + ".dot");
                out.println(dotRep);
                out.close();
            } catch (AnalyzerException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addNodeToDefOrUse(Node n,HashMap<String,HashSet<Integer>> map,int var) {
        if (map.get(n.line) != null) {
            HashSet<Integer> sset = map.get(n.line);
            sset.add(var);
        } else {
            HashSet<Integer> sset = new HashSet<Integer>();
            sset.add(var);
            map.put(n.line,sset);
        }
    }

    public void liveVariableAnalysis(CFG graph) throws Exception {
        this.mGraph = graph;
        useVariables.clear();
        defVariables.clear();
        inVariables.clear();
        outVariables.clear();
        dotBoxes.clear();

        ArrayList<Node> allOfTheNodes = new ArrayList<Node>(graph.getmNodeMap().values());
        allNodes = (ArrayList<Node>) allOfTheNodes.clone();
        ArrayList<Node> worklist = new ArrayList<Node>();
        //Add Label Nodes their instructions
        Integer previousLineNumber = -1;
        for (Node n : allOfTheNodes) {
            n.graph = graph;
            if (n.instruction instanceof LabelNode) {
                ArrayList<AbstractInsnNode> operations = new ArrayList<AbstractInsnNode>();
                AbstractInsnNode nNext = n.instruction.getNext();
                while (nNext != null && (nNext instanceof LabelNode) == false) {
                    operations.add(nNext);
                    nNext = nNext.getNext();
                }
                n.lineOperations = operations;
                int numberOfLineNumberNodes = 0;
                for (AbstractInsnNode abstractInsnNode : n.lineOperations) {
                    if (abstractInsnNode instanceof LineNumberNode) {
                        numberOfLineNumberNodes++;
                        LineNumberNode lineNumberNode = (LineNumberNode) abstractInsnNode;
                        n.line = String.valueOf(lineNumberNode.line);
                        Node tempNode = getNodeByAbstractInstruction(abstractInsnNode);
                        tempNode.pLabelNode = n;
                        for (AbstractInsnNode abstractInsnNode1 : n.lineOperations) {
                            Node aNode = getNodeByAbstractInstruction(abstractInsnNode1);
                            aNode.line = n.line;
                        }
                        previousLineNumber = Integer.valueOf(n.line);
                    }
                    if (numberOfLineNumberNodes > 1) {
                        log("ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR");
                    }
                }

                if (numberOfLineNumberNodes == 0) { //for thingie
                    n.line = String.valueOf(previousLineNumber);
                    for (AbstractInsnNode abstractInsnNode1 : n.lineOperations) {
                        Node aNode = getNodeByAbstractInstruction(abstractInsnNode1);
                        aNode.line = n.line;
                    }
                }

                worklist.add(n);
            }
        }
        //Add Label Nodes their instructions



        for (Node n : allOfTheNodes) {
            if (n.instruction instanceof LabelNode) { //a line
                for (AbstractInsnNode abstractInsnNode : n.lineOperations) { //operations in a line
                    if (abstractInsnNode instanceof VarInsnNode) { //variable instruction
                        VarInsnNode varInsnNode = (VarInsnNode) abstractInsnNode;
                        if (isInsnof(store,varInsnNode.getOpcode())) {
                            addNodeToDefOrUse(n,defVariables,varInsnNode.var);
                        } else if (isInsnof(load,varInsnNode.getOpcode())) {
                            addNodeToDefOrUse(n,useVariables,varInsnNode.var);
                        }
                    }
                }
            }
        }


        Collections.sort(worklist, new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                return Integer.valueOf(o2.line).compareTo(Integer.valueOf(o1.line));
            }
        });

        //log("'Initialize' phase starting:");
        inVariables.put(worklist.get(worklist.size()-1).line,new HashSet<Integer>());
        for (Node n : worklist) {
            inVariables.put(n.line,new HashSet<Integer>());
            outVariables.put(n.line,new HashSet<Integer>());
        }

        //log("'Initialize' phase ended");
        //log("'iterate' phase starting:");
        while (worklist.isEmpty() == false) {
            Node node  = worklist.remove(0); //line 1
            HashSet<Integer> union = new HashSet<Integer>();
            HashSet<String> succList = node.getSuccessorLines();
            List<String> succ = new ArrayList<String>(succList);
            while (!succ.isEmpty()) {
                String s = succ.remove(0);
                HashSet<Integer> tempSet = inVariables.get(s);
                if (tempSet != null) {
                    union.addAll(tempSet);
                }
            }
            outVariables.put(node.line,union);

            //line2
            //line3
            HashSet<Integer> oldin = (HashSet<Integer>) inVariables.get(node.line).clone();
            //line3
            //line4
            HashSet<Integer> union2 = new HashSet<Integer>();
            HashSet<Integer> aSet = useVariables.get(node.line);
            if (aSet != null)
                aSet = (HashSet<Integer>) aSet.clone();
            else
                aSet = new HashSet<Integer>();

            HashSet<Integer> set3 = outVariables.get(node.line);
            if (set3 == null)
                set3 = new HashSet<Integer>();
            else
                set3 = (HashSet<Integer>) set3.clone();

            HashSet<Integer> set4 = defVariables.get(node.line);
            if (set4 == null)
                set4 = new HashSet<Integer>();
            else
                set4 = (HashSet<Integer>) set4.clone();

            set3.removeAll(set4);
            aSet.addAll(set3);
            if (aSet.size() > 0)
                union2.addAll(aSet);
            inVariables.put(node.line,union2);
            //second
            //log("outin");

            if (!oldin.equals(union2)) {
                HashSet<Node> predecessors = this.getPredecessors(node);
                for (Iterator<Node> i = predecessors.iterator(); i.hasNext();) {
                    Node element = i.next();
                    Node lineNode = getNodeByAbstractInstruction(element.instruction);
                    worklist.add(lineNode);
                    i.remove();
                }
            }
        }
        HashSet<String> allLines = new HashSet<String>();
        for (String s: defVariables.keySet()) {
            allLines.add(s);
        }
        for (String s: useVariables.keySet()) {
            allLines.add(s);
        }
        ArrayList<String> allLinesArr = new ArrayList<String>(allLines);
        Collections.sort(allLinesArr, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });
        log("Method with name: " + graph.getmMethodNode().name);
        for (String  i : allLinesArr) {
            log("Line: " + i + " - " + "Use: " + getVariables(useVariables.get(i)) + " Def: " + getVariables(defVariables.get(i)) + " - Out: " + getVariables(outVariables.get(i)) + " - In: " + getVariables(inVariables.get(i)));
        }
        log("---");
    }

    private void log(String s) {
        System.out.println(s);
    }

    private HashSet<Node> getPredecessors(Node node) {
        HashSet<Node> predecessors = new HashSet<Node>();
        for (Node n : allNodes) {
            if (n.successors.contains(node)) {
                predecessors.add(n);
            }
        }
        return predecessors;
    }

    private Node getNodeByAbstractInstruction(AbstractInsnNode abstractInsnNode) {
        for (Node aNode : allNodes) {
            if (aNode.instruction.equals(abstractInsnNode))
                return aNode;
        }
        return null;
    }


    public String getVariables(HashSet<Integer> set) {
        String result = "";
        if (set != null) {
            for (Integer i : set) {
                result += Node.getLocalVariableNameWithIndex(i,mGraph);
            }
        }
        return result;
    }


}