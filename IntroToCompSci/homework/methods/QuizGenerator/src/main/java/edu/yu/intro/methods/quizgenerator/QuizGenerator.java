package edu.yu.intro.methods.quizgenerator; 
import java.util.Scanner;

public class QuizGenerator{
	public final static int N_QUIZ_QUESTIONS = 10; 

	public static void main(String[] args) {
		int[] firstNums = new int[N_QUIZ_QUESTIONS];
		int[] secondNums = new int[N_QUIZ_QUESTIONS];
		int[] userAns = new int [N_QUIZ_QUESTIONS];
		boolean[] quizRes = new boolean[N_QUIZ_QUESTIONS];
		createQuiz(firstNums, secondNums);
		giveQuiz(firstNums, secondNums, userAns);
		quizRes = gradeQuiz(firstNums, secondNums, userAns);
		getQuizResults(firstNums, secondNums, userAns, quizRes);
	}

	public static void createQuiz(int[] firstNumbers, int[] secondNumbers) {
		for(int i= 0; i<firstNumbers.length; i++){
			//sets valu to 0-50
			firstNumbers[i] = (int)(51*Math.random());
			secondNumbers[i] = (int)(51*Math.random());
		}

	}

	public static void giveQuiz(int[] firstNumbers, int[] secondNumbers, int[] userAnswers){
		System.out.println("Welcome to the addition quiz! \n");
		Scanner input = new Scanner(System.in);
		for(int i=0; i<firstNumbers.length; i++){
		System.out.printf("Question %2d: What is %2d + %2d ? ", i+1, firstNumbers[i], secondNumbers[i]);
		userAnswers[i] = input.nextInt();
		}
	}
	//hecks the users answers against the actual answers and sets
	//the boolean result to an aray
	public static boolean[] gradeQuiz(int[] firstNumbers, int[] secondNumbers, int[] userAnswers){
		boolean[] quizResults = new boolean[userAnswers.length];
		for(int i=0; i<firstNumbers.length; i++){
			quizResults[i] = (userAnswers[i] == firstNumbers[i]+secondNumbers[i]);
		}
		return quizResults;
	}
	//displays the correct answers and the users answers
	public static void getQuizResults(int[] firstNumbers, int[] secondNumbers, int[] userAnswers, boolean[] quizResults){
		int grade = 0;
		System.out.print("\nHere are the correct answers: \n"); 
		for(int i=0; i<firstNumbers.length; i++){
			System.out.printf("Question %2d: %2d + %2d = %2d  ", i+1, firstNumbers[i], secondNumbers[i], firstNumbers[i]+secondNumbers[i]);
			if(quizResults[i]){
				System.out.print("You were CORRECT. \n");
				grade++;
			}
			else{
				System.out.printf("You said %d, which is INCORRECT. \n", userAnswers[i]);
			}
		}
		System.out.printf("You got %2d question(s) correct. %nYour grade on the quiz is %2d",grade, grade*10);
	}
}