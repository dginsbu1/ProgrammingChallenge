package edu.yu.intro.objects.library4;

public class NotOnLoanException extends Exception{
	//private Exception exception;
	//test: null, normal
	//use when notOnLoan or notOnLoan to specific patron
	public NotOnLoanException(String message){
		super(message);
	}

}