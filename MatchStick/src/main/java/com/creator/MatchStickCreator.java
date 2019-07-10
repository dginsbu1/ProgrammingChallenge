package com.creator;

import com.creator.EquationCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class MatchStickCreator {
    static String localDir = System.getProperty("user.dir");
    static String NuSMVFilePath = localDir+"\\src\\main\\resources\\creator\\NuSMVCreate.smv";
    static String NuSMVResultsPath = localDir+"\\src\\main\\resources\\creator\\CreateResults.txt";
    static String NuSMVFilePathRead = localDir+"\\src\\main\\resources\\creator\\NuSMVFilePartThree";
    static Scanner input = new Scanner(System.in);
    static private String type;
    static private int used;
    static private int hintsGiven = 0;//used for hints
    static private int ADDSIZE = 20;//used for how many adds and subs there are
    static private String partThree;

    //runs a program that allows the user to solve various matchstick problems
    public static void main(String args[]) throws IOException, InterruptedException {
        System.out.println("Hello, we're doing some Matchstick problems today.");
        generalSetUp();
        while(true) {
            String type = getQuestionType();
            System.out.println("The question type is: "+type);
//            int x = 0;
//            while(x<10){
//                String[] equation = EquationCreator.createEquation();
//                System.out.println(EquationCreator.equationToString(equation));
//                x++;
//            }
            String[] equation = EquationCreator.createEquation();
            writeNuSMV(equation, type);
            sleep(500);
            runNuSMV();
            sleep(500);
            String[] result = scanNuSMV();
            int numMoves = setUpVariables();
            displayResults(result, numMoves);
            boolean correct = false;
            System.out.println("If you need help you can type \"hint\"" +
                    " for a hint or \"help\" for the answer");
            while(!correct){
                correct = checkAnswer(equation);
            }
        }
    }
    //sets up the static portion of the NuSMV file
    private static void generalSetUp() throws IOException {
        setPartThree();
    }

    //determines the type of question to create (addition, subtraction, move) based on user
    private static String getQuestionType() {
        System.out.println("what type of problem do you want? s (subtraction) a (addition), "+
                "or m (move)?");
        String problemType = input.nextLine();
        type = problemType;
        return problemType;
    }
    //writes a NuSMV that creates a mathstick problem based on the input
    private static void writeNuSMV(String[] equation, String type) throws IOException, InterruptedException {
        String partOne = setPartOne(equation);
        String LTLSPEC = setPartTwo(type);
        //partThree already set.
        //String partThree =  setPartThree();
        FileWriter fw = new FileWriter(NuSMVFilePath);
        fw.write(partOne);
        fw.write(LTLSPEC);
        fw.write(partThree);
        fw.close();
        sleep(300);
    }
    //runs the NuSMV file and save the results to CreateResults.txt
    private static void runNuSMV() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c"+"NuSMV -bmc "+NuSMVFilePath+" > "+NuSMVResultsPath;
        Process child = Runtime.getRuntime().exec(command);
    }
    //scans the CreateResults.txt and extracts the elements of the equation
    //as well as the number of moves
    private static String[] scanNuSMV() throws IOException {
        String[] result = new String[5];
        InputStream is = new FileInputStream(NuSMVResultsPath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        while (line != null) {
            int lineSize = line.length();
            if (line.contains(".output") && !line.contains("specification")) {
                if (line.contains("a"))//first number
                    result[0] = line.charAt(lineSize - 1)+"";
                if (line.contains("d"))//arithmetic symbol i.e. +/-
                    result[1] = line.charAt(lineSize - 2)+"";
                if (line.contains("b"))//second number
                    result[2] = line.charAt(lineSize - 1)+"";
                if (line.contains("c"))//last number
                    result[4] = line.charAt(lineSize - 1)+"";
            }
            if(line.contains("used = ") && !line.contains(".")) {//for number used
                used = Integer.valueOf(line.substring(line.indexOf("= ")+2));
            }
            line = buf.readLine();
        }
        result[3] = "=";//equal sign
        //System.out.println("The array is: "+result[0]+result[1]+result[2]+result[3]+result[4]);
        return result;
    }
    //sets the number of available moves
    //the additions
    private static int setUpVariables() {
        //System.out.println("Setting up variables");
        int numMoves = 0;
        if(type.equals("a")) numMoves = used / 20;
        if(type.equals("s")) numMoves = used % 20;
        if(type.equals("m")) numMoves = used % 20;
        //System.out.println("setUpVariables: you have moves "+numMoves);
        return numMoves;
    }
    //displays the equation to the command line
    private static void displayResults(String[] result, int numMoves) {
        String resultString = equationToString(result);
        System.out.println("Your problem is as follows:\n"+
                resultString+"\nYou have "+numMoves+" "+getType()+"(s)");
    }
    //returns the type of problem being created (addition, subtraction, move)
    public static String getType(){
        if(type.equals("a")) return "addition";
        if(type.equals("s")) return "subtraction";
        if(type.equals("m")) return "move";
        return null;
    }

    //turns an String array into a regular String
    private static String equationToString(String[] result) {
        StringBuilder equationString = new StringBuilder();
        for(String element: result)
            equationString.append(element);
        String equation = equationString.toString();
        return equation;
    }
    //checks the user's answer against the solution
    private static boolean checkAnswer(String[] equation) throws InterruptedException {
        sleep(700);
        System.out.println("What's the answer?");
        String suggestion = input.nextLine();
        String answer = equationToString(equation);
        suggestion = suggestion.replace(" ","");//remove spaces

        if(suggestion.equals(answer)){
            System.out.println("Congratulations, you got it!!!.\nLet's go again.");
            hintsGiven = 0;//resets the hints
            return true;
        } else if(suggestion.equals("help")) {
            giveHint(equation);
            System.out.println("It's a tough one, isn't it?");
            System.out.println("Here's the solution");
            System.out.println(answer);
        } else if(suggestion.equals("hint")) {
            giveHint(equation);
        } else {
            System.out.println("Nope, but good try.");
            return false;
        }
        return false;
    }
    private static void giveHint(String[] equation) {
        if(hintsGiven > 4){
            System.out.println("I literally gave you the whole equation...\n" +
                    "maybe you should stick with basic addition problems");
            return;
        }
        System.out.printf("Okay the element in position %d is %s\n",hintsGiven+1,equation[hintsGiven]);
        hintsGiven++;
    }
    //sets the first part of the NuSMV file based on the equation given
    private static String setPartOne(String[] equ) {
        return "MODULE main\n" +
                "VAR\n" +
                "\ta : changeNum("+equ[0]+", 0); --mov*21\n" +
                "\tb : changeNum("+equ[2]+", a.used);\n" +
                "\tc : changeNum("+equ[4]+", b.used);\n" +
                "\td : changeSymbol(\""+equ[1]+"\", c.used);\n" +
                "DEFINE\n" +
                " mini := 20;\n" +
                "  pls := 1;\n" +
                " used := -(d.used);\n" +
                "  add := (used / mini);\n" +
                "  sub := (used mod mini) / pls;";
    }
    //sets the middle part of the NuSMV file based on the type
    private static String setPartTwo(String type) {
        String LTLSPEC = "";
        if(type.equals("a")) LTLSPEC = "\nLTLSPEC G !(add != 0 & sub = 0); -- adding\n";
        if(type.equals("s")) LTLSPEC = "\nLTLSPEC G !(sub != 0 & add = 0); -- subtracting\n";
        if(type.equals("m")) LTLSPEC = "\nLTLSPEC G !(sub != 0 & add = sub);--moving\n";
        return LTLSPEC;
    }
    //sets the second half the equation (which never changes
    private static String setPartThree() throws IOException {
        Path path = Paths.get(NuSMVFilePathRead);
        partThree = new String(Files.readAllBytes(Paths.get(NuSMVFilePathRead)));
        return partThree;
    }
}
