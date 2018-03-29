package hw4;

public class ArrayBasedTextEditor { 
	private ArrayStack<ArrayStack<Character>> lines;
	
	public ArrayBasedTextEditor() { 
		this.lines = new ArrayStack<ArrayStack<Character>>(); 
	}
	//Notes: Only addChar can at at end of line
	//only newLine can create a new line at the end

	//adds character
	//test CharNotNull, started empty, neededToDouble, normal
	public void addChar(Character c) throws IllegalAccessException{ 
		if(c == null){
			System.out.println("I'm sorry, but the character can't be null");
			throw new IllegalArgumentException("I'm sorry, but the character can't be null");	
		}
		lines.peek().push(c);
	}
	
	//adds new line
	//test firstLine, normal
	public void newLine() { 
		lines.push(new ArrayStack<Character>());
	}
	
	//replace a character at x,y
	//test: xNegative/notInRange, yNegative/notInRange, Char isn't null 
	public void replaceChar(Character c, int x, int y) throws IllegalAccessException{
		modifyChar(c, x, y, "replace");
	}	
	
	//inserts character at given x,y
	//test: xNegative/notInRange, yNegative/notInRange, arrayFull 
	public void insertChar(Character c, int x, int y) throws IllegalAccessException{
		modifyChar(c, x, y, "insert");
	}
	
	//basically replace but without the char
	//test xNotInRange, yNotInRange, Char null
	public void deleteChar(int x, int y) throws IllegalAccessException{
		modifyChar(null, x, y, "delete");
	}
	
	/*make sure x is valid
	pop off the stacks until you get to x
	verify y is valid
	pop off the elements until you get y. 
	replace
	put all elements back
	put all stacks back
	*/
	private ArrayStack<T> popToTemp(ArrayStack<T> original, int x){
		ArrayStack<T> outerTemp = (ArrayStack<T>)(new ArrayStack<Object>());
		for(int i = original.size()-1; i > x; i--) {
			outerTemp.push(original.pop());
		}
	private pushAll(ArrayStack<T> original, ArrayStack<T> temp){
			//put all elements back
		while(temp.size() > 0) {
			original.push(temp.pop());
		}
	}
	private void modifyChar(Character c, int x, int y, String str) throws IllegalAccessException, IllegalArgumentException{
		if(!str.equals("delete") && c == null){
			System.out.println("I'm sorry, but the character can't be null");
			throw new IllegalArgumentException("I'm sorry, but the character can't be null");	
		}
		if(notValid(lines, x)){
			System.out.println("I'm sorry, but x must be in range");
			throw new IllegalArgumentException("I'm sorry, but x must be in range");
		}
		//pop off the stacks until you get to x
		ArrayStack<ArrayStack<Character>> outerTemp = popToTemp(lines,x);
		//verify y is valid
		if(notValid(lines.peek(), y)){
			System.out.println("I'm sorry, but y must be in range");
			throw new IllegalArgumentException("I'm sorry, but y must be in range");
		}
		ArrayStack<Character> currentLine = lines.peek();
		//pop off the elements until you get y.
		ArrayStack<Character> innerTemp = popToTemp(currentLine, y);

		//depending on the method
		if(str.equals("insert")) {
			currentLine.push(c);
		}
		else if(str.equals("replace")) {
			currentLine.pop();
			currentLine.push(c);
		}
		else if (str.equals("delete")) {
			currentLine.pop();
		}
		//put all elements back
		pushAll(currentLine, innerTemp);
		//put all stacks back
		pushAll(lines, outerTemp);
	}

	private boolean notValid(ArrayStack<T> stack, int z) {
		return (z < 0 || z >= stack.size());
	}
	
	//insert a line with a string after line x
	//test xNegative, xToHigh stringNull
	public void insertLine(String string, int x){
		//verifies that String isn't null
		if(string == null || string.equals("")){
			System.out.println("I'm sorry, but the string can't be null");
			throw new IllegalArgumentException("I'm sorry, but the string can't be null");	
		}
		//checks to make sure x is in range. -1 used so that x isn't the last row
		if(x < 0 || x >= lines.size()-1){
			System.out.println("I'm sorry, but x must be in range");
			throw new IllegalArgumentException("I'm sorry, but x must be in range");
		}
		/*
		 * pop all the rows until the specified row into a tempStack.
		 * push the new string as a stack of characters
		 * push all the rows from the tempStack back onto the realStack
		 */
		ArrayStack<Character> currentLine = lines.peek();
		//pop all the rows until the specified row into a tempStack.
		ArrayStack<ArrayStack<Character>> outerTemp = popToTemp(lines, x);
		//set each index of lines[x] to the character from string
		lines.push(new ArrayStack<Character>(string.length()));
		for(int i = 0; i < string.length(); i++){
			this.addChar(string.charAt(i));//maybe use this.push() instead
		}
		//push all the rows from the tempStack back onto the realStack
		pushAll(lines, outerTemp);
	}
	
	//delete line x
	//Test xNegative, xtoHigh, firstLine, lastLine, middleLine, StartedWithOneLine
	public void deleteLine(int x){
		//verifies x is in range
		if(x < 0 || x >= lines.size()){
			System.out.println("I'm sorry, but x must be in range");
			throw new IllegalArgumentException("I'm sorry, but x must be in range");
		}
		ArrayStack<ArrayStack<Character>> outerTemp = popToTemp(lines, x);
		//delete current line
		lines.pop();
		//replace lines
		pushAll(lines, outerTemp);
	}

	public void reverse(int x){
		if(notValid(x)){
			System.out.println("I'm sorry, but x must be in range");
			throw new IllegalArgumentException("I'm sorry, but x must be in range");
		}
		//pop off the elements until the desired line
		ArrayStack<ArrayStack<Character>> outerTemp = popToTemp(lines, x);
		//pop all the Characters from the stack thus reversing the order
		//ArrayStack<Character> currentLine = lines.pop();
		ArrayStack<Character> reverse = ArrayStack<Character>();
		pushAll(reverse, lines.pop());
		lines.push(reverse);
		pushAll(lines, outerTemp);
	}
	public void reverseLines(){
		ArrayStack<ArrayStack<Character>> temp = new ArrayStack<ArrayStack<Character>>(lines.size());
		//push the entire stack to the temp stack thus reversing it
		pushAll(temp, lines);
		lines = temp;
	}
	public void print(){
		printLines(lines);
	}
	private void printLines(ArrayStack<ArrayStack<Character>> stack){
		if(stack.size() == 1){
			printRow(stack);
		}
		else{
			ArrayStack<Character> row = stack.pop();
			printLines(stack);
			prinRow(row);

		}
	}
	private void printRow(ArrayStack<Character> row){
		if(row.size() == 1){
			System.out.print(row.pop());
		} 
		else{
			Character c = row.pop()
			printRow(row);
			System.out.print(c);
		}
	}


		//verify x,y are non negative and not higher then the current positions
	private boolean notValid(int x, int y){
		return (x < 0 || x >= lines.size() || y < 0 || y >= lines.peek());
	}
		
	public ArrayStack<ArrayStack<Character>> getArray(){
		return lines;
	}
	
	public void getPosition() {
		System.out.println(currentLine + " " + currentPosition);
	}
	
	public void printString(){
		ArrayStack<ArrayStack<Character>> arrayTest = lines;
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