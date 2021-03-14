package com.infy.Product.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.infy.Product.Dto.CartDTO;
import com.infy.Product.Dto.ProductDTO;
import com.infy.Product.Dto.StockDTO;
import com.infy.Product.Dto.SubscribedproductDTO;

import com.infy.Product.service.ProductMSException;
import com.infy.Product.service.ProductService;
import org.springframework.core.env.Environment;



@RestController
@CrossOrigin
public class ProductController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProductService productService;
	@Autowired
	Environment environment;
	
	
	
	// Fetches all products
	@GetMapping(value = "/api/products",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> getAllProducts() throws ProductMSException {
		try{
		List<ProductDTO> m=productService.getAllProducts();
		ResponseEntity<List<ProductDTO>> response=new ResponseEntity<List<ProductDTO>>(m,HttpStatus.OK);
		return response;
		}
		catch(ProductMSException e) {
			ResponseStatusException exception=new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
			throw exception;}
		}
	
	// Fetches products according to category
	@GetMapping(value = "/api/products/{category}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String category) throws ProductMSException{
		try
		{
			List<ProductDTO> subscribedproduct=productService.getProductByCategory(category);
		ResponseEntity<List<ProductDTO>> response=new ResponseEntity<List<ProductDTO>>(subscribedproduct,HttpStatus.OK);
			return response;
		}
		catch(ProductMSException e) {
			ResponseStatusException exception=new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
			throw exception;}
		}
	
	// Fetches products according to product name
	@GetMapping(value = "/api/product/{productname}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> getProductsByName(@PathVariable String productname) throws ProductMSException{
		try
		{
			List<ProductDTO> subscribedproduct=productService.getProductByName(productname);
		ResponseEntity<List<ProductDTO>> response=new ResponseEntity<List<ProductDTO>>(subscribedproduct,HttpStatus.OK);
			return response;
		}
		catch(ProductMSException e) {
			ResponseStatusException exception=new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
			throw exception;}
		}
	//add product
	@PostMapping(value = "/api/product/add",  produces = MediaType.APPLICATION_JSON_VALUE)
	public void addProduct(@RequestBody ProductDTO productDTO) throws ProductMSException {
		productService.addProduct(productDTO);
	}

	
	
	//updating stock
	@PutMapping(value = "/api/stock/{prodid}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> Stock(@RequestBody StockDTO stockDTO) throws ProductMSException {
		ResponseEntity<String> response = null;
		try {
			boolean flag=productService.Stock(stockDTO);
			if(flag) {
		String msg=environment.getProperty("STOCK_UPDATED");
		 response=new ResponseEntity<String>(msg,HttpStatus.OK);
		}
			else
			{
				logger.info("Update Stock {}",stockDTO.getStock());
				productService.Stock(stockDTO);
				String failureMessage=environment.getProperty("LESS_QUANTITY");
				response=new  ResponseEntity<String>(failureMessage,HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception e) {
			throw new ResponseStatusException (HttpStatus.OK,environment.getProperty(e.getMessage()),e);
			
		}
		return response;
	}
	//Subscription added
	@PostMapping(value = "/api/subscriptions/{subid}/add",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addProduct(@PathVariable int subid,@RequestBody SubscribedproductDTO subscribedproductDTO) throws ProductMSException {
		ResponseEntity<String> response = null;
		try{
		productService.addSubsciptions(subid,subscribedproductDTO);
		String successMessage = environment.getProperty("SUBSCRIPTION_ADDED");
		 response = new ResponseEntity<String>(successMessage,HttpStatus.CREATED);
	}
		catch(ProductMSException e) {
		          throw new ResponseStatusException(HttpStatus.OK,environment.getProperty(e.getMessage()),e);
     	}
		return response;
	  }
	@GetMapping(value = "/api/subscriptions/{subid}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SubscribedproductDTO> getSubscriptionsDetails(@PathVariable Integer subid) throws ProductMSException {
		try
		{
			SubscribedproductDTO subscribedproduct=productService.getDetailsBysubId(subid);
		ResponseEntity<SubscribedproductDTO> response=new ResponseEntity<SubscribedproductDTO>(subscribedproduct,HttpStatus.OK);
			return response;
		}
		catch(ProductMSException e) {
			ResponseStatusException exception=new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
			throw exception;
		}
	}
	@PostMapping(value = "/api/checkstock/{prodid}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> checkStockBeforeOrder(@PathVariable int prodid,@RequestBody Integer Quantity) throws ProductMSException {
		try{
			productService.checkStockBeforeOrder(prodid,Quantity);
			String msg=environment.getProperty("checking Stock before Order");
			ResponseEntity<String> response=new ResponseEntity<String>(msg,HttpStatus.OK);
			return response;
			}
			catch(Exception e) {
				ResponseStatusException exception=new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
				throw exception;
			}
		}
	
	
}