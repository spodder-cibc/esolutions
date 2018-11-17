package com.sushil.shoping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The <code> Classifier </code>
 * 
 * @author sushil
 * @version 1.0
 * 
 *          This class stores the vocabularies for each of Categories (Arraylists) 
 *          with the unique keys (book, food,medical) a Hashmap saves each of the categories
 * 
 */
public class Classifier {

	private List<String> book = new ArrayList<String>();
	private List<String> food = new ArrayList<String>();
	private List<String> medical = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();

	Classifier() {
		book.add("book");				// we can add as many vocabularies as needed
		book.add("books");	
		
		food.add("chocolate");
		food.add("chocolates");
		
		medical.add("pills");
		medical.add("pill");

		hashmap.put("book", (ArrayList<String>) book);
		hashmap.put("food", (ArrayList<String>) food);
		hashmap.put("medical", (ArrayList<String>) medical);

	}

	/**
	 * it will return category name for each of the itemName
	 * @param itemName
	 * @return String
	 */
	public String getCategory(String itemName) {
		ArrayList<String> arraylist;
		 for (String key : hashmap.keySet()){
			 arraylist = hashmap.get(key);			
				for (String temp : arraylist) {
					if(itemName.trim().contains(temp))	{		
						return key;
					}
				}			 
		 }
		
		return "uncategoried";		
	}
}
