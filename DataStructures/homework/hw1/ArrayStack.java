package main;

import java.util.EmptyStackException;

@SuppressWarnings("unchecked")
public class ArrayStack<T> {
  private static final int DEFAULT_CAPACITY = 1;  
  private int top;
  private T[] stack;

  public ArrayStack(){
    this(DEFAULT_CAPACITY);
  }

  public ArrayStack(int size){
    top=0;
    stack = (T[])(new Object[size]);
  }

  //adds an additional element to the list
  //Test: full, empty, normal
  public void push(T element){
    if(size() == stack.length){
      expandCapacity(); 
    }
    stack[top]=element;
    top++;
  }
  
  //removes and returns the top of the stack
  //Test: full, empty, normal, onlyOneLeft
  public T pop() throws EmptyStackException{
    if(isEmpty()){
      System.out.println("The stack is empty");
      throw new EmptyStackException();
    }
    top--;
    T result = stack[top];
    stack[top] = null;
    return result;
  }
  
  //what to do if empty?
  //Test: empty, normal, nullSpace
  public T peek() throws EmptyStackException{
    if(isEmpty()){
      System.out.println("The stack is empty");
      throw new EmptyStackException();
    }
    return stack[top-1];
  }

  public int size() {
    return top;
  }
  //doubles size if full
  private void expandCapacity() {
    T[] doubled = (T[])(new Object[stack.length*2]);
    for(int i = 0; i < stack.length; i++) {
      doubled[i] = stack[i]; 
    } 
    stack = doubled; 
  }  
  private boolean isEmpty(){
    return top==0;
  }
  //FOR TESTING
  /*
  @Override
  public String toString(){
    if(stack == null){return "";}
    String str = "";
    for(T element: stack){
      if(element!= null) {
        str+= element;
      }
    }
      return str;
  }
  public T[] getArray(){
    return (stack);
  }*/

}
