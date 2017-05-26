package com.grails.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public enum LowestPricingStrategy implements PriceCalculationStrategy{
	INSTANCE;
	
	@Override
	public BigDecimal calculatePrice(List<BigDecimal> priceList) {
		if(priceList.size() != 0)
			return priceList.stream().min((b1, b2) -> { return b1.compareTo(b2);})
					.get().setScale(2, RoundingMode.HALF_UP);
		return BigDecimal.ZERO;
	}
}
