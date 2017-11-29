package edu.yu.intro.strings;

public class StringUtils{
	public static void main(String[] args) {
		/*
		System.out.println(extractExtension(null));//error
		System.out.println(extractExtension("asda.sasdas.asd"));//error
		System.out.println(extractExtension(""));//empty
		System.out.println(extractExtension("fsd"));//empty
		System.out.println(extractExtension(" s ssd.java"));//java
		System.out.println(extractExtension("sdfs."));//""
		System.out.println(extractExtension("dasdasas.dfsdygsd"));//dfsdygsd
		System.out.println(extractExtension("ftyu.   g"));//empty
		System.out.println(extractExtension("ftyu."));*/
		System.out.println(isNotEmpty(null));
		System.out.println(isNotEmpty(""));
		System.out.println(isNotEmpty("  "));
		System.out.println(isNotEmpty("asd"));
	}
	
//test case:null, empty, one letter, normal not, normal yes
	public static boolean isPalindrome(String s){
		if(s == null){
			return false;
		}
		int length = s.length();
		for(int i = 0; i < length/2; i++){
			if(s.charAt(i) != s.charAt(length-1-i)){
				return false;
			}
		}
		//if the entire string(besides the middle letter) has been traversed and all the letters
		//matched it is a palindrome
		return true;
	}

	//test case:null, empty, nodot, only dot, nothing after dot, two dot, Happy
	//empty string when the input does not contain 
	//any "dot". If the input is null, or if multiple 
	//"dots" are present in the input, throw a RuntimeException 
	public static String extractExtension(String filename){
		String ext = "";
		if(filename == null){
			throw new RuntimeException("the input must have exactly one dot.");
		}
		if(!filename.contains(".")){
			return ext;
		}
		int dotPos = filename.indexOf(".");
		//if there is more than one dot
		if(dotPos != filename.lastIndexOf(".")){
			throw new RuntimeException("the input must have exactly one dot.");
		}
		return filename.substring(dotPos+1,filename.length());
	}

	//Return true if the input's length is non-zero, false otherwise 
	//test cases null, empty spaces, norm
	//double check null case
	public static boolean isNotEmpty(String str){
		return !(str == null || str.length() == 0);
	}

	//Return true iff the input is null, empty,
	//or contains only whitespace; false otherwise 
	//test cases null, empty, spaces, norm

	public static boolean isBlank(String str){
		return (!isNotEmpty(str) || trimWhitespaceTillEmpty(str).length() == 0);
	}

	//test case both null, one null, two different, two same
	public static boolean equals(String str1, String str2){
		//if both are null, they are equal
		if(str1 == null && str2 == null){
			return true;
		}
		//if only one is null, they are not equal
		if(str1 == null || str2 == null){
			return false;
		}
		//if both are not null then actually compare
		return(str1.equals(str2));
	}
	//join all the objects in the array
	//test case: null, array with null and empty, normal
	public static String join(Object[] array){
		if(array == null){
			throw new RuntimeException("Sorry, input can't be null.");
		}
		String fullString = "";
		for(Object element:array){
			if(element == null){
				continue;
			}
			fullString = fullString+element.toString();
		}
		return fullString;
	}

	//testcases: nullString, negative len, len bigger than 20, len smaller than str, negative len
	public static String leftPad(String str, int len){
		if(str==null){
			return null;
		}
		if(len >=20 ){
			len = 20;
		}	
		//if(str.length()
		String newString = str;
		for(int i = len-str.length(); i>0;i--){
			newString= " " + newString;
		}
		return newString;
	}
	//testase: null, empty, just space, normal with space, normal without space
	public static String trimWhitespaceTillEmpty(String str){
		if(str == null){
			return "";
		}
		return str.trim();
	}
	//Remove all occurrences of the specified character 
	//from the input String. A null source string will
	//return null; an empty ("") source string will 
	//return the empty string. 
	//testase: null, empty, just space, 
	//normal with space, normal without space
	 public static String remove(String str, char removeIt){
	 	if(str == null){
	 		return null;
	 	}
	 	String shortString = "";
	 	for(int i = 0; i<str.length(); i++){
	 		char char1 = str.charAt(i);
	 		if(char1 != removeIt){
	 			shortString = shortString+char1;
	 		}
	 	}
	 	return shortString;
	 }
}


