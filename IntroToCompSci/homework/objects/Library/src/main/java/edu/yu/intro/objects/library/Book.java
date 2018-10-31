package edu.yu.intro.objects.library;
import java.util.Objects; 
import java.io.*;

public class Book
{
	private String title, author, type;
	private long isbn;	
	
	public Book(String title, String author, Long isbn, String type)
	{
		if(title == null || title.equals(""))
		{
			throw new IllegalArgumentException("Book title must have at least length one");
		}
		if(author == null || title.equals(""))
		{
			throw new IllegalArgumentException("Book title must have at least length one");
		}
		this.title = title;

		this.author = author;
		//checks to make sure ISBN is exactly 13 digits long
		if(!validISBN(isbn))
		{
			throw new IllegalArgumentException("ISBN must be 13 digits long");
		}
		this.isbn = isbn;
		//checks to make sure input is one of the three choices
        switch (type) 
        {
            case "hardcover":  
            	this.type = "hardcover";
                break;
            case "paperback":
           		this.type = "paperback";
                break;
            case "ebook":
           		this.type = "ebook";
                break;
            default:
            	throw new IllegalArgumentException("Book type must be hardcover, softcover, or ebook.");
         }
	}
	//getters
	public String getTitle()
	{
		return title;
	}
	public String getAuthor()
	{
		return author;
	}
	public String getBookType()
	{
		return type;
	}
	public long getISBN13()
	{
		return isbn;
	} 	

	//personal checker method
	public static boolean validISBN(long isbn)
	{
		String isbnString = ""+isbn;
		return (isbnString.length()==13);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object that)
	{
		if(this == that)
		{
			return true;
		}
		if(that == null)
		{
			return false;
		}
		if(getClass() != that.getClass())
		{
			return false;
		}
		Book thatBook = (Book)(that);
		return isbn == thatBook.getISBN13();
	}

	@Override
	public String toString()
	{
		return "Title: "+title+" Author: "+author+" ISBN: "+isbn+" Type: "+type;
	}
}