package edu.yu.intro.objects.rational;
import java.io.*;

public class Rational{
	int numerator, denominator;

	public Rational(int num, int den){
		numerator = num;
		denominator = den;
	}
	public Rational(){
		this(0,1);
	}
	//Write an instance method called printRational() that displays a Rational in some reasonable format
	public void printRational() {
		System.out.printf("%d/%d %n", numerator,denominator);
	}
	public int getNumerator(){
		return numerator;
	}
	public int getDenominator(){
		return denominator;
	} 
	public static void main(String[] args) {
		Rational rat1 = new Rational();
		rat1.printRational();
		
		Rational rat2 = new Rational(3,2);
		rat2.printRational();

		rat2.negate();//make negative
		rat2.printRational();

		rat2.invert();//flip num and denom
		rat2.printRational();
		System.out.printf("The double version of %d/%d is: %f %n", rat2.getNumerator(), rat2.getDenominator(), rat2.toDouble());

		Rational rat3 = new Rational(20,24);
		rat3.printRational();

		rat3 = rat3.reduce();
		rat3.printRational();

		rat3 = rat3.add(new Rational(3,4));
		rat3.printRational();

		rat3 = rat3.add(new Rational (0,23));
		rat3.printRational();		
		
	}
	//makes the fracction negative
	public void negate(){
		numerator *= -1;
	}
	//flips the numerator and denominator
	public void invert(){
		int num = numerator;
		numerator = denominator;
		denominator = num;
	}
	public double toDouble(){
		return ((double)numerator / denominator);
	}
	public Rational reduce(){
		if(numerator==0){
			return new Rational(0,denominator);
		}
		int num = numerator;
		int den = denominator;
		while(den>0){
			//using Euclideans algorithm 
			//i is a holding variable for the numerator
			int i = num;
			num = den;
			den = i%den;
		}
		//num is the gcd
		return new Rational(numerator/num, denominator/num);
	}
	//adds two fractions 
	public Rational add(Rational rat){
		if(rat.getDenominator()==0){
			throw new IllegalArgumentException("You tried to add a number with 0 as the denominator");
		}
		if(this.getDenominator()==0){
			throw new IllegalStateException("You tried to add a number with 0 as the denominator");
		}
		int newDen = this.denominator*rat.getDenominator();
		int newNum = this.numerator*rat.getDenominator()+rat.getNumerator()*this.denominator;
		Rational newRat = new Rational(newNum,newDen);
		return newRat.reduce();
	}


}