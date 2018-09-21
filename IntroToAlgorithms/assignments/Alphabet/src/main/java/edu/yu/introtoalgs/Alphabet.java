package edu.yu.introtoalgs;
import java.util.*;
import java.lang.Math;
public class Alphabet{
	final int R;
	ArrayList<Character> alphabet;
	//create new alphabet from chars in s
	public Alphabet(String s){
		if(s.length()==0){
			throw new IllegalArgumentException("Alphabet must be at least one character");
		}
		R = s.length();
		alphabet = new ArrayList<Character>(R);
		for(int i =0; i < s.length(); i++){
			alphabet.add(s.charAt(i));
		}
	}
	//convert index to corresponding alphabet char
	//Test: negative, to high
	public char toChar(int index){
		if(index < 0 || index >= R){
			throw new IndexOutOfBoundsException("The index "+index+" is out of range");
		}
		return alphabet.get(index);
	}
	//convert c to an index in between 0 and R-1
	//Test: c not in alphabet
	public int toIndex(char c){
		if(!this.contains(c)){
			throw new IllegalArgumentException("char "+c+" is not in the alphabet");
		}
		return alphabet.indexOf(c);
	}
	//is C in the alphabet
	public boolean contains(char c){
		return alphabet.contains(c);
	}
	//radix(number of characters in the alphabet)
	public int radix(){
		return R;
	}
	//number of bits corresponding to an index
	public int lgR(){
		if(R == 0 || R == 1){
			return R;
		}
		//DOUBLE CHECK
		return (int)(Math.ceil(Math.log(R)/Math.log(2)));
	}
	//converts s to base-R integer
	public int[] toIndices(String s){
		//FINISH
		int[] indices = new int[s.length()];
		for(int i =0; i < s.length(); i++){
			indices[i] = this.toIndex(s.charAt(i));
		}
		return indices;
	}
	//convert base-R integer to string over this alphabet
	public String toChars(int[] indices){
		String s = "";
		for(int index: indices) {
			s += this.toChar(index);
		}
		return s;
	}

	//public static void main(String args[]){
	// 	Scanner scanner = new Scanner(System.in);
 	//Alphabet alphabet = new Alphabet("");//ABCDE
	// 	for(int i: alphabet.toIndices("aebdc")){
	// 		System.out.print(i);
	// 	}
	// 	int[] num = {-1, 4,3,2,1,0,0,0,0,0,1,3,4,2,2};
	// 	System.out.println(alphabet.toChar(num));
	// 	//int R = alphabet.radix();//5
	// 	//char c= alphabet.toChar(9);//B
	// 	//int i = alphabet.toIndex('!');//0
	// 	//boolean yes = alphabet.contains('C');//true
	// 	///boolean no = alphabet.contains('F');//false
	// 	//int log = alphabet.lgR();//3
	// 	//for(int i=0; i<9999999; i++){
	// 	//		System.out.println(i+ " " +(int)(Math.ceil(Math.log(i)/Math.log(2))));
	// 	//}
	// 	//System.out.println(R+" "+c+" "+i+" "+yes+" "+no+
	// 	//	" "+log+" ");
	//}
}