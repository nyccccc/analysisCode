package fileComparison;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by neilwalkinshaw on 21/11/2016.
 */
public class DuplicateDetector {

    List<File> files;
    String suffix;
    int threshold = 3;

    public DuplicateDetector(File root, String suffix){
        files = new ArrayList<File>();
        this.suffix = suffix;
        populateFiles(root);

    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    private void populateFiles(File root) {
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(root.listFiles()));
        for(File f: files) {
            if(f.isDirectory())
                populateFiles(f);
            else if(f.getName().endsWith(suffix)){
                this.files.add(f);
            }
        }
    }

    public void fileComparison(File target) throws IOException {
        FileWriter fw = new FileWriter(target);
        CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.EXCEL);
        double[][] fileCompare = new double[files.size()][files.size()];

        for(int i = 0; i<files.size(); i++){
            for(int j = i+1; j<files.size(); j++){
                double score;
                double scoreProportional = 0D;
                if(i == j)
                    score = 0;
                else{
                    FileComparator fc = new FileComparator(files.get(i),files.get(j));
                    score = fc.coarseCompare(false);
                    scoreProportional = fc.coarseCompare(true);
                }
                List<String> record = new ArrayList<String>();
                record.add(files.get(i).toString());
                record.add(files.get(j).toString());
                record.add(Double.toString(score));
                record.add(Double.toString(scoreProportional));
                if(score > threshold)
                    csvPrinter.printRecord(record);
            }
        }
        csvPrinter.close();
    }

    public List<File> getFiles(){
        return files;
    }

}
