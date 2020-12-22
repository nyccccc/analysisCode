package structuralAnalysis;


import dependenceAnalysis.interprocedural.CallGraph;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import util.ASMClassReader;

import java.io.File;
import java.util.*;

/**
 * A class to calculate a few helpful metrics for planning a
 * reengineering effort.
 *
 */
public class ClassMetrics {

    // Map of inheritance tree, from class to set of sub-classes.
    protected Map<ClassNode,Set<ClassNode>> subclasses;

    // Map to link class names to corresponding ClassNode objects
    protected Map<String,ClassNode> classNodes;

    protected Map<ClassNode,Double> wmc;

    protected Map<MethodNode,Double> cyclomaticComplexity;

    protected Map<MethodNode,Double> methodFanIn;
    protected Map<MethodNode,Double> methodFanOut;

    protected Map<ClassNode,Double> classFanIn;
    protected Map<ClassNode,Double> classFanOut;




    /**
     * Constructor, taking as input root-directory (String) for project.
     * @param root
     */
    public ClassMetrics(String root){

        File dir = new File(root);

        classNodes = new HashMap<String,ClassNode>();

        ASMClassReader acr = new ASMClassReader();

        // Traverse root directory structure and obtain all classes.
        List<ClassNode> classes = acr.processDirectory(dir,"");

        cyclomaticComplexity = new HashMap<MethodNode, Double>();

        wmc = new HashMap<>();

        methodFanIn= new HashMap<>();
        methodFanOut = new HashMap<>();

        classFanIn = new HashMap<>();
        classFanOut = new HashMap<>();

        calculateMetrics(root, classes);
    }

    private void calculateMetrics(String root, List<ClassNode> classes) {
        CallGraph cg = new CallGraph(root);

        //calculate CC for all methods in all classes
        for(ClassNode cn : classes){
            if((cn.access & Opcodes.ACC_INTERFACE) !=0)
                continue;
            if(cn.outerClass!=null)
                continue;
            classNodes.put(cn.name,cn);
            Double currentWMC = 0D;
            Double cFanIn = 0D;
            Double cFanOut = 0D;
            for(MethodNode method : cn.methods){
                Double cc = calculateCC(method);
                cyclomaticComplexity.put(method,cc);
                currentWMC += cc;
                try {
                    Double mIn = (double) cg.incomingCalls(cn, method);
                    Double mOut = (double) cg.outgoingCalls(cn, method);
                    methodFanIn.put(method, mIn);
                    methodFanOut.put(method, mOut);
                    cFanIn += mIn;
                    cFanOut += mOut;
                }
                catch(Exception e){
                    //e.printStackTrace();
                }
            }
            wmc.put(cn,currentWMC);
            classFanIn.put(cn,cFanIn);
            classFanOut.put(cn,cFanOut);
        }
    }


    /**
     * Calculates McCabe's cyclomatic complexity for a method. Usually defined
     * as the number of edges in the control flow graph minus the number of edges + 2,
     * we "cheat" here, we skip the construction of the CFG and just add 1 to the number
     * of branches, which gives us the same result.
     *
     * @param md
     * @return
     */
    private Double calculateCC(MethodNode md){
        double branches = 0;
        for(AbstractInsnNode instruction : md.instructions){
            if (instruction.getType() == AbstractInsnNode.JUMP_INSN)
                branches++;
        }
        return branches + 1;
    }


    public String classMetricString(){
        String returnString = "Class, WMC, Fan In, Fan Out\n";
        for(ClassNode cn : wmc.keySet()){
            String cString = cn.name+","+wmc.get(cn)+","+classFanIn.get(cn)+","+classFanOut.get(cn)+"\n";
            returnString += cString;
        }
        return returnString;

    }


}
