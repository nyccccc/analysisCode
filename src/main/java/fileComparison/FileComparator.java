package fileComparison;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by neilwalkinshaw on 21/11/2016.
 */
public class FileComparator {

    File from,to;
    Set<String> unwantedLines = new HashSet<>();

    public FileComparator(File from, File to){
        this.from = from;
        this.to = to;
        unwantedLines.add("}");
        unwantedLines.add("");
        unwantedLines.add("{");
        unwantedLines.add(";");
        unwantedLines.add("*");
        unwantedLines.add("/**");
        unwantedLines.add("*/");
    }

    public double coarseCompare(boolean proportional){
        List<String> fromFile = new ArrayList<String>();
        List<String> toFile = new ArrayList<String>();
        extractStrings(fromFile, from);
        extractStrings(toFile, to);
        int total =fromFile.size() + toFile.size();
        fromFile.retainAll(toFile);
        double returnVal = fromFile.size();
        if(proportional)
            returnVal = returnVal / total;
        return returnVal;
    }

    private void extractStrings(Collection<String> fromFile, File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while(line != null) {
                line = line.trim();
                fromFile.add(line);
                line = br.readLine();
            }
            br.close();
            fromFile.removeAll(unwantedLines);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean[][] detailedCompare(){
        List<String> fromFile = new ArrayList<String>();
        List<String> toFile = new ArrayList<String>();
        extractStrings(fromFile, from);
        extractStrings(toFile, to);
        boolean[][] matrix = new boolean[fromFile.size()][toFile.size()];
        for(int i = 0; i< fromFile.size(); i++){
            for(int j = 0; j<toFile.size(); j++){
                boolean match = fromFile.get(i).equals(toFile.get(j));
                if(unwantedLines.contains(fromFile.get(i)) || unwantedLines.contains(toFile.get(j))||
                        fromFile.get(i).length() == 0 || toFile.get(j).length()==0) {
                    match = false;
                }
                matrix[i][j]=match;
            }
        }
        return matrix;
    }

    public void printRelations(boolean[][] scores, File target) throws IOException {

        List<String> fromFile = new ArrayList<String>();
        List<String> toFile = new ArrayList<String>();
        extractStrings(fromFile, from);
        extractStrings(toFile, to);

        FileWriter fw = new FileWriter(target);
        CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.EXCEL);
        List<String> record = new ArrayList<String>();
        for(int i = 0; i< fromFile.size(); i++){
            record.add(fromFile.get(i));
        }
        csvPrinter.printRecord(record);


        for(int j = 0; j<toFile.size(); j++){
            List<String >dup = new ArrayList<String>();
            dup.add(toFile.get(j));
            for(int i = 0; i< fromFile.size(); i++) {
                if(scores[i][j]){
                    dup.add("1");
                }
                else
                    dup.add("0");
            }
            csvPrinter.printRecord(dup);
        }

        csvPrinter.close();
    }

    public void comparisonBitmap(boolean[][] scores, File target) throws IOException {

        List<String> fromFile = new ArrayList<String>();
        List<String> toFile = new ArrayList<String>();
        extractStrings(fromFile, from);
        extractStrings(toFile, to);

         BufferedImage image = new BufferedImage(fromFile.size(), toFile.size(), BufferedImage.TYPE_INT_RGB);

        for(int j = 0; j<toFile.size(); j++){
                        for(int i = 0; i< fromFile.size(); i++) {
                if(scores[i][j]){
                    image.setRGB(i,j, Color.yellow.getRGB());
                }
                else
                    image.setRGB(i,j,Color.GRAY.getRGB());;
            }

        }
        ImageIO.write(image,"png",target);
    }

}
