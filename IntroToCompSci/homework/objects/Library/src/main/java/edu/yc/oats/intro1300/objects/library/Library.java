package edu.yc.oats.intro1300.objects.library;

public class Library{
	private Book[] books;
	private String name;
	private int numberOfBooks;

	public static void main(String[] args){
		Book firstBook = new Book("Cat in The Hat", "Dr. Seuss", "123-45", "hard-cover");
		Book secondBook = new Book("Hunger Games", "don't remember", "23", "soft-cover");
		Library library = new Library("library");
		library.addBook(firstBook);
		System.out.println(library.checkBook(secondBook));
		library.addBook(secondBook);
		System.out.println(library.checkBook(secondBook));
		}
	}


	public Library(String name){
		books = new Book[100];
		this.name = name;
		int numberOfBooks = 0; 
	}

	//returns the amount of books in library
	public int getBookNumber(){
		return numberOfBooks;
	}

	//adds book to library
	public void addBook(Book book){
		books[numberOfBooks] = book;
		numberOfBooks++;
	}
	//checks to see if book is in library
	public boolean checkBook(Book book){
		//returns true if any of the books matches the current book title
		for(int i = 0; i < numberOfBooks; i++){
			if(book.getTitle().equals(books[i].getTitle())){
				return true;
			}
		}
		//assuming no matches 
		return false;
	}
	
}