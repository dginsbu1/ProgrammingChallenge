package edu.yu.introtoalgs;

public class ColoredItem extends Item {
	final Color color;

	public ColoredItem ( final String description , 
			final double price , final Color color){
		super(description, price);
		this.color = color;
	}
	public Color getColor(){
		return color;
	}
	//old code 
	//public int compareTo(ColoredItem that){
	public int compareTo(Item that){
		if(this.description.compareTo(that.getDescription()) < 0) return -1;
		if(this.description.compareTo(that.getDescription()) > 0) return 1;
		if(this.price < that.getPrice()) return -1;
		if(this.price > that.getPrice()) return 1;
		//old code 
		// return this.color.toString().compareTo(that.color.toString());
		if(that instanceof ColoredItem){
			return this.color.toString().compareTo(((ColoredItem)(that)).color.toString());
		}
		//if its a regular Item its smaller
		return 1;
		
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
		ColoredItem thatColoredItem = (ColoredItem)(that);
		return  this.hashCode() == thatColoredItem.hashCode();
	}
	@Override
	public int hashCode(){
		int hash = 1;
		hash = 31*hash + this.description.hashCode();
		hash = 31*hash + Double.valueOf(this.price).hashCode();
		hash = 31*hash + this.color.toString().hashCode();
		return hash;
	}
	@Override
	public String toString(){
		return description + ", "+price+", " +color;
	}



}