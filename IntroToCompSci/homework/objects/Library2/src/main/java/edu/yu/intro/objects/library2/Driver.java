//reset the patrons state
package edu.yu.intro.objects.library2;
import java.util.Objects; 
import java.util.UUID;
import java.io.*;
public class Driver{
	public static void run(){
	//Reset any state so that no Patrons exist, no Books exist, etc. 
	//Create 3 different books, add them to the Library 
	//Create 2 different patrons, add them to Patrons 
	Patrons.Singleton.clear(); 
	Library library = new Library("PBCC","101 East 1st street", "1231231234");
	Book a = new Book("Hamlet" , "Shakespear", 1234567890123L,"hardcover");
	Book b = new Book("Chumash", "G-d", 1234567890124L, "ebook");
	Book c =  new Book("Batman", "DC", 1234567890125L, "paperback");
	library.add(a); 
	library.add(b);
	library.add(c);
	Patron p1 = new Patron("first", "1ln", "here");
	Patron p2 = new Patron("Second", "2ln", "there");
	//Patron p3 = new Patron patron("third", "3ln", "everywhere");
	Patrons.Singleton.add(p1);
	Patrons.Singleton.add(p2);
	}

}