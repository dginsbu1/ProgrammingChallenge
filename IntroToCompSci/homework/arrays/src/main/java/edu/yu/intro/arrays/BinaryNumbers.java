package edu.yu.intro.arrays;
import java.util.Scanner;

public class BinaryNumbers{
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		System.out.print("Nu? Enter an integer greater than zero: ");
		final int num = input.nextInt(); 
		//System.out.println(a);
		if(num <= 0){
			System.out.printf("I'm sorry but %d is not an integer greater than 0.", num);
			java.lang.System.exit(0);
		}
		System.out.printf("Converting %d to binary representation %n", num);
		String numString = "";
		int numTemp = num;
		while(numTemp > 0){
			numString = numTemp%2 + numString;
			numTemp /= 2; 
		}
		System.out.printf("%d -> %s %n", num, numString);
		//test 
		//System.out.println(Integer.toBinaryString(num));
	}
}