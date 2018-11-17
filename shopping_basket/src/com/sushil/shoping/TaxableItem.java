package com.sushil.shoping;

import org.apache.log4j.Logger;

/**
 * The <code> TaxableItem </code>
 * 
 * @author sushil
 * @version 1.0
 * 
 *          This class implements Item interface, invoked by the TaxFactory 
 *          It calculates tax for the items which is not exempted 
 *          For imported item, it also calculates imported duty tax
 */
public class TaxableItem implements Item {

	static final Logger logger = Logger.getLogger(TaxableItem.class);
	
	private boolean imported_flag = false; 				// based on the flag, duty tax is calculated (default false)

	private String number = ""; 						// number of items
	private String unitprice = ""; 						// unit price of an item
	private String item_string = ""; 					// item name

	private float tax = 0; 								// tax calculated based on the tax rate
	private float price = 0; 							// price = number * unitprice (default 0)
	

	/**
	 * Creates an instance of TaxableItem
	 * 
	 * @param imported_flag
	 * @param number
	 * @param item_string
	 * @param unitprice
	 */
	TaxableItem(boolean imported_flag, String number, String item_string, String unitprice) {
		this.imported_flag = imported_flag;
		this.number = number;
		this.item_string = item_string;
		this.unitprice = unitprice;
		logger.info("imported_flag:"+imported_flag+" number:"+number+" item_string:"+item_string+" unitprice:" + unitprice);

	}

	@Override
	public void showOrder() {
		System.out.println(number.trim() + " " + item_string.trim() + ": " + String.format("%.2f", calculateTotal()));

	}

	@Override
	public float calculateSalesTax() {
		if(!number.isEmpty() && !unitprice.isEmpty() ) {
			price = Integer.parseInt(number) * Float.parseFloat(unitprice);
		}
		
		/*
		 *  Business rule:The rounding rules for sales tax are that for a tax rate of n%, 
		 *  a shelf price of p contains (np/100 rounded up to the nearest 0.05) amount of sales tax.
		 */
		tax = (float) (Math.ceil(price * Constants.BASIC_TAX * 20) / 20.0);
		if (this.imported_flag) {
			tax += Math.ceil(price * Constants.IMPORT_DUTY * 20) / 20.0;
		}
		return tax;
	}

	@Override
	public float calculateTotal() {
		return price + tax;
	}

}
