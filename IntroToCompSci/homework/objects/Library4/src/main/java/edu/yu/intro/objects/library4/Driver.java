package edu.yu.intro.objects.library4;
import java.util.*; 
import java.util.UUID;
import java.io.*;
public class Driver{
	public static Library run(){
	//Reset any state so that no Patrons exist, no Books exist, etc. 
	//Create 3 different books, add them to the Library 
	//Create 2 different patrons, add them to Patrons  
	Library library = new Library("PBCC","101 East 1st street", "1231231234");
	Book a = new Book("Hamlet" , "Shakespear", 1234567890123L,"hardcover");
	Book b = new Book("Chumash", "G-d", 1234567890124L, "ebook");
	Book c =  new Book("Batman", "DC", 1234567890125L, "paperback");
	library.add(a); 
	library.add(b);
	library.add(c);
	Patron p1 = new Patron("first", "lnOne", "here");
	Patron p2 = new Patron("Second", "lnTwo", "there");
	//Patron p3 = new Patron patron("third", "3ln", "everywhere");

	library.add(p1);
	library.add(p2);
	return library;
	}
	/*public static void main(String[] args) {
		run();
	}
		Library library1 = new Library("Englewood Library", " 221 Allison Court", "201-290-2124");
		Library library2 = new Library("Teaneck Library", "1600 Queen Anne Road", "201-555-5555");
		Library library3 = new Library("New York Public Library", "510 W 184th", "999-999-9999");

		Patron patron1 = new Patron("Avi", "Katz", "555 Broad Avenue");
		Patron patron2 = new Patron("Myles", "Tyberg", "Somewhere In England");
		Patron patron3 = new Patron("David", "Beardface", "777 Who Knows Road");
		Patron patron4 = new Patron("Rabbi", "Belizon", "Fair Lawn NJ");

		Book book1 = new Book("Moby Dick", "Julio Mann", 1234567899999L, "hardcover");
		Book book2 = new Book("The Kuzari", "Julio Mann", 1234567891234L, "ebook");
		Book book3 = new Book("The Little Midrash Says", "Who Cares", 1234567894433L, "paperback");
		Book book4 = new Book("MIT Discrete", "Some Shmo", 9876543210000L, "ebook");
		Book book5 = new Book("Tanach", "G-d", 6574839201101L, "hardcover");

		library1.add(book1);
		library1.add(book2);
		library1.add(book3);
		library1.add(book4);

		library2.add(book1);
		library2.add(book4);

		library3.add(book5);

		library1.add(patron1);
		library1.add(patron2);

		library2.add(patron3);

		library3.add(patron4);

		library1.borrow(patron1, book1);
		library1.borrow(patron2, book1);
		library1.borrow(patron1, book1);
		library1.borrow(patron1, book1);
		

		Collection<Patron> lib1 = library1.onLoan(patron1);
		Object[] lib1Array = lib1.toArray();
		for(Object b: lib1Array){
			System.out.println((Book)(b));	
		}
		Collection<Patron> lib2 = library1.onLoan(patron2);
		Object[] lib2Array = lib2.toArray();
		for(Object b: lib1Array){
			System.out.println((Book)(b));	
		}
		
		//Collection<Patron> lib2 = library2.onLoan(patron3);
		Collection<Patron> lib3 = library3.onLoan(patron4);
		System.out.println(library1.nPatrons());//2
		System.out.println(library1.get(patron1.getId()));//avi
		library1.clear();
		System.out.println(library1.nPatrons());//0
		//System.out.println(library1.getBook());//0
		BookFilter bookFilter = new BookFilter.Builder()
								.setTitle("d")
								.setAuthor("d")
								.setISBN13(1234567939999L)
								.setBookType("hardcover")
								.build();
		Collection<Book> books = (Collection<Book>)(library1.search(bookFilter));
		for(Book b: books){
			System.out.println(b);//0
		}

	}*/
}