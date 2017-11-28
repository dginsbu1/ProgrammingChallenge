package edu.yc.oats.intro1300.objects.library;

public class Book{
	private String title, authors, isbn, type;
	

	public Book(String title, String authors, String isbn, String type){
		this.title = title;
		this.authors = authors;
		this.isbn = isbn;
		this.type = type;
	}
	//getters
	public String getIsbn(){
		return isbn;

	}
	public String getTitle(){
		return title;
	}

	
}