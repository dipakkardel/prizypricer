package com.grails.practical



import static org.springframework.http.HttpStatus.*

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import grails.transaction.Transactional

@Transactional
class ProductController {
	def priceStrategyService
	def productCount
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)
        respond Product.list(params),model:[productInstanceCount: Product.count()]
    }

    def show(Product productInstance) {
		respond productInstance, model:[defaultStrategyMap:calculateForDefaultStrategies(),standardStrategySet:getStandardStrategySet(),priceCount:getPriceCount(),priceList:getListToDisplay()]
		
    }

    def create() {
        respond new Product(params)
    }

  
    def save(Product productInstance) {
        if (productInstance == null) {
            notFound()
            return
        }
        if (productInstance.hasErrors()) {
            respond productInstance.errors, view:'create'
            return
        }
	
        productInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'product.label', default: 'Product'), productInstance.barcode])
                redirect(action: "show", id: productInstance.barcode, params: [barcode: productInstance.barcode,productName:productInstance.productName,productDescription:productInstance.productDescription])
				return
            }
            '*' { respond productInstance, [status: CREATED] }
        }
    }
	
	
	def searchResults(Integer max){ 
		params.max = Math.min(max ?: 15, 100)
		def productCriteria = Product.createCriteria()
		def results = productCriteria.list(params) {
			if(params?.barcode) {
				ilike("barcode","${params.barcode}%")
				order("barcode", "asc")
				}
			else {
				ilike("barcode","%${params.barcode}%")
				order("barcode", "asc")
				}				
			}
		
			render view:'index' , model:[results: results,productCount: Product.count()]
		}
	
	
	
		
	def search = {
		render(view:'index')
	  }
		
	def getStandardStrategySet(){
		def list = priceStrategyService.getStandardStrategySet();
	}
	
	def calculateForDefaultStrategies(){
		def map = priceStrategyService.calculateForDefaultStrategies(params.id)
	}
	
	def calculateForStandardStrategy(){
		def price = priceStrategyService.calculateForStandardStrategy(params.id,params.strategy)
		
		if(BigDecimal.ZERO.compareTo(price) == 0 && "Ideal".equals(params.strategy))
		{
			render {
				div(id: "subContainer",style:"margin-top:-44px;",class:"message", "Provide minimum 5 prices to calculate ideal price")
			}
			return
		}
		
		if(getPriceCount() == 0){			
			render {
				div(id: "subContainer",style:"margin-top:-44px;",class:"message", "Add some prices for price calculation")
			}
			return
		}
		render(template: "/template/displayprices", collection:[key:params.strategy,value:price])
	}
	
	def getPriceCount(){
		def count =  priceStrategyService.getPriceCount(params.id)
	}
	
	def getListToDisplay(){
		def list = priceStrategyService.getPriceListTodisplay(params.id)
	}
	
    def delete(Product product) {
		Product productInstance = Product.get(params.barcode)
		println params.barcode
        if (productInstance == null) {
            notFound()
            return
        }

        productInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Product.label', default: 'Product'), productInstance.barcode])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }
	
	

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
