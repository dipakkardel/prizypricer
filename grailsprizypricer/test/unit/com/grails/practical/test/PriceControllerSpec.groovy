package com.grails.practical.test



import com.grails.practical.Price;
import com.grails.practical.PriceController;

import grails.test.mixin.*
import grails.test.runtime.FreshRuntime;
import spock.lang.*

@TestFor(PriceController)
@Mock(Price)
class PriceControllerSpec extends spock.lang.Specification {

    def populateValidParams(params) {
        assert params != null
		params["price"] = 'new BigDecimal(33.00)'
		params["product.id"] = 'XXXxxx12'
		params["id"] = 'XXXxxx12'
		params["barcode"] = 'XXXxxx12'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.priceInstanceList
            model.priceInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.priceInstance!= null
    }

    void "Test the save action correctly persists an instance"() {
		
		
        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def price = new Price()
            price.validate()
            controller.save(price)

        then:"The create view is rendered again with the correct model"
            model.priceInstance!= null
            view == 'create'

    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show()

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def price1 = new Price(params)
            controller.show(price1)

        then:"A model is populated containing the domain instance"
            model.priceInstance == price1
    }

   
    
}
