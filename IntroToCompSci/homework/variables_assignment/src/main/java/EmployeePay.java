import java.util.Scanner;

public class EmployeePay {  
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		System.out.println("Nu? Enter employeeId, hrsWorked, wageRate, deductions:");
		String name = input.next();
		double hrsWrkd = input.nextDouble();
		double wage = input.nextDouble();
		int deduction = input.nextInt();
		//after finishing the input, we start the calculations
		double grossPay = hrsWrkd*wage;
		//tax rate is 15%
		double taxRate = 0.15;
		double tax = grossPay*taxRate;
		//pension is 5%
		double pension = 0.05*grossPay;
		double netPay = grossPay-tax-deduction-pension;
 		double avgPay = netPay/hrsWrkd;
 		//this prints all the inputted info
 		System.out.println("*******************************************************");
 		System.out.printf("INPUT... %n%-20s%s %n%-20s%.2f %n%-20s%.2f %n%-20s%d %n%n", "Employee Id:", name, "Hours Worked:", hrsWrkd, "Wage Rate:", wage, "Deduction:", deduction);
 		//this prints all the compued info
 		System.out.printf("OUTPUT... %n%-20s%.2f %n%-20s%.2f %n%-20s%.2f %n%-20s%.2f", "Gross Pay:", grossPay, "Taxes:", tax, "Net pay:", netPay, "Average pay:", avgPay);
 	}
 }

