package com.infy.Product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import com.infy.Product.Dto.ProductDTO;
import com.infy.Product.Dto.StockDTO;
import com.infy.Product.Dto.SubscribedproductDTO;
import com.infy.Product.entity.Product;
import com.infy.Product.entity.Subscribedproduct;
import com.infy.Product.repository.ProductRepository;
import com.infy.Product.repository.SubscribedproductRepository;





@Service
public class ProductServiceImpl implements ProductService{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProductRepository productRepository;
	@Autowired
	SubscribedproductRepository subscribedproductRepository;
	
	//Get the entire product list
	public List<ProductDTO> getAllProducts() throws ProductMSException{
		System.out.println("In service");
		List<Product>products = productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		
		for(Product p:products) {
			ProductDTO productDTO = ProductDTO.valueOf(p);
			productDTOs.add(productDTO);
		}
		logger.info(" Getting Product details : {}", productDTOs);
		return productDTOs;
	}
 // GETTING PRODUCT DETAILS ACCORDING TO CATEGORY
	@Override
	public List<ProductDTO> getProductByCategory(@PathVariable String category) throws ProductMSException {
		// TODO Auto-generated method stub
		List<Product> product = productRepository.findByCategory(category);
		List<ProductDTO> productDTOs = new ArrayList<>();
		
		for(Product p:product) {
			productDTOs.add(ProductDTO.valueOf(p));
		}
		logger.info(" Getting Product details according to category : {}", productDTOs);
		
		return productDTOs;
	}
	// GETTING PRODUCT DETAILS ACCORDING TO name
	@Override
	public List<ProductDTO> getProductByName(String productname) throws ProductMSException {
		// TODO Auto-generated method stub
		
		List<Product> products = productRepository.findByProductname(productname);
		List<ProductDTO> productDTOs = new ArrayList<>();
		
		for(Product p:products) {
			productDTOs.add(ProductDTO.valueOf(p));
		}
		logger.info("Product details according to product name : {}", productDTOs);
		return productDTOs;
	}
	// adding products to product list
	public void addProduct(ProductDTO productDTO) {
		
		Product product=productDTO.createEntity();
		System.out.println("adding product to product list");
		productRepository.save(product);
	}
 
	// // GETTING PRODUCT DETAILS ACCORDING TO id
		public ProductDTO getProductById(Integer prodid) {
			logger.info("====== Product details according  to id  {} ======",prodid);
			ProductDTO productDTO=null;
			Optional<Product> product = productRepository.findById(prodid);
			if(product.isPresent())
			{
				Product p=product.get();
				productDTO =ProductDTO.valueOf(p);
		}

			//add to wish
			//addProductToWishList(prodid);
			
			return productDTO;
	}
		// Stock update 
		public boolean Stock(StockDTO stockDTO) {
			logger.info("Updating Stock ");
			Optional<Product> product1 = productRepository.findById(stockDTO.getProdid());
			if(stockDTO.getStock()>=10)
			{ 
				Product p=product1.get();
				p.setStock(stockDTO.getStock());
				productRepository.save(p);
				return true;
		}
			else
			{
				return false;
			}
}
		// Adding subscription
		@Override
		public void addSubsciptions(int subid, SubscribedproductDTO subscribedproductDTO) {
			// TODO Auto-generated method stub
			subscribedproductDTO.setSubid(subid);
			Subscribedproduct product=subscribedproductDTO.createEntity();
			subscribedproductRepository.save(product);
			
		}
// getting subscription details
		@Override
		public SubscribedproductDTO getDetailsBysubId(Integer subid) throws ProductMSException {
			logger.info("======  Getting Subscription details==== {} ======",subid);
			SubscribedproductDTO subscribedproductDTO=null;
			Optional<Subscribedproduct> product = subscribedproductRepository.findById(subid);
			if(product.isPresent())
			{
				Subscribedproduct p=product.get();
				subscribedproductDTO =SubscribedproductDTO.valueOf(p);
		}
			return subscribedproductDTO;
		}
		
		// check stock before order
		public boolean checkStockBeforeOrder(Integer prodid,Integer quantity) {
			logger.info("Updating Stock after order");
			Optional<Product> product1 = productRepository.findById(prodid);
			Product p=product1.get();
			Boolean value;
			if(p.getStock()>=quantity) {
				p.setStock(p.getStock()-quantity);
				value=true;
			}
			else {
				value=false;
			}
			return value;
		}
		
		}

	

