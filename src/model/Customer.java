package model;


import lombok.Data;

@Data
public class Customer {

	private Integer id; 
	private String name;
	private boolean activated;
	private Product product;
}
