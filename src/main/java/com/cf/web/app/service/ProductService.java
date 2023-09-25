package com.cf.web.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cf.web.app.wrapper.ProductWrapper;

public interface ProductService {

	ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

	ResponseEntity<List<ProductWrapper>> getAllProduct();

	ResponseEntity<String> updateProdcut(Map<String, String> requestMap);

}
