package cfg_live_variable_analysis;

import org.objectweb.asm.tree.*;

import java.util.*;

/**
 * Created by tolgacaner on 07/11/16.
 */
public class Node {
    public CFG graph;
    public AbstractInsnNode instruction;
    public final List<Node> successors = new ArrayList<Node>(2);
    public Node(AbstractInsnNode instruction) {
        this.instruction = instruction;
    }
    public List<AbstractInsnNode> lineOperations;

    public String line = "";
    public Node pLabelNode = null;

    public static int currentLineNumber;
    public static int previousLineNumber;
    public static boolean isFirst = true;
    public static boolean isPreviousNodeJumpNode = false;

    private String getInOutScore() {
        if (true)
            return "";
        String result = "";
        HashSet<Integer> inSet = Main.inVariables.get(this.line);
        if (inSet == null)
            inSet = new HashSet<Integer>();

        if (pLabelNode != null) {
            if (pLabelNode.lineOperations != null) {
                for (AbstractInsnNode n : pLabelNode.lineOperations) {
                    Node m = graph.getmNodeMap().get(n);
                    if (m != null) {
                        if (Main.inVariables.containsKey(m.line)) {
                            HashSet<Integer> inMy = Main.inVariables.get(m.line);
                            inSet.addAll(inMy);
                        }
                    } else
                        ;
                }
            }
        }


        if (inSet.size() > 0) {
            result += " In:";
            for (Integer i : inSet) {
                ;
                result += " " + Node.getLocalVariableNameWithIndex(i,graph);
            }
            result += " - ";
        }

        HashSet<Integer> outSet = Main.outVariables.get(this.line);

        if (outSet == null)
            outSet = new HashSet<Integer>();

        if (pLabelNode != null) {
            if (pLabelNode.lineOperations != null) {
                for (AbstractInsnNode n : pLabelNode.lineOperations) {
                    Node m = graph.getmNodeMap().get(n);
                    if (m != null) {
                        if (Main.outVariables.containsKey(m.line)) {
                            HashSet<Integer> inMy = Main.outVariables.get(m.line);
                            outSet.addAll(inMy);
                        }
                    } else
                        ;
                }
            }
        }

        if (outSet.size() > 0) {
            result += "Out:";
            for (Integer i : outSet) {
                result += " " + Node.getLocalVariableNameWithIndex(i,graph);
            }
        }
        return result;
    }

    public String toString(String [] testDataLines) {
        String result = "";

        if(instruction instanceof LineNumberNode) {
            currentLineNumber = ((LineNumberNode)instruction).line;
            if(!isPreviousNodeJumpNode){
                if(isFirst) {
                    result = result.concat("->\"" + " " + testDataLines[currentLineNumber] + "\"\n");
                    isFirst = false;

                }else {
                    Main.dotBoxes.put(currentLineNumber,testDataLines[currentLineNumber]);
                    Main.dotBoxes.put(previousLineNumber,testDataLines[previousLineNumber]);
                    result = result.concat("\"" + " " + testDataLines[previousLineNumber] + "\"");
                    result = result.concat("->\"" + " " + testDataLines[currentLineNumber] + "\";\n");
                    isFirst = false;
                }
            }
            isPreviousNodeJumpNode = false;
            previousLineNumber = currentLineNumber;
        }
        if(instruction instanceof JumpInsnNode) {

            isPreviousNodeJumpNode = true;

            Iterator<Node> iter = successors.iterator();
            while(iter.hasNext()){
                Node tmpNode = iter.next();
                if (tmpNode.instruction.getNext() instanceof LineNumberNode){
                    LineNumberNode lnn = (LineNumberNode)(tmpNode.instruction.getNext());
                    if(lnn.line != previousLineNumber){
                        Main.dotBoxes.put(previousLineNumber,testDataLines[previousLineNumber]);
                        Main.dotBoxes.put(lnn.line,testDataLines[lnn.line]);
                        result = result.concat("\"" + " " + testDataLines[previousLineNumber] + "\"");
                        result = result.concat("->\"" + " " + testDataLines[lnn.line] + "\";\n");
                    }
                }
            }
            isFirst = false;
        }
        return result;
    }

    public static String getLocalVariableNameWithIndex(int i, CFG graph) {
        for (Object object : graph.getmMethodNode().localVariables) {
            if (object instanceof LocalVariableNode) {
                LocalVariableNode localVariableNode = (LocalVariableNode) object;
                if (localVariableNode.index == i)
                    return localVariableNode.name;
            }
        }
        return null;
    }

    public HashSet<String> getSuccessorLines() {
        List<Node> succs = (List<Node>) ((ArrayList)this.successors).clone();
        HashSet<String> result = new HashSet<String>();

        boolean hasIf = false;
        if (this.lineOperations != null) {
            for (AbstractInsnNode insnNode : this.lineOperations) {
                if (insnNode instanceof JumpInsnNode) {
                    hasIf = true;
                }
            }
        } else if (this.instruction instanceof JumpInsnNode) {
            hasIf = true;
        }

        while (!succs.isEmpty()) {
            Node n = succs.remove(0);
            if (n.line != this.line)
                result.add(n.line);
            if (hasIf == false && result.size() == 1)
                break;
            else if (hasIf == true && result.size() == 2)
                break;
            if (succs.isEmpty()) {
                succs = (List<Node>) ((ArrayList) n.successors).clone();
            }
        }
        return result;
    }
}
