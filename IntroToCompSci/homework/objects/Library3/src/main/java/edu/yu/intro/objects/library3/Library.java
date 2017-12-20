package edu.yu.intro.objects.library3;
import java.util.*;
import java.io.*;


public class Library
{
	private ArrayList<Book> books;
	private String name, address, phoneNumber;
	private Set<Patron> patrons; 
	private Map<Patron, Book> loans;

	
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
	


	public Library(String name, String address, String phone){
		if(name == null || address == null || phone == null || name.length() == 0 || phone.length() == 0 || address.length() == 0){
			throw new IllegalArgumentException("All variables must have at least length one");
		}
		books = new ArrayList<Book>();
		this.name = name;
		this.address = address;
		this.phoneNumber = phone;
		loans = new HashMap<>();
		patrons = new HashSet<>();
	}

	public String getName(){
		return name;
	}

	public String getAddress(){
		return address;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}
	public ArrayList<Book> getAllBooks(){
		return books;
	}
	public Set<Patron> getPatrons(){
		return patrons;
	}

	//test both null, patron null or not member,
	//book null or not in library 
	public void borrow(Patron patron, Book book){
		if(patron == null || !patrons.contains(patron) || book == null || !books.contains(book)){
				throw new IllegalArgumentException("Patron and Book must be instantiated");		
		}
		book.setPatron(patron);
	}
	//test null, not member, member no book, member yes book(s)
	public Collection onLoan(Patron patron){
		if(patron == null || !patrons.contains(patron)){
			throw new IllegalArgumentException("Patron must be instantitated and part of the library");			
		}
		//check
		Collection<Book> bookCollection = new ArrayList<>();
		for(Book book:books){
			for(Patron p:book.getPatrons()){
				if(p.equals(patron)){
					bookCollection.add(book);
					break;
				}
			}
		}
		return bookCollection;
	}
	//test nullFilter, empty filter, no match, one match, multiple match 
	public Collection search(final BookFilter filter){
		if(filter == null){
			throw new IllegalArgumentException("Filter can't be null");			
		}
		List<Book> matches = new ArrayList<>();
		for(Book book: books){
			if(filter.filter(book)){
				matches.add(book);
			}
		}
		return matches;
	}
	//Adds the specified patron to the container
	//test:null, normal,
	public void add(Patron patron){
		if(patron == null){
			throw new IllegalArgumentException("Patron can't be null.");
		}
		patrons.add(patron);
	}
	//Removed all patrons from the container
	//test: see if it is empty after run
	public void clear(){
		patrons.clear();
		for(Book b: books){
			b.getPatrons().clear();
		}
	}
	//test, null, there, not there
	public Patron get(String uuid){
		if(uuid == null || uuid.equals("")){
			throw new IllegalArgumentException("UUID must be at least length one.");
		}
		for(Patron patron : patrons){
			if(patron.getId().equals(uuid)){
				return patron;
			}
		}
		return null;
	}

	//Returns the number of stored Patrons.
	//make sure number is right
	public int nPatrons(){
		return patrons.size();
	}

	//test case: null, empty, one letter, mutiple letters, one match, no match
	//substring to long, what if none match
	public Set<Patron> byLastNamePrefix(String prefix){
		if(prefix == null){
			throw new IllegalArgumentException("Prefix can't be null.");
		}
		Set<Patron> matchingPatrons = new HashSet<>();
		//if prefix is "" return all the patrons
		if(prefix.equals("")){
			for(Patron patron : patrons){
				matchingPatrons.add(patron);
			}
			return matchingPatrons;
		}

		for(Patron patron : patrons){
			String patronLN = patron.getLastName(); 
			if(patronLN.length() < prefix.length()){
				continue;
			}
			if(prefix.equals(patronLN.substring(0,prefix.length()))){
				matchingPatrons.add(patron);
			}
		}
		return matchingPatrons;
	}


	//returns the amount of books in library
	public int nBooks(){
		return books.size();
	}
	//test cases: null, nbook already there, book not there.
	public void add(Book b){
		if(b == null){
			throw new IllegalArgumentException("Book can't be null");
		}
		books.add(b);
	}

	@Override
	public int hashCode(){
		return Objects.hash(name);
	}

	@Override 
	public boolean equals(Object that){
		if(this == that){
			return true;
		}
		if(that == null){
			return false;
		}
		if(getClass() != that.getClass()){
			return false;
		}
		Library thatLibrary = (Library)(that);
		return name.equals(thatLibrary.getName());	
	}

	@Override
	public String toString(){
		return "Name: "+name+" Address: "+address+" Phone Number: "+phoneNumber;
	}
}