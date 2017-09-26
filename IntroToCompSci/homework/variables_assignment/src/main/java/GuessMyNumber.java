import java.util.*;
import java.util.Random;
import java.util.Scanner;
/** * Hello world! * */ 
public class GuessMyNumber {  
	public static void main( String[] args ) {
		//if you want it to repeat  
		//while(true){
			Random giveRan = new Random();
			int ranNum = giveRan.nextInt(100)+1; 
   		 	System.out.println("\nHi, for securtiy reasons, we must ask you to guess an integer between 1-100 inclusive.\nPlease type your guess and press enter");
			Scanner input = new Scanner(System.in);
			try {
				int guess = input.nextInt();
				if (guess < 1 || guess > 100){
				System.out.println("I'm sorry to inform you that due to your\ninability to differentiate between integers 1-100 and all other\nnumbers/letters, you are not qualified to continue working here. \nIf you have any concerns, please discuss them with the Senior Partner");
				} 
				else{
					System.out.println("The number was " + ranNum);
					System.out.println("You're guess was off by " + Math.abs(ranNum - guess)); 
					}
			}
								
 			catch (InputMismatchException e) {
				System.out.println("I'm sorry to inform you that due to your\ninability to differentiate between integers 1-100 and all other \nnumbers/letters, you are not qualified to continue working here.\nIf you have any concerns, please discuss them with the Senior Partner");

			}
		//}
	}
}