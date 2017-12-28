package edu.yu.intro.objects.library4;
import java.util.*;
import java.io.*;


public class Library
{
	private ArrayList<Book> books;
	private Set<BookInstance> bookInstances;
	private String name, address, phoneNumber;
	private Set<Patron> patrons; 
	//private Map<Patron, BookInstance> loans;

	
	public static void main(String[] args) throws NotOnLoanException, OnLoanException{

		//throw new OnLoanException("test");
		throw new NotOnLoanException("test");
	}
	

	public Library(String name, String address, String phone){
		if(name == null || address == null || phone == null || name.length() == 0 || phone.length() == 0 || address.length() == 0){
			throw new IllegalArgumentException("All variables must have at least length one");
		}
		this.name = name;
		this.address = address;
		this.phoneNumber = phone;
		this.books = new ArrayList<Book>();
		this.bookInstances = new HashSet<>();
		//loans = new HashMap<>();
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

	//test bothNull, patronNull or notMember,
	//bookNull or notInLibrary, bookOnLoan, bookOnReserve 
	public void borrow(Patron patron, BookInstance book) throws OnLoanException{
		if(patron == null || !patrons.contains(patron) || book == null || !bookInstances.contains(book)){
				throw new IllegalArgumentException("Patron and Book must be instantiated");		
		}
		book.borrow(patron);
	}
	//test bothNull, patronNull or notMember,
	//bookNull or notInLibrary, bookOnLoan, bookOnReserve 
	public boolean reserve(Patron patron, Book book){
		if(patron == null || !patrons.contains(patron) || book == null || !books.contains(book)){
				throw new IllegalArgumentException("Patron and Book must be instantiated");		
		}
		for(BookInstance bI: bookInstances){
			if(bI.getBook().equals(book) && bI.getLoanState().equals(LoanState.AVAILABLE)){
				bI.reserve(patron);
				return true;
			}
		}
		return false;
	}
	//Test: patronNull, patronNotMember, bookNull, bookNotThere, wrongPatron, normal
	public void returnInstance(Patron patron, BookInstance book) throws NotOnLoanException{
		if(patron == null || !patrons.contains(patron) || book == null || !bookInstances.contains(book)){
				throw new IllegalArgumentException("Patron and Book must be instantiated");		
		}
		Patron p = book.getPatron();
		if(book.getLoanState() != LoanState.BORROWED || p == null || !p.equals(patron)){
			throw new NotOnLoanException("This book isn't on loan to the specified patron.");
		}
		//book = new BookInstance(book.getBook());
		book.returnInstance(patron);
	}


	//test null, notMember, noBook, yesBook(s)
	public Set<BookInstance> onLoan(Patron patron){
		if(patron == null || !patrons.contains(patron)){
			throw new IllegalArgumentException("Patron must be instantitated and part of the library");			
		}
		//check
		Set<BookInstance> bookSet = new HashSet<>();
			for(BookInstance book: bookInstances){
				if(book.getLoanState().equals(LoanState.BORROWED) && book.getPatron().equals(patron)){
					bookSet.add(book);
			}
		}
		return bookSet;
	}
	//test: patronNull, patronNotMember, noReserved, oneReserved, multipleReserved 
	public Set<BookInstance> onReserve(Patron patron){
		if(patron == null || !patrons.contains(patron)){
			throw new IllegalArgumentException("Patron must be instantitated and part of the library");			
		}
		Set<BookInstance> bookSet = new HashSet<>();
			for(BookInstance book: bookInstances){
				if(book.getLoanState().equals(LoanState.ON_RESERVE) && book.getPatron().equals(patron)){
					bookSet.add(book);
			}
		}
		return bookSet;
	}
	//test: bookNull, bookNotThere, zeroSet, oneSet, multipleSet
	public Set<BookInstance> getInstances(Book book){
		if(book == null){
			throw new IllegalArgumentException("Book must be instantitated");			
		}
		Set<BookInstance> bookSet = new HashSet<>();
		for(BookInstance bookInstance: bookInstances){
			if(bookInstance.getBook().equals(book)){
				bookSet.add(bookInstance);
			}
		}
		return bookSet;
	}

	//test cases: null, nbook already there, book not there.
	//replace
	public BookInstance add(Book book){
		if(book == null){
			throw new IllegalArgumentException("Book can't be null");
		}
		BookInstance bookInstance = new BookInstance(book);
		if(!books.contains(book)){
			books.add(book);
		}
		bookInstances.add(bookInstance);
		bookInstance.setLibrary(this);
		return bookInstance;
	}

	//Test: BINull, BINormal
	public boolean isInHoldings(BookInstance bookInstance){
		if(bookInstance == null){
			throw new IllegalArgumentException("BookInstance can't be null");
		}
		return (bookInstances.contains(bookInstance));
	}

	//
	public Collection<Book> allBooks(){
		final Collection<Book> allLibraryBooks = books;
		return allLibraryBooks;
	}


	//test nullFilter, empty filter, no match, one match, multiple match 
	public Collection<Book> search(final BookFilter filter){
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