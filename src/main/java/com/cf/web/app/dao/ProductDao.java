package com.cf.web.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cf.web.app.model.Product;
import com.cf.web.app.wrapper.ProductWrapper;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer>{

	List<ProductWrapper> getAllProduct();
	
	
}
