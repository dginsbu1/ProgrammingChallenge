package edu.yu.intro.objects.library;
import java.util.ArrayList;
import java.util.Objects;
import java.io.*;


public class Library
{
	
	private ArrayList<Book> books;
	private String name, address, phoneNumber;

	
	public static void main(String[] args){
		/*
		System.out.println(Book.validISBN(0000000L));
		System.out.println(Book.validISBN(0000000000001L));
		System.out.println(Book.validISBN(1000000000000L));
		System.out.println(Book.validISBN(0000010000000L));
		System.out.println(Book.validISBN(12342343L));
		System.out.println(Book.validISBN(0000000L));
		Book firstBook = new Book("Cat in The Hat", "Dr. Seuss", 123-45, "hard-cover");
		Book secondBook = new Book("Hunger Games", "don't remember", 23, "soft-cover");
		Book secondBook = new Book("", "don't remember", 23, "soft-cover");
		Library library = new Library("library");
		//library.addBook(firstBook);
		//System.out.println(library.checkBook(secondBook));
		library.addBook(secondBook);
		////System.out.println(library.checkBook(secondBook));*/
	}
	


	public Library(String name, String address, String phone)
	{
		if(name == null || address == null || phone == null || name.length() == 0 || phone.length() == 0 || address.length() == 0)
		{
			throw new IllegalArgumentException("All variables must have at least length one");
		}
		books = new ArrayList<Book>();
		this.name = name;
		this.address = address;
		this.phoneNumber = phone;
	}

	public String getName()
	{
		return name;
	}

	public String getAddress()
	{
		return address;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	//returns the amount of books in library
	public int nBooks()
	{
		return books.size();
	}
	//test cases: null, nbook already there, book not there.
	public void add(Book b)
	{
		if(b == null)
		{
			throw new IllegalArgumentException("Book can't be null");
		}
		books.add(b);
	}

	//Is the specified book in the library's holdings
	//test cases: null, yes, no
	public boolean isTitleInHoldings(String title)
	{
		//add
		if(title == null || title.length() == 0)
		{
			throw new IllegalArgumentException("Title must be at least length one");
		}
		for(Book b : books)
		{
			if(b.getTitle().equals(title))
			{
				return true;
			}
		}
			//if it doesn't match any title, it is not there
			return false;
	}

	//Is the specified book in the library's holdings?
	//test case: null, yes, no
	public boolean isISBNInHoldings(long isbn13)
	{
		if(!Book.validISBN(isbn13))
		{
			throw new IllegalArgumentException("ISBN must be 13 digits long");
		}

		for(Book b: books)
		{
			if(b.getISBN13() == isbn13)
			{
				return true;
			}
		}
			//if it doesn't match any ISBN, it is not there
			return false;
	}

	//Return null if the specified Book is not in 
	//the holdings, the Book instance if it is.
	//test case: null, there, not there
	public Book getBook(long isbn13)
	{
		if(!Book.validISBN(isbn13))
		{
			throw new IllegalArgumentException("ISBN must be 13 digits long");	
		}
		for(Book b : books)
		{
			if(b.getISBN13() == isbn13)

			{
				return b;
			}
		}
		//if it doesn't match any ISBN, it is not there
		return null;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
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
		Library thatLibrary = (Library)(that);
		return name.equals(thatLibrary.getName());	
	}

	@Override
	public String toString()
	{
		return "Name: "+name+" Address: "+address+" Phone Number: "+phoneNumber;

	}
}