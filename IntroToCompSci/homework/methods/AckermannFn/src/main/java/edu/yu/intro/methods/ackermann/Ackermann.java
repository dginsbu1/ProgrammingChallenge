

package edu.yu.intro.methods.ackermann;
import java.util.Scanner;
import java.io.*;

public class Ackermann{
	public static long ackermann(long m, long n){
		if(m<0 || n<0){
			throw new IllegalArgumentException("Invalid input, numbers must be 0 or higher");
		}
		if(m==0){
			return n+1;
		}
		if(m>0 && n==0){
			return ackermann(m-1,1);
		}
		if(m>0 && n>0){
			return ackermann(m-1, ackermann(m,n-1));
		}
		//to assuage the compiler
		else
			return 0;

	}
}  