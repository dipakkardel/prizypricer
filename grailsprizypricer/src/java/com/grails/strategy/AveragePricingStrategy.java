package com.grails.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public enum AveragePricingStrategy implements PriceCalculationStrategy{
	INSTANCE;
	
	@Override
	public BigDecimal calculatePrice(List<BigDecimal> priceList) {
		if(priceList.size() != 0)
			return priceList.stream().reduce(BigDecimal.ZERO, (b1, b2) ->{return b1.add(b2);} )
					.divide(new BigDecimal(priceList.stream()
					.count()),2,RoundingMode.HALF_UP)
					.setScale(2, RoundingMode.HALF_UP);
		return BigDecimal.ZERO;
	}
}
