package edu.yu.intro.exceptions; 
import java.util.Scanner;
import java.io.*;
//import java.io.FileNotFoundException;

public class EmployeePay{  
	public static void main( String[] args){
		//checks to make sure their is one input only
		if (args.length != 1) { 
			final String msg = "Usage: EmployeePay name_of_input file";  
			System.err.println(msg);  
			throw new IllegalArgumentException(msg);
		}
		final String inputFileName = args[0];
		final File doc;
		//sets scanner input to filler input
		Scanner input = new Scanner(System.in);

		//tries a file, which might not exist
		try{
			doc = new File (inputFileName);
			input = new Scanner(doc);
		}
		 catch(FileNotFoundException exception){
			System.out.println("I'm sorry, but your file couldn't be found");
			java.lang.System.exit(0);
		}
	
		//System.out.println("Nu? Enter employeeId, hrsWorked, wageRate, deductions:");
		//The program runs as long as their are nore files to be read then exits
		double totalGrossPay = 0;
		double totalTaxes = 0;
		double highestGross = Integer.MIN_VALUE;
		String bigBucks = "No One";
		//keeps track of what line the program is reading
		int counter = 0;
		System.out.printf("Name, Gross Pay, Taxes, Net Pay, Avg Pay %n");

	  while(input.hasNext()){
	  	counter++;
	  	//creates an array of the strings of the current line 
	  	//of input from the doc
	  	String inputLine = input.nextLine();
		String delims = "[ ]+";
	  	String[] inputTokens = inputLine.split(delims);
	  	//verifies the correct numnber of input for the line 
	  	if(inputTokens.length != 4){
	  	 System.out.printf("Problem at line # %d: Expected 4 tokens per line, found %d. Throwing input away, advancing to next line %n", counter, inputTokens.length);  
	  	 continue;
	  	 }
		String name = inputTokens[0];
		double hrsWrkd;
		double wageRt;
		int deduction;
		//tries to parse each input into the correct type
		try{
			hrsWrkd = Double.parseDouble(inputTokens[1]);
		}
		catch(IllegalArgumentException e){
			System.out.printf("Problem at line # %d: Could not parse field #1 [%s] into valid input %n", counter, inputTokens[1]);
			continue;
		}
		try{
			wageRt = Double.parseDouble(inputTokens[2]);
		}
		catch(IllegalArgumentException e){
			System.out.printf("Problem at line # %d: Could not parse field #2 [%s] into valid input %n", counter, inputTokens[2]);
			continue;
		}
		try{
			deduction = Integer.parseInt(inputTokens[3]);
		}
		catch(IllegalArgumentException e){
			System.out.printf("Problem at line # %d: Could not parse field #2 [%s] into valid input %n", counter, inputTokens[3]);
			continue;
			 }

		//check for calculations that are too high or too low
		if(hrsWrkd < 1.0){
			System.out.println("Problem at line # "+counter+ ": The hours worked was less than 1.0. You don't work enough.");
			//java.lang.System.exit(0);
			continue;
		}
		if(wageRt < 15){
			System.out.println("Problem at line # "+counter+ ": Your wage rate is less than 15. You don't get paid enough.");
			//java.lang.System.exit(0);
			continue;

		}
		if(deduction <= 0 || deduction >= 35){
			System.out.println("Problem at line # "+counter+ ": Sorry, your deduction needs to be 1-34 inclusive");
			//java.lang.System.exit(0);
			continue;
		}
		//after finishing the input, we start the calculations
		double grossPay = hrsWrkd*wageRt;
		//tax rate is 15%
		double taxRate = 0.15;
		double tax = grossPay*taxRate;
		//pension is 5%
		double pension = 0.05*grossPay;
		double netPay = grossPay-tax-deduction-pension;
		//checks for ngative netpay
		if(netPay < 0){
			System.out.println("Problem at line # "+counter+ ": Looks like you owe more than you made.");
			continue;
		}
 		double avgPay = netPay/hrsWrkd;
 		if(grossPay > highestGross){
 			highestGross = grossPay;
 			bigBucks = name;
 		}
 		totalTaxes += tax;
 		totalGrossPay += grossPay;
 		//this prints all the computed info
 		System.out.printf("%s, %.2f, %.2f, %.2f, %.2f %n", name, grossPay, tax, netPay, avgPay);
 	  }
 	  System.out.printf("%nTotal Gross pay: %.2f %n", totalGrossPay);
 	  System.out.printf("Total Taxes: %.2f %n", totalTaxes);
 	  System.out.printf("Employee with Largest Gross Pay: %s", bigBucks);
 	}
 }

