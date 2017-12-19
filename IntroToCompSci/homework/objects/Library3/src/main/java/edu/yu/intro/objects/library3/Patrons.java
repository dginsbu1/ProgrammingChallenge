package edu.yu.intro.objects.library3;
import java.util.*; 
import java.io.*;

public class Patrons{
	private Set<Patron> patrons; 
	public  Patrons(){
		patrons = new HashSet<>();
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
			throw new IllegalArgumentException("UUID can't be null.");
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
}
