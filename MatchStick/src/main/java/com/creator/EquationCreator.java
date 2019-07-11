package com.creator;
import sun.awt.Symbol;

//import javax.swing.plaf.TableHeaderUI;
//import java.lang.Math;
import java.util.Random;

public class EquationCreator {
    static int limit = 10;
    static Random randomNumGen = new Random();
    //creates prints a random addition or subtraction equation
    // using digits less then 10
     public static void main(String[] args){
         //String[] equation = createEquation();
         //String equ = equationToString(equation);
         //System.out.println(equ);
//         String[] equation;
//         String equ;
//         int x =0;
//         while(x<1000){
//             equation = createEquation();
//             equ = equationToString(equation);
//             System.out.println(equ);
//             x++;
//         }
     }
    //creates and returns an array containing the elements of an random equation ex. {2,+,2,=,4}
    public static String[] createEquation(){
        int randIntOne = randomNumGen.nextInt(limit);
        getRandSymbol();
        String sym = getRandSymbol();
        int randIntTwo = getRandNum(randIntOne, sym);
        int result = getResult(randIntOne,sym,randIntTwo);
        String[] equation = {randIntOne+"",sym,randIntTwo+"","=", result+""};
        //System.out.println("The equation is "+equationToString(equation));
        return equation;
    }
    //turns an String array into a regular String
    public static String equationToString(String[] equation){
         StringBuilder equ = new StringBuilder();
         for(String element : equation){
             equ.append(element);
         }
         return equ.toString();
    }
    //@return the result of either adding or subtracting the two inputs
    private static int getResult(int rand1, String sym, int rand2) {
        if(sym.equals("+")){//addition
            return rand1 + rand2;
        }
        else if(sym.equals("-")){//subtraction
            return rand1 - rand2;
        }
        return -1;//should never happen
    }
    //returns a random number between 0 and limit(9) depending on the inputs
    private static int getRandNum(int rand1, String sym) {
        int high = 0;
        if(sym.equals("+")){//guarantees the result won't exceed the limit
             high = limit - rand1;
        }
        else if(sym.equals("-")){//guarantees the result will not be negative
            high = rand1+1;
        }
        return randomNumGen.nextInt(high);
    }
    //returns a random arithmetic symbol ("-","+")
    private static String getRandSymbol() {
        String[] symArr = {"+","-"};
        int symPos = (int)(Math.random()*(symArr.length));
        String sym = symArr[symPos];
        return sym;
    }
}
