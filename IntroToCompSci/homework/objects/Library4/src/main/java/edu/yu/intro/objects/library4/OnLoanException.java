package edu.yu.intro.objects.library4;

public class OnLoanException extends Exception{
	//private Exception exception;
	//test: null, normal
	//use when onLoan or onReserve
	public OnLoanException(String message){
		super(message);
	}

}