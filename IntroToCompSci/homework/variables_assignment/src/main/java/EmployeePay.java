import java.util.Scanner;

public class EmployeePay {  
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		System.out.println("Nu? Enter employeeId, hrsWorked, wageRate, deductions:");
		String name = input.next();
		double hrsWrkd = input.nextDouble();
		double wageRt = input.nextDouble();
		int deduction = input.nextInt();
		//check for erors
		/*if(hrsWrkd < 1.0 || wageRt < 15 || deduction <= 0 || deduction >= 35){
			System.out.println("Sorry, put one of your inputs was invalid. You don't deserve to use this program.");
			java.lang.System.exit(0); 
		}*/

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
 		//this prints all the inputted info
 		System.out.println("*******************************************************");
 		System.out.printf("INPUT... %n%-20s%s %n", "Employee Id:", name);   
 		System.out.printf("%-20s%.2f %n", "Hours Worked:", hrsWrkd);
 		System.out.printf("%-20s%.2f %n", "Wage Rate:", wageRt);
 		System.out.printf("%-20s%d %n%n", "Deduction:", deduction);
 		
 		//this prints all the compued info
 		System.out.printf("OUTPUT... %n%-20s%.2f %n", "Gross Pay:", grossPay);
 		System.out.printf("%-20s%.2f %n", "Taxes:", tax); 
 		System.out.printf("%-20s%.2f %n", "Net pay:", netPay);
 		System.out.printf("%-20s%.2f %n", "Average pay:", avgPay);
 	}
 }

