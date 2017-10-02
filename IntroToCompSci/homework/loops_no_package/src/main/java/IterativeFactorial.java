import java.util.Scanner;

public class IterativeFactorial{
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
	
		System.out.print("Enter `n` -- I will calculate `factorial(n)`: ");
		int n = input.nextInt();
		//System.out.println(n);
		System.out.println("***********************************************");
		if(n < 0){
			System.out.println("You need to enter a non-negative integer," + 
				" and you entered " + n);
			java.lang.System.exit(0);
		}
		long sum = 1;
		for(int i=n; i>0; i--){
			sum *= i;
		}
		System.out.printf("factorial(%d) = %d %n" , n, sum);
		System.out.println("***********************************************");
	}
}