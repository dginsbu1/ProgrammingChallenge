import java.util.Scanner;

public class EmployeePay {  
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		System.out.println("Nu? Enter employeeId, hrsWorked, wageRate, deductions:");
		String name = input.next();
		double hrsWrkd = input.nextDouble();
		double wageRt = input.nextDouble();
		int deduction = input.nextInt();
		//this prints all the inputted info
 		System.out.println("*******************************************************");
 		System.out.printf("INPUT... %n%-20s%s %n", "Employee Id:", name);   
 		System.out.printf("%-20s%.2f %n", "Hours Worked:", hrsWrkd);
 		System.out.printf("%-20s%.2f %n", "Wage Rate:", wageRt);
 		System.out.printf("%-20s%d %n%n", "Deduction:", deduction);
		//check for erors
		if(hrsWrkd < 1.0 ){
			System.out.println("The hours worked was less than 1.0. You don't work enough.");
			java.lang.System.exit(0);
		}
		if(wageRt < 15  ){
			System.out.println("Your wage rate is less than 15. You don't get paid enough.");
			java.lang.System.exit(0);
		}
		if(deduction <= 0 || deduction >= 35 ){
			System.out.println("Sorry, your deduction needs to be 1-34 inclusive");
			java.lang.System.exit(0);
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
			java.lang.System.exit(0); 
		}
 		double avgPay = netPay/hrsWrkd;
 		
 		//this prints all the compued info
 		System.out.printf("OUTPUT... %n%-20s%.2f %n", "Gross Pay:", grossPay);
 		System.out.printf("%-20s%.2f %n", "Taxes:", tax); 
 		System.out.printf("%-20s%.2f %n", "Net pay:", netPay);
 		System.out.printf("%-20s%.2f %n", "Average pay:", avgPay);
 	}
 }

