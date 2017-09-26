import java.util.Scanner;
public class CelsiusToFahrenheit{  
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		System.out.println("Please type in a temperature in Celcius");
		double tempC = input.nextDouble();
		//uses conversion ratio to go from C to F
		double tempF = tempC*9/5 + 32;
		System.out.printf("%.1f C = %.1f F", tempC, tempF);
	}
}


