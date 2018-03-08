package hw2;
import java.io.*;

public class TextSearcher {
	private static String dir,extension,searchString;
	File directory;
		
	public TextSearcher(){}

	public static void main(String[] args) throws Exception {
		//check to make sure there are only three arguments
		TextSearcher txtSearch = new TextSearcher();
		//set dir,extension, and searchString
		txtSearch.setStrings(args);
		txtSearch.makeFile(); 
		//check
		txtSearch.run();
	}
	
	//sets the variables dir, searchString, and extension to the corresponding inputed values
	//@param args, the input from the command line
	//@return void
	//@throws IllegalArgumentException, if the argument isn't the correct size, or has bad input
	public void setStrings(String[] args) throws IllegalArgumentException{
		//check to make sure only three arguments
		checkInput(args, 3);
		for(String str: args) {
			if(str.startsWith("dir=")){
				dir = str.substring(str.indexOf("=")+1);
			}
			else if(str.startsWith("string=")) {
				searchString = str.substring(str.indexOf("=")+1);
			} else 
				if(str.startsWith("extension=")) {
					extension = str.substring(str.indexOf("=")+1);
				}
				else {
					System.out.println("Bad Input");
					throw new IllegalArgumentException("Bad Input");
				}
				
		}
		
	}
	
	//checks to make sure inputed array is the right size
	private <T> void checkInput(T[] args, int x) {
		if( args.length != x) {
			System.out.println("You must input exaclty " + x + " arguments");
			throw new IllegalArgumentException("You must input exaclty " + x + " arguments");
		}
	}
	
	//sets the directory to a File cased on the input dir
	public void makeFile() {
		directory = new File(dir);
	}
	
	//vefrifies that directory is valid, then proceeds to call scanDirectory
	public void run() throws Exception{
		if(!directory.isDirectory()) {
			System.out.println("dir must be a valid directory");
			throw new IllegalArgumentException("dir must be a valid directory");
		}
		scanDirectory(directory);
	}
	
	//checks to see if the input is a file or directory and proceeds with appropiate action
	private void scanDirectory(File directory) throws Exception {//check for exceptions
		File[] files = directory.listFiles();//retrieve list of all the files/directories in the current path
		//if dir, call again
		for(File file: files) {
			if(file.isDirectory()) {
				scanDirectory(file);
			}
			else{
				searchFile(file);
			}
		}
	}
	
	/* Reads a given file line by line for the searchString
	 * and print out any finds
	 * @param file, the file to be searched
	 */
	private void searchFile(File file) throws Exception {
		if(file.getName().endsWith(extension)){//verify the file has the right extension
			//read each line and check for the searchString
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {//as long as the file contains more lines
				checkLine(file, line, 0, lineNumber);
				lineNumber++;
			}
			br.close();
		}
	}
	
	//checks the given line for every given appearence of the searchString and prints out what it finds.
	private void checkLine(File file, String line, int startPosition, int lineNumber) {
		if(startPosition >= line.length()) {//base case
			return;
		}
		String newLine = line.substring(startPosition);
		if(newLine.contains(searchString)) {
			int index = line.indexOf(searchString,startPosition); 
			System.out.printf("%s: line %d, character %d %n", file.getAbsolutePath(), lineNumber, index);
			checkLine(file, line, index+1, lineNumber);
		}
	}
}
