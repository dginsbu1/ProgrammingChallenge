package edu.yu.intro.objects.library4;
import java.util.*;
public class BookInstance{
	private Book book;
	private String id;
	private Patron patron;
	private LoanState loanState; 
	private Library library;
	//Test: null normal
	public BookInstance(Book book){
		if(book == null){
			throw new IllegalArgumentException("Book can't be null");
		}
		this.book = book;
		id = UUID.randomUUID().toString();
		loanState = LoanState.AVAILABLE;
	}
	//getters
	public String getId(){
		return id;
	}

	public Book getBook(){
		return book;
	}

	//Available, reserved, borrowed
	public Patron getPatron(){
		if(loanState.equals(LoanState.AVAILABLE)){
			return null;
		}
		return patron;

	}

	public LoanState getLoanState(){
		return loanState;
	}
	//test: patron null, patron notin system, already borrowed,on reserve, normal;
	public void borrow(Patron patron) throws OnLoanException{
		if(patron == null){
			throw new IllegalArgumentException("Patron can't be null");
		}
		//check to make sure part of library
		if(!library.getPatrons().contains(patron)){
			throw new IllegalArgumentException("Patron must belong to library");	
		}
		if((loanState.toString().equals("ON_RESERVE") && !patron.equals(this.patron)) || loanState.toString().equals("BORROWED")){
			throw new OnLoanException("Sorry, but this book is not available");
		}
		this.patron = patron;
		loanState = LoanState.BORROWED;

	}
	//test: patron null, notInSystem, booKOnLoan, bookOnReserve, normal
	public void reserve(Patron patron){
		if(patron == null){
			throw new IllegalArgumentException("Patron can't be null.");
		}
		//check to make sure part of library
		if(!library.getPatrons().contains(patron)){
			throw new IllegalArgumentException("Patron must belong to library");	
		}
		//verifies that book is available or on reservee to this patron
		if(loanState.toString().equals("AVAILABLE") || (loanState.toString().equals("ON_RESERVE") && this.patron.equals(patron))){
			loanState = LoanState.ON_RESERVE;
			this.patron = patron;
		}
		else{
			throw new IllegalArgumentException("Sorry, but this book is not available for you");
		}

	}
	//test: Patron null, notInLibrary, notBorrowedBook, normal
	public void returnInstance(final Patron pat) throws NotOnLoanException{
		if(pat == null){
			throw new IllegalArgumentException("Patron can't be null.");
		}
		//check to make sure part of library
		if(!library.getPatrons().contains(pat)){
			throw new IllegalArgumentException("Patron must belong to library");	
		}
		// p = book.getPatron();
		if(loanState != LoanState.BORROWED){
			throw new NotOnLoanException("This book isn't on loan");
		}
		
		if(patron == null || !patron.equals(pat)){
			throw new IllegalArgumentException("This isn't on loan to the specified patron");
		}
		patron = null;
		loanState = LoanState.AVAILABLE;
	}

	public void setLibrary(Library library){
		if(library == null){
			throw new IllegalArgumentException("Library can't be null");
		}
		this.library = library;
	}

	@Override
	public String toString(){
		return "Book: "+book+" ID: "+id;
	}
	@Override 
	public int hashCode(){
		return Objects.hash(id);
	}
	@Override 
	public boolean equals(Object that){
		if(that == null || that.getClass() != this.getClass()){
			return false;
		}
		if(this == that){
			return true;
		}
		BookInstance thatBookInstance = (BookInstance)(that);
		return id.equals(thatBookInstance.getId());
	}
}
