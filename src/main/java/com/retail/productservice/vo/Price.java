package com.retail.productservice.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Value Object to hold the price information such as price and corresponding currency code
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {

	private BigDecimal value;

	@JsonProperty("currency_code")
	private String currencyCode;

	public Price() {
	}

	public Price(BigDecimal value, String currencyCode) {
		this.value = value;
		this.currencyCode = currencyCode;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Override
	public String toString() {
		return "{ value : " + this.value + ", currency_code :" + this.currencyCode + " }";
	}

}
