//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.*;
//regex
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//input and output
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
//errors
import javax.swing.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {

        //data structure with lines in it

        //selection system
        Scanner scnr = new Scanner(System.in);
        char action;
        System.out.print("Type Y to proceed and search for file: ");
        action = scnr.next().charAt(0);


        if (action == 'Y' || action == 'y') {
            File initialDirectory = new File("C:\\Users\\comma\\IdeaProjects\\Kindle Highlights Processor\\src");

            JFileChooser fileChooser = new JFileChooser(initialDirectory);
                fileChooser.setDialogTitle("Choose a file");

                int userSelection = fileChooser.showOpenDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToRead = fileChooser.getSelectedFile();
                    //method to add lines to the treemap
                    int key = addLines(fileToRead);

                    //print number of lines recorded in the treemap
                    int s = lines.size();
                    System.out.printf("\n%d lines were recorded\n", s);
                    //select next action
                    int action2;
                    System.out.print("Type 1 to output to a text file: ");
                    action2 = scnr.nextInt();

                    if (action2 == 1) {
                        JFileChooser fileChoose = new JFileChooser();
                        fileChoose.setDialogTitle("Select a directory to save files");
                        fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Set directory selection mode

                        int userSelections = fileChoose.showSaveDialog(null);
                        if (userSelections == JFileChooser.APPROVE_OPTION) {
                            File selectedDirectory = fileChoose.getSelectedFile();
                            System.out.println("Selected directory: " + selectedDirectory.getAbsolutePath());
                            //get the name of the new file
                            System.out.print("Enter the name for the text file you want to save: ");
                            String fileName = scnr.next();
                            fileName = String.format("%s.txt", fileName);
                            File outputFile = new File(selectedDirectory, fileName);
                            // Proceed with file writing or any other operations with the selected directory
                            outputLines(key, outputFile);
                        } else {
                            System.out.println("No directory selected or operation canceled.");
                        }

                    }


                } else {
                    System.out.println("No file chosen or operation canceled.");
                }
        }
        else {
            throw new Exception("Invalid key");
        }

    }


    //public static treemap
    public static Map<Integer, lineRecord> lines = new TreeMap<>();

    //add lines to the treemap and return a key to use later in the program
    public static int addLines (File input) {
        int key = 0;
        int tracker = 0;

        Pattern patternStop = Pattern.compile("==========\n");
        Pattern patternStart = Pattern.compile("^\\s*$");

        try {
            Scanner scanner = new Scanner(input);
            boolean start;
            boolean stop;

            while (scanner.hasNextLine()) {
                //scan in current line
                String line = scanner.nextLine();
                //update the tracker for the line number in the txt file
                tracker++;
                //check the start and stop matches
                Matcher matcherStart = patternStart.matcher(line);
                Matcher matcherStop = patternStop.matcher(line);
                start = matcherStart.find();
                stop = matcherStop.find();
                //set the start and stop variables
                if (stop) {
                    start = false;
                }


                if (start) {
                    //if the start pattern was triggered in the previous line move to the next line
                    line = scanner.nextLine();
                    //key tracks the number of the element added and tracker the original file line
                    key++;
                    tracker++;
                    lineRecord currentRecord = new lineRecord(tracker,line);

                    lines.put(key,currentRecord);
                    System.out.println(tracker + " " + key);
                    System.out.println(lines.get(key).getLineNumber() + " " + lines.get(key).getLineContent());

                }


            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return key;
    }

    public static void outputLines(int key, File input) {

        try (FileOutputStream fos = new FileOutputStream(input)) {
            // Data to write to the file
            for (int current = 1; current <= key; current++) {
                lineRecord data = lines.get(current);
                String insert = String.format("%d: %s (%d)\n", current, data.getLineContent(), data.getLineNumber());
                if (data != null) {
                    // Print the line number and content
                    System.out.print(insert);
                    fos.write(insert.getBytes(StandardCharsets.UTF_8));
                }
            }

            System.out.println("Data written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class lineRecord {
    int lineNumber;
    String lineContent;
    public lineRecord(int inputi, String inputs) {
        lineNumber = inputi;
        lineContent = inputs;
    }

    public void setLineNumber (int input) {
        lineNumber = input;
    }
    public void setLineContent (String input) {
        lineContent = input;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLineContent() {
        return lineContent;
    }

}
