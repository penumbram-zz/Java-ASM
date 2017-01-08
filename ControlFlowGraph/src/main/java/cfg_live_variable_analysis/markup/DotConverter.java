package cfg_live_variable_analysis.markup;

import cfg_live_variable_analysis.CFG;
import cfg_live_variable_analysis.Main;
import cfg_live_variable_analysis.Node;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.HashSet;

/**
 * Created by tolgacaner on 07/11/16.
 */
public class DotConverter {

    private static LineBinder lineBinder;

    public static String convert(String methodName, CFG cfg) {

        lineBinder = new LineBinder();
        lineBinder.bindLinesOf("/Users/tolgacaner/Documents/workspace/ControlFlowGraph/src/main/java/TestData.java");

        String result = "digraph " + methodName + " { \n\"begin\"";

        // SourceCodeAsString Initial Variables for every method
        Node.isFirst = true;
        Node.isPreviousNodeJumpNode = false;
        Node.currentLineNumber = -1;
        Node.previousLineNumber = -1;

        AbstractInsnNode currentNode;
        currentNode = cfg.getmNodeMap().keySet().iterator().next();
        // Find the root node of hw_one.Graph hw_one.Node Map of the method
        while (currentNode.getPrevious() != null) {
            currentNode = currentNode.getPrevious();
        }

        //Start to add graph parameters from the root node
        while (currentNode != null) {
            Node tempNode = cfg.getmNodeMap().get(currentNode);

            if (tempNode != null) {
                 result += tempNode.toString(lineBinder.getLines());
            }
            currentNode = currentNode.getNext();
        }
        for (String s : Main.dotBoxes.values()) {
            Integer lineNumber = getLineNumber(s);
            result += "\" " + s + "\"" + " [label=\"" + s + " \n\nIn:" + getVariables(Main.inVariables.get(String.valueOf(lineNumber)),cfg) + " \nOut:" + getVariables(Main.outVariables.get(String.valueOf(lineNumber)),cfg) + " \nDef:" + getVariables(Main.defVariables.get(String.valueOf(lineNumber)),cfg) + " \nUse:" + getVariables(Main.useVariables.get(String.valueOf(lineNumber)),cfg) +"\"];\n";
        }
        ////[label="hello world"]
        return result + "}";
    }

    private static Integer getLineNumber(String s) {
        for (Integer i : Main.dotBoxes.keySet()) {
            if (Main.dotBoxes.get(i).equalsIgnoreCase(s)){
                return i;
            }
        }
        return -1;
    }

    public static String getVariables(HashSet<Integer> set,CFG mGraph) {
        String result = "";
        if (set != null) {
            for (Integer i : set) {
                result += " " + Node.getLocalVariableNameWithIndex(i,mGraph);
            }
        }
        return result;
    }
}
