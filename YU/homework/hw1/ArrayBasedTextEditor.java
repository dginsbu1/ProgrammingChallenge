package hw1;

public class ArrayBasedTextEditor { 
	private Character[][] lines;
	private int currentLine; 
	private int currentPosition;
	
	public ArrayBasedTextEditor() { 
		this.lines = new Character[1][1]; 
		this.currentLine = this.currentPosition = 0;
		
	}
	//Notes: Only addChar can at at end of line
	//only newLine can create a new line at the end

	//inserts character at given x,y
	//test: xNegative/notInRange, yNegative/notInRange, arrayFull 
	public void insertChar(Character c, int x, int y){
		//verifies C isn't null
		if(c == null){
			System.out.println("I'm sorry, but the character can't be null");
			throw new IllegalArgumentException("I'm sorry, but the character can't be null");	
		}
		//checks x,y
		if(notValid(x,y) || (y != 0 && lines[x][y-1] == null) || (x == currentLine && y == currentPosition)){
			System.out.println("I'm sorry, but x and y must be in range");
			throw new IllegalArgumentException("I'm sorry, but x and y must be in range");
		}
		//shift all characters above given spot up one
		for(int i=lines[x].length-1; i > y; i--){
			lines[x][i] = lines[x][i-1];
		}
		//replace character at x,y
		lines[x][y] = c;
		//adjust currentPosition if needed
		if(x == currentLine){
			currentPosition++; 
		}
		//double size of lines[x] if full
		if(lines[x][lines[x].length-1] != null){
			lines[x] = doubleSize(lines[x]);
		}
	}
	//insert a line with a string after line x
	//test xNegative, xToHigh stringNull
	public void insertLine(String string, int x){
		//verifies that String isn't null
		if(string == null || string.equals("")){
			System.out.println("I'm sorry, but the string can't be null");
			throw new IllegalArgumentException("I'm sorry, but the string can't be null");	
		}
		//checks to make sure x is in range. -2 used so that x isn't the last row
		if(x < 0 || x > currentLine-1){
			System.out.println("I'm sorry, but x must be in range");
			throw new IllegalArgumentException("I'm sorry, but x must be in range");
		}
		currentLine++;
		//double the amount of all rows are filled
		if(currentLine == lines.length){
			lines = doubleSize(lines);
		}
		//shift all the rows up one to make room for the new line
		for(int i = lines.length-1; i > x+1; i--){
			lines[i] = lines[i-1];
		}
		//set each index of lines[x] to the character from string
		lines[x] = new Character[1];
		for(int i = 0; i < string.length(); i++){
			this.insertChar(string.charAt(i), x, i); ;
		}
		//double size if necessary
		//set current position
		for(int i = 0; i < lines[currentLine].length; i++){
			if(lines[currentLine][i] == null){
				currentPosition = i;
				break; 
			}
		}		
	}
	//adds new line
	//test firstLine, normal
	public void newLine() { 
		this.currentLine++; 
		this.currentPosition = 0;
		if(this.currentLine == this.lines.length) { 
		 	this.lines = doubleSize(this.lines); 
		} 

	}
	//delete a character at x,y
	//test xNotInRange, yNotInRange, Char null
	public void deleteChar(int x, int y){
		//verifies that x,y are valid
		if(notValid(x,y) || lines[x][y] == null){
			System.out.println("I'm sorry, but x and y must be in range");
			throw new IllegalArgumentException("I'm sorry, but x and y must be in range");
		}
		//shifts all characters above the deleted one down one
		for(int i=y; i < lines[x].length-1; i++){
			lines[x][i] = lines[x][i+1];
		}
		//decrements currentPosition of the deleted char was from the currentLine
		if(x == currentLine){
			currentPosition--;
		}
		//if the deleted character was the only one in the line, then delete the line
		if(lines[x][0] == null){
			this.deleteLine(x);
		}
		else{
			//cuts lines[x] in half if majority is empty by checking the middle character
			if(lines[x][lines[x].length/2 -1] == null ) { 
				lines[x] = halveSize(lines[x]);
			}
		} 
	}
	//delete line x
	//Test xNegative, xtoHigh, firstLine, lastLine, middleLine, StartedWithOneLine
	public void deleteLine(int x){
		//verifies x is in range
		if(x < 0 || x > currentLine){
			System.out.println("I'm sorry, but x must be in range");
			throw new IllegalArgumentException("I'm sorry, but x must be in range");
		}
		//if the only Line is deleted
		if(currentLine == 0) {
			lines[currentLine] = null;
			currentPosition = 0;
			return;
		}
		//shifts all rows above x down one
		for(int i = x; i < lines.length-1; i++){
			lines[i] = lines[i+1];
		}
		//delete last line
		lines[currentLine] = new Character[1];
		//Decrement current line due to deleted line
		currentLine--;
		//reset current position
		for(int i = 0; i < lines[currentLine].length; i++){
			if(lines[currentLine][i] == null){
				currentPosition = i;
				break; 
			}
		}		
		//Halve the amount of rows if needed
		if(currentLine < lines.length/2-1) { 
			lines = halveSize(lines);
		} 
	}


