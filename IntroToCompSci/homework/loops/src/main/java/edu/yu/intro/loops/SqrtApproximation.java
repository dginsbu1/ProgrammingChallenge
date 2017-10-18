package edu.yu.intro.loops;
import java.util.Scanner;

public class SqrtApproximation{
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter `a` -- I will approximate `sqrt(a)`: ");
		double a = input.nextDouble(); 
		//System.out.println(a);
		if(a <= 0){
			System.out.println("I'm sorry but the number you entered" +
				" is not a positive number. I can't continue.");
			java.lang.System.exit(0);
		}
		System.out.println("Math.sqrt() reports " + Math.sqrt(a));
		System.out.println("*************************************************");
		//this is used as an original guess
		double sqrt = a/2;
		int iteration = 0;
		int precise = 0;
		while(precise < 2){
			sqrt = 0.5*(sqrt + a/sqrt);
			if(Math.abs(sqrt - Math.sqrt(a)) < 0.0001){
				precise++;
			}
			else{
				precise = 0;
			}
			iteration++;
		}

		System.out.printf("sqrt(%f) = %.3f after %d iteration(s) %n", a, sqrt, iteration);
		System.out.println("*************************************************");
	}
}