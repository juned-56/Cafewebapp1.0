package com.cf.web.app.wrapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductWrapper {

	@JsonProperty("id")
	Integer id;
	
	@JsonProperty("name")
	String name;
	
	@JsonProperty("description")
	String description;
	
	@JsonProperty("price")
	Integer price;
	
	@JsonProperty("status")
	String status;
	
	@JsonProperty("categoryId")
	Integer categoryId;
	
	@JsonProperty("categoryName")
	String categoryName;
	
	public ProductWrapper() {
	}
	
	public ProductWrapper(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.status = status;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
}
