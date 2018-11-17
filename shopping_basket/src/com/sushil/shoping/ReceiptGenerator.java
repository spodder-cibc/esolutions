package com.sushil.shoping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The <code> ReceiptGenerator </code>
 * 
 * @author sushil
 * @version 1.0
 * 
 *          This is the main class that  
 *          a) read the data from a local file
 *          b) generates Receipt that will show output as directed by the
 *          business requirements
 * 
 */
public class ReceiptGenerator {
	//static logger
	static final Logger logger = Logger.getLogger(ReceiptGenerator.class);
		
	List<String> list = new ArrayList<String>();

	 String[] readFile(String filepath) throws IOException {
		 logger.info("reading file" + filepath);
		 
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			StringBuffer sbuffer = new StringBuffer();
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				sbuffer.append(line).append("\n");
			}

			String[] blocks = sbuffer.toString().split("\n\n");
			br.close();
			 logger.info("reading complete");
			return blocks;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static void main(String[] args) throws IOException {

		PropertyConfigurator.configure("log4j.properties");
		
		String filename ="";
		if (args.length < 1) {
			System.out.println("Error: please provide filename with absolute path as argument eg. C:\\\\input.txt");
			System.out.println("taking default file from project root");
			filename = "input.txt";
			
		}else {
			filename = args[0];
		}
	
		ReceiptGenerator receiptGenerator = new ReceiptGenerator();

		// read file and save each of the blocks in an array
		String[] blocks = receiptGenerator.readFile(filename);
		logger.info(Arrays.toString(blocks));
		
		int blockCount = 0;
		if (blocks != null && blocks.length != 0) {
			Pattern p = Pattern.compile("(^[\\d]*)([ a-zA-Z]*)[a-zA-Z]{2}[ ]([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)");
			Classifier classifier = new Classifier();
			// print first empty line
			System.out.println("");
	
			for (String block : blocks) {
				block = block.trim();
				if (block.length() == 0)
					continue;
				// block - individual block from file
				String[] data = block.split("\n");
				
				boolean imported_flag = false;

				float tax = 0;
				float total = 0;

				String datam = "";
				int count = 0;

				for (count = 0; count < data.length; count++) {
					datam = data[count];

					// print the output Starting with Output for each of the block
					if (datam.contains("Input")) {
						System.out.println(datam.trim().replace("Input", "Output"));

					} else {

						Matcher matcher = p.matcher(datam);

						TaxFactory taxFactory = null;
						Item item = null;

						while (matcher.find()) {
							// decide whether given item is imported
							imported_flag = matcher.group(2).contains("imported") ? true : false;

							taxFactory = new TaxFactory(imported_flag, matcher.group(1), matcher.group(2),
									matcher.group(3));
							item = taxFactory.getTaxType(classifier.getCategory(matcher.group(2)));

						}
						tax += item.calculateSalesTax();
						total += item.calculateTotal();

						// print each of the items with price including tax (if any)
						item.showOrder();

					}

				}

				logger.info("Sales Taxes: "+tax +" Total:"+total);
				// print sales tax and total with format
				System.out.println("Sales Taxes: " + String.format("%.2f", tax));
				if (blockCount++ == blocks.length - 1) {
					System.out.print("Total: " + String.format("%.2f", total));
				} else {
					System.out.println("Total: " + String.format("%.2f", total) + "\n");
				}

			}
		}
	}

}
