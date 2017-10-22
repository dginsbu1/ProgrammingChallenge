package edu.yu.intro.loops; 
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class EmployeePay {  
	public static void main( String[] args) throws FileNotFoundException{

		if (args.length != 1) { 
			final String msg = "Usage: EmployeePay name_of_input file";  
			System.err.println(msg);  
			throw new IllegalArgumentException(msg);
		}
		final String inputFileName = args[0]; 
		final File doc = new File (inputFileName);
		Scanner input = new Scanner(doc);

		//System.out.println("Nu? Enter employeeId, hrsWorked, wageRate, deductions:");
		//The program runs as long as their are nore files to be read then exits
		double totalGrossPay = 0;
		double totalTaxes = 0;
		double highestGross = Integer.MIN_VALUE;
		String bigBucks = "";
		System.out.printf("Name, Gross Pay, Taxes, Net Pay, Avg Pay %n");

	  while(input.hasNext()){ 
		String name = input.next();
		double hrsWrkd = input.nextDouble();
		double wageRt = input.nextDouble();
		int deduction = input.nextInt();
		//this prints all the inputted info
 		/*System.out.println("*******************************************************");
 		System.out.printf("INPUT... %n%-20s%s %n", "Employee Id:", name);   
 		System.out.printf("%-20s%.2f %n", "Hours Worked:", hrsWrkd);
 		System.out.printf("%-20s%.2f %n", "Wage Rate:", wageRt);
 		System.out.printf("%-20s%d %n%n", "Deduction:", deduction);*/
		//check for erors
		if(hrsWrkd < 1.0){
			System.out.println("The hours worked was less than 1.0. You don't work enough.");
			//java.lang.System.exit(0);
			continue;
		}
		if(wageRt < 15){
			System.out.println("Your wage rate is less than 15. You don't get paid enough.");
			//java.lang.System.exit(0);
			continue;

		}
		if(deduction <= 0 || deduction >= 35){
			System.out.println("Sorry, your deduction needs to be 1-34 inclusive");
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
			System.out.println("Looks like you owe more than you made.");
			//java.lang.System.exit(0); 
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
 		//System.out.printf("OUTPUT... %n%-20s%.2f %n", "Gross Pay:", grossPay);
 		/*System.out.printf("Name: %s %n", name);
 		System.out.printf("Gross Pay", name);
 		System.out.printf("%-20s%.2f %n", "Taxes:", tax); 
 		System.out.printf("%-20s%.2f %n", "Net pay:", netPay);
 		System.out.printf("%-20s%.2f %n", "Average pay:", avgPay);*/
 	  }
 	  System.out.printf("%nTotal Gross pay: %.2f %n", totalGrossPay);
 	  System.out.printf("Total Taxes: %.2f %n", totalTaxes);
 	  System.out.printf("Employee with Largest Gross Pay: %s", bigBucks);
 	}
 }

