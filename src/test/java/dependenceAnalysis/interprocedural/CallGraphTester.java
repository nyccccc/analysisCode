package dependenceAnalysis.interprocedural;

import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;

public class CallGraphTester {

    @Test
    public void callGraphTester(){
        CallGraph cg = new CallGraph("target/classes");
        System.out.println(cg.getCallGraph());
    }

    /*@Test
    public void clientsForClassTest(){
        CallGraph cg = new CallGraph("/Users/neil/Google Drive/Teaching/Sheffield/Reengineering/2019-20/Examples/weka-3.8/weka/build/classes");
        Collection<ClassNode> clients = cg.getPredecessorsForClass(cg.classNodes.get("weka/classifiers/evaluation/Evaluation"));
        for(ClassNode client : clients){
            System.out.println(client.name);
        }
    }

    @Test
    public void successorsForClassTest(){
        CallGraph cg = new CallGraph("/Users/neil/Google Drive/Teaching/Sheffield/Reengineering/2019-20/Examples/weka-3.8/weka/build/classes");
        Collection<ClassNode> clients = cg.getSuccessorsForClass(cg.classNodes.get("weka/classifiers/evaluation/Evaluation"));
        for(ClassNode client : clients){
            System.out.println(client.name);
        }
    }*/
}
