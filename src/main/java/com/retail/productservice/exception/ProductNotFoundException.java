package com.retail.productservice.exception;

/*
 * Exception to be thrown if there are no products found for the given product id
 */
public class ProductNotFoundException extends RuntimeException {

	private long productId;

	public ProductNotFoundException(long productId) {
		this.productId = productId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

}
