package edu.yu.intro.objects.rational;

public class Rational{
	int numerator, denominator;

	public Rational(){
		numerator = 0;
		denominator = 1;
	}
	//Write an instance method called printRational() that displays a Rational in some reasonable format
	public void printRational() {
		System.out.printf("%d/%d", numerator,denominator);
	}
	public int getNumerator(){
		return numerator;
	}
	public int getDenominator(){
		return denominator;
	} 
	public static void main(String[] args) {
		Rational rat = new Rational();
		System.out.print("current rational: ");
		rat.printRational();
		Rational rat2 = new Rational(2,3);
		
		rat2.negate();
		System.out.print("\ncurrent rational: ");
		rat2.printRational();

		rat2.invert();
		System.out.print("\ncurrent rational: ");
		rat2.printRational();

		System.out.printf("%nThe double version is: %f", rat2.toDouble());
	}
	public Rational(int num, int den){
		numerator = num;
		denominator = den;
	}
	public void negate(){
		numerator *= -1;
	}
	public void invert(){
		int num = numerator;
		numerator = denominator;
		denominator = num;
	}
	public double toDouble(){
		return ((double)numerator / denominator);
	}
	public Rational reduce(){
		int num = numerator;
		int den = denominator;
		while(den>0){
			//i is a holding variable for the numerator
			int i = num;
			num = den;
			den = i%den;
		}
		return new Rational(num, den);
	}
	public Rational add(Rational rat){
		int newDen = this.denominator*rat.getDenominator();
		int newNum = this.numerator*rat.getDenominator()+rat.getNumerator()*this.numerator;
		Rational newRat = new Rational(newNum,newDen);
		return newRat.reduce();

	}


}