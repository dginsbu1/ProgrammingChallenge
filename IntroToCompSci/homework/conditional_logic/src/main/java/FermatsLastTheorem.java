import java.util.Scanner;
import java.lang.Math;
public class FermatsLastTheorem {  
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter a b c and n on the same line, each separated by a space");
		if(!input.hasNextInt()){
			System.out.println("Program requires integer input: you supplied " + input.next());
			java.lang.System.exit(0);
		}
		int a = input.nextInt();

		if(!input.hasNextInt()){
			System.out.println("Program requires integer input: you supplied " + input.next());
			java.lang.System.exit(0);
		}
		int b = input.nextInt();

		if(!input.hasNextInt()){
			System.out.println("Program requires integer input: you supplied " + input.next());
			java.lang.System.exit(0);
		}
		int c = input.nextInt();

		if(!input.hasNextInt()){
			System.out.println("Program requires integer input: you supplied " + input.next());
			java.lang.System.exit(0);
		}
		int n = input.nextInt();

		int aToN = (int)Math.pow(a, n);
		int bToN = (int)Math.pow(b, n);
		int cToN = (int)Math.pow(c, n);
		boolean equalCase = aToN+bToN == cToN;
		System.out.printf("INPUT... %n a: %d%n b: %d%n c: %d%n (coefficient n): %d%n%n", a,b,c,n);
		System.out.printf("OUTPUT... %nEvaluating %d + %d ==? %d %n", aToN, bToN, cToN); 
		if (n > 2){
			if(equalCase){
			System.out.println("Major discovery: I just proved that Fermat was wrong!");
			}
			else{
			System.out.println("Hmm ... you haven't disproved Fermat yet.");
			}	
		}
		else {
			if(!equalCase){
				System.out.println("The sums are not equal but irrelevant to Fermat's theorem which applies to n>2");
			}
			else{
				System.out.println("Nothing new here: Fermat's theorem applies only to positive integers greater than 2.");
			}
		}
	}
}
