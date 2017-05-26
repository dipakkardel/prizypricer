package com.grails.practical



import static org.springframework.http.HttpStatus.*

import org.codehaus.groovy.grails.web.pages.AbstractGroovyPageBinding.BindingMapEntry;
import org.springframework.beans.factory.annotation.Autowired;

import grails.transaction.Transactional

@Transactional
class PriceController {
	def priceStrategyService;
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Price.list(params), model:[priceInstanceCount: Price.count()]
    }

    def show(Price priceInstance) {
        respond priceInstance
    }

    def create() {
        respond new Price(params)
    }

   
    def save(Price priceInstance) {
		
        if (priceInstance == null) {
            notFound()
            return
        }

        if (priceInstance.hasErrors()) {
            respond priceInstance.errors, view:'create'
            return
        }
	
		if(priceStrategyService.checkDuplicatePriceForSameProduct(priceInstance, params.product.id))
		{
			flash.error = "Product ${Product.get(params.product.id).productName} already has a price ${priceInstance.price}";
			respond flash.error,view:'create'
			return
		}


		
        priceInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'price.label', default: 'Price'), priceInstance.id])
				redirect(action:"index",id:params.barcode)
            }
            '*' { respond priceInstance, [status: CREATED] }
        }
    }

    def delete(Price priceInstance) {
	
		if (priceInstance == null) {
			notFound()
			return
		}
		priceInstance.delete flush:true

		flash.message = "${priceInstance.price} of product ${Product.get(priceInstance.product.barcode).productName} is deleted";
		redirect(action:"index",id:params.barcode)
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'price.label', default: 'Price'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
