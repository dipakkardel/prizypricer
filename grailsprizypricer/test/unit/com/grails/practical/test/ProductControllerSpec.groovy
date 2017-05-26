package com.grails.practical.test
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;

import com.grails.practical.Product;
import com.grails.practical.ProductController;

import grails.test.mixin.*
import grails.test.runtime.FreshRuntime;
import spock.lang.*

@TestFor(ProductController)
@Mock(Product)
class ProductControllerSpec extends Specification {
	def priceStrategyService
    def populateValidParams(params) {
        assert params != null
		params["barcode"] = 'XXXxxx12'
		params["product.id"] = 'XXXxxx12'
		params["id"] = 'XXXxxx12'
		params["productName"] = 'moto-g'
		params["productDescription"] = 'xxxxxxxxxxxx'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.productInstanceList
            model.productInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.productInstance!= null
    }
	
    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def product = new Product()
            product.validate()
            controller.save(product)

        then:"The create view is rendered again with the correct model"
            model.productInstance!= null
            view == 'create'

    }
	
	
}
