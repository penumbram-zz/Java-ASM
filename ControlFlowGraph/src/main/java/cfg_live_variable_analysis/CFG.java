package cfg_live_variable_analysis;

import cfg_live_variable_analysis.markup.OpCodeNameHelper;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by tolgacaner on 07/11/16.
 */
public class CFG {
    private HashMap<AbstractInsnNode, Node> mNodeMap;
    public HashMap<AbstractInsnNode, Node> getmNodeMap() {
        return mNodeMap;
    }
    private MethodNode mMethodNode;
    public MethodNode getmMethodNode() {
        return mMethodNode;
    }

    public static CFG construct(ClassNode classNode, MethodNode currentMethod) throws AnalyzerException, IOException {

        final CFG graph = new CFG();
        final InsnList instructions = currentMethod.instructions;

        graph.mNodeMap = new HashMap<AbstractInsnNode, Node>(instructions.size());
        graph.mMethodNode = currentMethod;
        OpCodeNameHelper.fillOpcodes();

        Analyzer analyzer = new Analyzer(new BasicInterpreter()) {
            @Override
            protected void newControlFlowEdge(int insn, int successor) {
                AbstractInsnNode source = instructions.get(insn);
                AbstractInsnNode destination = instructions.get(successor);
                graph.add(source, destination);
                super.newControlFlowEdge(insn,successor);
            }
        };
        analyzer.analyze(classNode.name, currentMethod);
        return graph;
    }

    protected void add(AbstractInsnNode source, AbstractInsnNode destination) {
        Node nSource = mNodeMap.get(source);
        if (nSource == null) { //if non-existant, create new source
            nSource = new Node(source);
            mNodeMap.put(source, nSource);
        }

        Node nDest = mNodeMap.get(destination);
        if (nDest == null) { //if non-existant, create new destination
            nDest = new Node(destination);
            mNodeMap.put(destination, nDest);
        }

        if (!nSource.successors.contains(nDest)) {
            nSource.successors.add(nDest);
        }
    }


}