package edu.yu.intro.interfaces.quizgenerator; 

public class AdditionQuestion implements IntQuestion{
	int firstNumber, secondNumber;

  	public AdditionQuestion() { 
		//sets valu to 0-50
		firstNumber = (int)(51*Math.random());
		secondNumber = (int)(51*Math.random());
		
	}

  	public String getQuestion() {   
  		return "What is "+firstNumber+" + "+secondNumber+" ? ";
  	}

  	public int getCorrectAnswer() {
  		return firstNumber+secondNumber;
   	}
}