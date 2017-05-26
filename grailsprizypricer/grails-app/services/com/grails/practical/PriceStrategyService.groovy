package com.grails.practical

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;

import com.grails.pricecalculation.strategy.map.StrategyMap;
import com.grails.strategy.PriceCalculationStrategy;
import com.sun.org.apache.bcel.internal.generic.LSHL;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import grails.transaction.Transactional

@Transactional
class PriceStrategyService{

	StrategyMap strategyMapInstance = new StrategyMap();
	def Price priceInstance;
	
	def getStandardStrategySet(){
		def standardStrategySet = strategyMapInstance.getStandardStrategySet()
		def set = new HashSet<String>()
		standardStrategySet.each {
			e -> set.add(e.toLowerCase().capitalize())
		}
		return set
	}
	def calculateForDefaultStrategies(String barcode){
		def strategyMap = strategyMapInstance.calculateForDefaultStrategies(getPriceList(barcode))
		def map = new HashMap<String, BigDecimal>()
		strategyMap.each {
			key,value -> map.put(String.valueOf(key).toLowerCase().capitalize(), value)
		}
		return map
	}
	
	def calculateForStandardStrategy(String barcode,String strategy){
		def price = strategyMapInstance.calculateForStandardStrategy(getPriceList(barcode),strategy.toUpperCase())
		return price
	}
	
	def getPriceList(String barcode){
		List<BigDecimal>priceListInBigDecimal = new ArrayList<BigDecimal>();
		List<Price>priceList = Price.createCriteria().list {
			product{
				eq("barcode",barcode as String)
			}
		}
		for(Price p : priceList){
			priceListInBigDecimal.add(p.price)
		}
		return priceListInBigDecimal;
	}
	
	
	def getPriceCount(String barcode){
		List<BigDecimal> priceList = getPriceList(barcode)
		return priceList.size()
	}
	
	def getPriceListTodisplay(String barcode){
		List<BigDecimal> priceList = getPriceList(barcode)
		Collections.sort(priceList)
		if(priceList.size() > 10)
			return priceList.subList(priceList.size()-10, priceList.size()).unique()
		return priceList
	}

	def checkDuplicatePriceForSameProduct(Price priceInstance,String barcode){
		List<Price>priceList = Price.createCriteria().list {
			product{
				eq("barcode",barcode as String)
			}
		}
		if(priceList.contains(priceInstance))
			return true
		return false
	}
	
	}
