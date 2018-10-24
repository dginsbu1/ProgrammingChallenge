package edu.yu.introtoalgs;
import java.util.Arrays;

public class SortDriver{
	
	// public static void main(String args[]){
	// 	Item a = new Item("a", 1);
	// 	Item b = new Item("b", 1);
	// 	Item c = new Item("c", 1);
	// 	Item d = new Item("b", 2);
	// 	Item e = new Item("a", 1);
	// 	Item f = new Item("e", 1);
	// 	Item g = new Item("a", 1);
	// 	Item h = new Item("g", 23342);
	// 	Item i = new Item("g", 22);
	// 	ColoredItem ca = new ColoredItem("a", 1, Color.BLUE);
	// 	ColoredItem cb = new ColoredItem("a", 1, Color.RED);
	// 	ColoredItem cc = new ColoredItem("a", 2, Color.BLUE);
	// 	ColoredItem cd = new ColoredItem("b", 1, Color.BLUE);
	// 	ColoredItem ce = new ColoredItem("d", 4, Color.BLUE);
	// 	ColoredItem cf = new ColoredItem("d", 3, Color.GREEN);
	// 	ColoredItem cg = new ColoredItem("f", 23234, Color.YELLOW);
	// 	ColoredItem ch = new ColoredItem("g", 22, Color.BLUE);
	// 	Item[] items = {a,b,c,d,e,f,g,h,i};
	// 	ColoredItem[] coloredItems = {ca,cb,cc,cd,ce,cf,cg,ch};
	// 	Item[] mixedItems = {ca,cb,cc,a,b,e,cd,ce,cf,cg,ch,a,b,c,ca,cg,cb,d,e,f,g,h,i};
		
	// 		// sort(items);
	// 		// System.out.println("Items:");
	// 		// for(Item is: items){
	// 		// 	System.out.println(is);
	// 		// }

	// 	System.out.println();

	// 	sortByPrice(items);
	// 	System.out.println("ItemsByPrice:");
	// 	for(Item is: items){
	// 		System.out.println(is);
	// 	}

	// 	System.out.println();

	// 	// System.out.println("ColoredItems:");
	// 	// sort(coloredItems);
	// 	// for(ColoredItem cis: coloredItems){
	// 	// 	System.out.println(cis);
	// 	// }

	// 	System.out.println();

	// 	System.out.println("ColoredItemsByPrice:");
	// 	sortByPrice(coloredItems);
	// 	for(ColoredItem cis: coloredItems){
	// 		System.out.println(cis);
	// 	}

	// 	sort(mixedItems);
	// 	System.out.println("MixedItems:");
	// 	for(Item mis: mixedItems){
	// 		System.out.println(mis);
	// 	}

	// 	System.out.println(ca.equals(cb)+ " "+ cb.equals(ca));
	// 	System.out.println(ca.compareTo(cb)+ " "+ cb.compareTo(ca));


		

	// }

	static public void sort(Item[] a){
		Arrays.sort(a);
	}


	// This method sorts the Array of Item by ascending order on getPrice().
	// You must implement this capability through use of the Comparator
	// interface.
	static public void sortByPrice (Item [] a){
		Arrays.sort(a, new Item.priceOrder());
	}





}