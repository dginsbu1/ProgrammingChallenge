package edu.yu.intro.arrays; 
import java.util.Scanner;
import java.io.*;

public class EmployeePay{  
	public static void main( String[] args){
		if(args.length != 2) {     
			final String msg = "Usage: EmployeePay employeeInputFile deptInputFile";      
			System.err.println(msg);      
			throw new IllegalArgumentException(msg);   
		}

	    final String employeeInputFile = args[0];   
	    final String deptInputFile = args[1];

	    Scanner inputEmployee = new Scanner(System.in);
	    Scanner inputDep = new Scanner(System.in);
	   try{
			inputEmployee = new Scanner(new File(employeeInputFile));
			inputDep = new Scanner(new File(deptInputFile));
		}
		 catch(FileNotFoundException exception){
			System.out.println("I'm sorry, but your file(s) couldn't be found");
			java.lang.System.exit(0);
		}

	    String[] depList = new String[10];
	   
	    int counter = 0;
	    while(inputDep.hasNext()){
	    	counter++;
	    	String newDep = inputDep.next();
	    	//make sure that no repeats
	    	for(int i = 0; i < depList.length; i++){
	    		String oldDep = depList[i];
	    		//if the depList is not full, then add it to the list
	    		if(oldDep == null){
	    			depList[i] = newDep;
	    			break;
	    		}
	    		//assuming the department spot isnt full, check for repeat
	    		if(newDep.equals(oldDep)){
	    			System.out.printf("Duplicate at line %d of department file. %s already exists %n", counter, newDep);
	    			break;
	    		}else if(i==depList.length-1){
	    			System.out.printf("Overload: too many departments. Melting...boom");
	    			System.exit(0);
	    		}


	    	}//the lsit is complete (may not have ten deps)
	    }
//////////////////////////////////////////////////////////////////////////
	    //sets up holders for employees by department
	    int[] depNum = new int[10];
	    double[] depGrossPay = new double[10];

		//The program runs as long as their are nore files to be read then exits
		double totalGrossPay = 0;
		double totalTaxes = 0;
		//keeps track of what line the program is reading
		counter = 0;
	  while(inputEmployee.hasNext()){
	  	counter++;
	  	//creates an array of the strings of the current line 
	  	//of input from the doc
	  	String inputLine = inputEmployee.nextLine();
		String delims = "[ ]+";
	  	String[] inputTokens = inputLine.split(delims);
	  	//verifies the correct numnber of input for the line 
	  	if(inputTokens.length != 5){
	  	 System.out.printf("Problem at line # %d: Expected 5 tokens per line, found %d. Throwing input away, advancing to next line %n", counter, inputTokens.length);  
	  	 continue;
	  	 }
		String name = inputTokens[0];
		double hrsWrkd;
		double wageRt;
		int deduction;
		String employeeDep = inputTokens[4];
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
			continue;
		}
		if(wageRt < 15){
			System.out.println("Problem at line # "+counter+ ": Your wage rate is less than 15. You don't get paid enough.");
			continue;

		}
		if(deduction <= 0 || deduction >= 35){
			System.out.println("Problem at line # "+counter+ ": Sorry, your deduction needs to be 1-34 inclusive");
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
 		//checks to see which department the employe belongs to
 		for(int i = 0; i < depList.length; i++){
 			//stop if run out of deps
 			if(depList[i] == null){
 				break;
 			}
	    	if(depList[i].equals(employeeDep)){
	    		depNum[i]++;
	    		depGrossPay[i]+= grossPay;
	    		break;
	    	}
	    	if(i == depList.length-1){
	    	System.out.printf("Problem at line # %d: Employee %s doesn't belong to any known department. %n", counter, name);
	    	break;
	    	}
	    }
	  }
	  System.out.printf("Department %11s Employees %25s %27s %n", "#", "Total Gross Pay", "Average Gross Pay");
	  for(int i = 0; i < depList.length; i++){
	  	//stop if run out of deps
	  	if(depList[i] == null){
 				break;
 			}
	  	System.out.printf("%s %26d %26.2f %26.2f %n", depList[i], depNum[i], depGrossPay[i], depGrossPay[i]/depNum[i]);
	  }
    }
} 