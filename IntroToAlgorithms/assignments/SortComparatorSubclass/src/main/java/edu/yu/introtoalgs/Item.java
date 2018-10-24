package edu.yu.introtoalgs;
import java.util.Comparator;
//import java.util.Comparable;


//may need more methods
//You must declare Item as follows:
public class Item implements Comparable <Item>{
	final String description;
	final double price;

	public Item ( final String description , final double price ){
		this.description = description;
		this.price = price;
	}
	
	public String getDescription (){
		return description;
	}
	
	public double getPrice (){
		return price;
	}

	public int compareTo(Item that){

		if(this.description.compareTo(that.getDescription()) < 0) return -1;
		if(this.description.compareTo(that.getDescription()) > 0) return 1;
		if(this.price < that.getPrice()) return -1;
		if(this.price > that.getPrice()) return 1;
		//new code. Colored item is bigger
		if(that instanceof ColoredItem) return -1;
		return 0;
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
		Item thatItem = (Item)(that);
		return  this.hashCode() == thatItem.hashCode();
	}
	@Override
	public int hashCode(){
		int hash = 1;
		hash = 31*hash + this.description.hashCode();
		hash = 31*hash + Double.valueOf(this.price).hashCode();
		return hash;
	}
	@Override
	public String toString(){
		return description + ", "+price;
	}
	public static class priceOrder implements Comparator<Item>{
		public int compare(Item a, Item b){
			if(a.getPrice() < b.getPrice()) return -1;
			if(a.getPrice() > b.getPrice()) return 1;
			return 0;
		}
	}
}