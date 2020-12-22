package structuralAnalysis;

import dependenceAnalysis.interprocedural.CallGraph;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassMetricsTester {

    @Test
    public void classMetricsTester(){
        ClassMetrics cm = new ClassMetrics("target/classes");
        System.out.println(cm.classMetricString());
    }
    /*
    @Test
    public void wekaMetricsTester() throws IOException {
        ClassMetrics cm = new ClassMetrics("/Users/neil/Google Drive/Teaching/Sheffield/Reengineering/2019-20/Examples/weka-3.8/weka/build/classes");
        //ClassMetrics cm = new ClassMetrics("/tmp");

        writeToFile(new File("wekaClassMetrics.csv"),cm.classMetricString());
    }
    */

    /**
     * Write out the class diagram to a specified file.
     * @param target
     */
    public void writeToFile(File target, String toWrite) throws IOException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(target));
        fw.write(toWrite);
        fw.flush();
        fw.close();
    }
}
