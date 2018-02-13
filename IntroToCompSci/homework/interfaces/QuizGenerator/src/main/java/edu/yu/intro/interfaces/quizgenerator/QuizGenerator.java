package edu.yu.intro.interfaces.quizgenerator; 
import java.util.Scanner;

public class QuizGenerator{
	//runs the quiz
	public static void main(String[] args) {
		IntQuestion[] questions = new IntQuestion[10];
		int[] userAns = new int [10];
		createQuiz(questions);
		giveQuiz(questions, userAns);
		getQuizResults(questions, userAns);
	}
	//creates the quiz by calling on the AdditionQuesion/SubtractionQuestion class
	public static void createQuiz(IntQuestion[] qs) {
		qs[0] = new AdditionQuestion();
		qs[1] = new SubtractionQuestion(); 
		for(int i= 2; i<qs.length; i++){
			if(Math.random() < 0.5){
				qs[i] = new AdditionQuestion();
			}
			else{
				qs[i] = new SubtractionQuestion();
			}
		}

	}
	//gives the quiz and records the answers
	public static void giveQuiz(IntQuestion[] qs, int[] userAnswers){
		System.out.println("Welcome to the addition quiz! \n");
		Scanner input = new Scanner(System.in);
		for(int i=0; i<qs.length; i++){
		System.out.printf("Question %2d: ", i+1);
		System.out.print(qs[i].getQuestion());
		userAnswers[i] = input.nextInt();
		}
	}	
	//displays the correct answers, the users answers and the results
	public static void getQuizResults(IntQuestion[] qs, int[] userAnswers){
		int grade = 0;
		System.out.print("\nHere are the correct answers: \n"); 
		for(int i=0; i<qs.length; i++){
		System.out.printf("Question %2d: ", i+1);
		System.out.print(qs[i].getQuestion());
		System.out.printf("Correct answer is %d. ", qs[i].getCorrectAnswer());
			if(userAnswers[i] == qs[i].getCorrectAnswer()){
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