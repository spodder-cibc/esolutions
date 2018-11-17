package com.sushil.shoping;

/**
 * The <code> Item </code>
 * 
 * @author sushil
 * @version 1.0
 * 
 */
public interface Item {
	/**
	 * the concrete class must implement this method. It will display output of each
	 * order as directed by business requirements
	 */
	public void showOrder();

	/**
	 * the concrete class must implement this method. it calculates SalesTax
	 * 
	 * @return float
	 */
	public float calculateSalesTax();

	/**
	 * the concrete class must implement this method. it calculates total price
	 * including tax
	 * 
	 * @return float
	 */
	public float calculateTotal();

}
