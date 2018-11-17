/**
 * 
 */
package com.sushil.shoping;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author sushil
 *
 */
public class ReceiptGeneratorTest {

	ReceiptGenerator rgen = null;
	String[] blocks = null;
	static final Logger logger = Logger.getLogger(ReceiptGeneratorTest.class);
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		rgen = new ReceiptGenerator();

		try {
			blocks = rgen.readFile("C:\\input.txt");

		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	/*
	 * check whether number of input data > 0
	 */
	public void test1() {
		//System.out.println(blocks.length);
		assertTrue(blocks.length > 0);
	}

	@Test
	/**
	 *  check whether number of entries in 1st input data > 0
	 */
	public void test2() {

		String[] data = blocks[0].split("\n");
		assertTrue(data.length > 0);
	}

	@Test
	/**
	 *  compare 1st entry of 1st input data
	 */
	public void test3() {

		String[] data = blocks[0].split("\n");

		assertEquals("1 book at 12.49", data[2]);
	}

	@Test
	/**
	 * extract imported flag from 1st entry of 1st Input
	 * 1 book at 12.49 -> not imported
	 */
	public void test4() {

		String[] data = blocks[0].split("\n");
		Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
		Matcher matcher = p.matcher(data[2]);
		boolean imported_flag = false;
		while (matcher.find()) {
			// decide whether given item is imported
			imported_flag = matcher.group(2).contains("imported") ? true : false;

		}

		assertEquals(imported_flag, false);
	}

	@Test
	/**
	 *  check the category of 1st entry of 1st Input
	 *  1 book at 12.49 -> book
	 */
	public void test5() {

		String[] data = blocks[0].split("\n");
		Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
		Classifier classifier = new Classifier();
		Matcher matcher = p.matcher(data[2]);
		boolean imported_flag = false;
		String category = "";

		while (matcher.find()) {
			// decide whether given item is imported
			imported_flag = matcher.group(2).contains("imported") ? true : false;
			category = classifier.getCategory(matcher.group(2));
		}

		assertEquals(category, "book");
	}

	@Test
	/**
	 *  check whether the category of 2nd entry of 1st input is uncategoried
	 *  1 music CD at 14.99 -> music is uncategoriezed ; basic tax involve
	 */
	public void test6() {

		String[] data = blocks[0].split("\n");
		Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
		Classifier classifier = new Classifier();
		Matcher matcher = p.matcher(data[3]);
		boolean imported_flag = false;
		String category = "";

		while (matcher.find()) {
			// decide whether given item is imported
			imported_flag = matcher.group(2).contains("imported") ? true : false;
			//System.out.println(matcher.group(2));
			category = classifier.getCategory(matcher.group(2));
		}

		assertEquals(category, "uncategoried");
	}

	@Test
	/**
	 * check whether the entry's object is-A TaxableItem
	 *  music CD -> tax involved
	 * 
	 */
	public void test7() {

		String[] data = blocks[0].split("\n");
		Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
		Classifier classifier = new Classifier();
		Matcher matcher = p.matcher(data[3]);
		boolean imported_flag = false;
		String category = "";

		while (matcher.find()) {
			// decide whether given item is imported
			imported_flag = matcher.group(2).contains("imported") ? true : false;
			category = classifier.getCategory(matcher.group(2));

			TaxFactory taxFactory = new TaxFactory(imported_flag, matcher.group(1), matcher.group(2), matcher.group(3));
			Item item = taxFactory.getTaxType(classifier.getCategory(matcher.group(2)));
			//System.out.println(matcher.group(2));
			boolean x = item instanceof TaxableItem;
			assertEquals(x, true);
		}
	}

		@Test
		/**
		 * check whether the entry's object is-A TaxExemptItem
		 *  book -> tax exempted
		 */
		public void test8() {

			String[] data = blocks[0].split("\n");
			Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
			Classifier classifier = new Classifier();
			Matcher matcher = p.matcher(data[2]);
			boolean imported_flag = false;
			String category = "";

			while (matcher.find()) {
				// decide whether given item is imported
				imported_flag = matcher.group(2).contains("imported") ? true : false;
				category = classifier.getCategory(matcher.group(2));
				TaxFactory taxFactory = new TaxFactory(imported_flag, matcher.group(1), matcher.group(2), matcher.group(3));
				Item item = taxFactory.getTaxType(classifier.getCategory(matcher.group(2)));
				boolean x = item instanceof TaxExemptItem;
				
				assertEquals(x, true);
			}
		
		}
		@Test
		/**
		 * check whether the item (music CD) taxable
		 */
		public void test9() {

			String[] data = blocks[0].split("\n");
			Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
			Classifier classifier = new Classifier();
			Matcher matcher = p.matcher(data[3]);
			boolean imported_flag = false;
			String category = "";

			while (matcher.find()) {
				// decide whether given item is imported
				imported_flag = matcher.group(2).contains("imported") ? true : false;
				category = classifier.getCategory(matcher.group(2));
				TaxFactory taxFactory = new TaxFactory(imported_flag, matcher.group(1), matcher.group(2), matcher.group(3));
				Item item = taxFactory.getTaxType(classifier.getCategory(matcher.group(2)));
				float tax= item.calculateSalesTax();
				//System.out.println(matcher.group(2));
				//System.out.println(String.valueOf(tax));
				assertEquals(String.valueOf(tax), "1.5");
				float total = item.calculateTotal();
				assertEquals(String.valueOf(total), "16.49");
			}
		
	}

		@Test
		/**
		 * Check whether the item (book) is not taxable
		 */
		public void test10() {

			String[] data = blocks[0].split("\n");
			Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
			Classifier classifier = new Classifier();
			Matcher matcher = p.matcher(data[2]);
			boolean imported_flag = false;
			String category = "";

			while (matcher.find()) {
				// decide whether given item is imported
				imported_flag = matcher.group(2).contains("imported") ? true : false;
				category = classifier.getCategory(matcher.group(2));
				TaxFactory taxFactory = new TaxFactory(imported_flag, matcher.group(1), matcher.group(2), matcher.group(3));
				Item item = taxFactory.getTaxType(classifier.getCategory(matcher.group(2)));
				float tax= item.calculateSalesTax();
				//System.out.println(String.valueOf(tax));
				assertEquals(String.valueOf(tax), "0.0");
				float total = item.calculateTotal();
				assertEquals(String.valueOf(total), "12.49");
			}

			assertEquals(category, "book");
		}
}
