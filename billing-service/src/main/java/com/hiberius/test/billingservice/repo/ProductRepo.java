package com.hiberius.test.billingservice.repo;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepo {
	
	
	public String getName(Long productId) {
		
		String theName;
		int theNewProductId=productId.intValue();
		switch (theNewProductId) {
		case 1:
			theName= "Product A";
			break;
		case 2:
			theName= "Product B";
			break;
		case 3:
			theName= "Product C";
			break;
		case 4:
			theName= "Product D";
			break;
		case 5:
			theName= "Product E";
			break;
		case 6:
			theName= "Product F";
			break;
		case 7:
			theName= "Product G";
			break;
		case 8:
			theName= "Product H";
			break;
		case 9:
			theName= "Product I";
			break;
		case 10:

		default:
			theName= "Generic Product";;
		}
		
		
		return theName;
		
		
	}

}
