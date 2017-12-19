package edu.yu.intro.objects.library3;
import java.util.Objects; 
import java.util.UUID;
import java.io.*;
//@comment should I use the ID methods or make my own
public class Patron{
	private String firstName, lastName, address, id;

	//test: null/empty, first/last/address regular (7 totatl)
	public Patron(String fName, String lName, String address){
		if(fName == null || fName.equals("")){
			throw new IllegalArgumentException("First name must have at leastt one character");
		}
		if(lName == null || lName.equals("")){
			throw new IllegalArgumentException("Last name must have at leastt one character");
		}
		if(address == null || address.equals("")){
			throw new IllegalArgumentException("Address name must have at leastt one character");
		}
		firstName = fName;
		lastName = lName;
		this.address = address;
		//sets the id to a randomly generated id
		id = UUID.randomUUID().toString(); 

	}
	//GETTERS
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getAddress(){
		return address;
	}
	public String getId(){
		return id;
	}

	//OVERRIDES
	@Override
	public int hashCode(){
		return Objects.hash(id);
	}

	//test: Null, notPatron, equal, notEqual (4)
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
		Patron thatPatron = (Patron)(that);
		return id == thatPatron.getId();
	}

	@Override
	public String toString(){
		return "First Name: "+firstName+" Last Name: "+lastName+" Address: "+address+" ID: "+id;
	}
}
