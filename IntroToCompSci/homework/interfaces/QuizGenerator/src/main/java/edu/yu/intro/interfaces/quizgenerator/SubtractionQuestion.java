package edu.yu.intro.interfaces.quizgenerator; 

public class SubtractionQuestion implements IntQuestion{
	int firstNumber, secondNumber;

  	public SubtractionQuestion() { 
		//sets valu to 0-50
		firstNumber = (int)(51*Math.random());
		secondNumber = (int)(51*Math.random());
    //checks to see if the first number is smaller and switches
    //if it is
    if(secondNumber > firstNumber){
      int copyFirst = firstNumber;
      firstNumber = secondNumber;
      secondNumber = copyFirst;
    }
		
	}

  	public String getQuestion() {   
  		return "What is "+firstNumber+" - "+secondNumber+" ? ";
  	}

  	public int getCorrectAnswer() {
  		return firstNumber-secondNumber;
   	}
}