	//returns array of Characters with same contents as original but half the size.
	//Assumes array is majority empty.
	private Character[] halveSize(Character[] original){
		Character[] halved = new Character[original.length/2];
		for(int i = 0; i < original.length/2-1; i++){
			halved[i] = original[i];
		}
		return halved;
	}
	private Character[][] halveSize(Character[][] original){
		Character[][] halved = new Character[original.length/2][1];
		for(int i = 0; i <original.length/2-1; i++){
			halved[i] = original[i];
		}
		return halved;
	}

	//verify x,y are non negative and not higher then the current positions
	private boolean notValid(int x, int y){
		return (x < 0 || x >= lines.length || y < 0 || y >= lines[x].length);
	}
	//replace a character at x,y
	//test: xNegative/notInRange, yNegative/notInRange, Char isn't null 
	public void replaceChar(Character c, int x, int y){
		//verifies c isn't null
		if(c == null){
			System.out.println("I'm sorry, but the character can't be null");
			throw new IllegalArgumentException("I'm sorry, but the character can't be null");	
		}
		//verifies x,y are in range
		if(notValid(x,y) || lines[x][y] == null){
			System.out.println("I'm sorry, but x and y must be in range");
			throw new IllegalArgumentException("I'm sorry, but x and y must be in range");
		}
		//replaces Character c
		lines[x][y] = c;
	}
						//ICC2
	//adds character
	//test CharNotNull, started empty, neededToDouble, normal
	public void addChar(Character c) { 
		if(c == null){
			System.out.println("I'm sorry, but the character can't be null");
			throw new IllegalArgumentException("I'm sorry, but the character can't be null");	
		}
		this.lines[this.currentLine][this.currentPosition] = c; 
		this.currentPosition++;
		if(this.currentPosition == this.lines[this.currentLine].length) { 
			this.lines[this.currentLine] = doubleSize(this.lines[this.currentLine]); 
		} 
	}
		//doubles length of current line    
	private Character[] doubleSize(Character[] original) { 
		Character[] doubled = new Character[original.length * 2];
		for(int i = 0; i < original.length; i++) { 
			doubled[i] = original[i]; 
		} 
		return doubled; 
	}
	//doubles number of lines
	private Character[][] doubleSize(Character[][] original) { 
		Character[][] doubled = new Character[original.length * 2][1];
	 	for(int i = 0; i < original.length; i++) { 
	 		doubled[i] = original[i]; 
	 	} 
	 	return doubled;
	}
	public Character[][] getArray(){
		return lines;
	}
	public void getPosition() {
		System.out.println(currentLine + " " + currentPosition);
	}
	public void printString(){
		Character[][] arrayTest = lines;
		for(int i = 0; i < arrayTest.length; i++ ){
			if(arrayTest[i] == null) {
				System.out.println("nill");
			}
			else{
				for(int j=0; j < arrayTest[i].length;j++){
				System.out.print(arrayTest[i][j]);	
				}
			}
			System.out.println("");
		}	
	}
	
}