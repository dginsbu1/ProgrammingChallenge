package edu.yu.intro.methods.sqrt;
import java.util.Scanner;
import java.io.*;


public class RefactoredSqrtApproximation{  

	public static void main( String[] args) throws IllegalArgumentException{
		Scanner input = new Scanner(System.in);
		System.out.print("Enter `n` -- I will calculate all square roots from 0 to n (inclusive) using Babylonian approximation algorithm: ");
		if(!input.hasNextInt()){
			throw new IllegalArgumentException("Your input of " +input.next()+ " was not valid");
		}
			int a = input.nextInt(); 
		//System.out.println(a);
		if(a < 0){
			throw new IllegalArgumentException("Your input of " +a+ " was not valid");
		}
		/*for testing code
		System.out.println("Math.sqrt() reports " + Math.sqrt(a)); */
		System.out.println("*************************************************");
		int counter = 0;
		for(double i: calculateSquareRoots(a)){
			System.out.printf("sqrt(%d) =  %.3f %n", counter, i);
			counter++;
		}

		System.out.println("*************************************************");
	}
	
	public static double[] calculateSquareRoots(int n){
		double[] sqrtArray = {0};
		if(n == 0){
			return sqrtArray;	
		}
		sqrtArray = new double[n+1];
		sqrtArray[0] = 0;
		for(int i = n; i>0; i--){
			sqrtArray[i] = sqrt(i);
		}
		return sqrtArray;
	} 


	public static double sqrt(double a){
		//checks if a is less than zero
		if(a < 0){
			throw new IllegalArgumentException("Your input of " +a+ " was not valid");
		}
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
		return sqrt;
	}

}
