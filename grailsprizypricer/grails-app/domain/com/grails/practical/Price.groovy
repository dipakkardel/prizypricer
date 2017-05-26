package com.grails.practical;

import grails.validation.Validateable;
import groovy.transform.EqualsAndHashCode;
@Validateable
@EqualsAndHashCode
class Price implements Serializable {

	BigDecimal price;
	static belongsTo = [product:Product]
	static mapping = {
		table 'price'
		version:false
	}
	static constraints = {
		price nullable:false,scale:2
		price(min:new BigDecimal(0.0),max:new BigDecimal(99999999.99))
	}


	public Price(BigDecimal price) {
		this.price = price;
	}
	
	
	@Override
	public String toString() {
		return price;
	}
}
