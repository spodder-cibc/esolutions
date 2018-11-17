package com.sushil.shoping;

import org.apache.log4j.Logger;

/**
 * The <code> TaxFactory </code>
 * 
 * @author sushil
 * @version 1.0
 * 
 *          A Factory class that creates objects based on some criteria 
 *          in this implementation, we have two broad criteria 
 *          a) uncategoried (for which basic tax will be applied) 
 *          b) categorized (like book, food,medicinal products; tax exemption is applied)
 * 
 */
public class TaxFactory {

	static final Logger logger = Logger.getLogger(TaxFactory.class);

	private String number = "";
	private String item_string = "";
	private String unitprice = "";

	private boolean imported_flag = false;

	/**
	 * It creates instance of TaxFactory
	 * 
	 * @param imported_flag
	 * @param number
	 * @param item_string
	 * @param unitprice
	 */
	public TaxFactory(boolean imported_flag, String number, String item_string, String unitprice) {
		this.imported_flag = imported_flag;
		this.number = number;
		this.item_string = item_string;
		this.unitprice = unitprice;
		logger.info("imported_flag:"+imported_flag+" number:"+number+" item_string:"+item_string+" unitprice:" + unitprice);

	}

	/**
	 * This method creates object based on category
	 * 
	 * @param category
	 * @return
	 */
	public Item getTaxType(String category) {

		Item item = null;
		if (category.equalsIgnoreCase("uncategoried")) {
			item = new TaxableItem(imported_flag, number, item_string, unitprice);
		} else {
			item = new TaxExemptItem(imported_flag, number, item_string, unitprice);
		}
		return item;
	}
}
