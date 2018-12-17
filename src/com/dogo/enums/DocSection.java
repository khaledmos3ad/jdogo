package com.dogo.enums;

public enum DocSection {
	
	HEADER("Header"), DATA("Data");

	private final String value;

	private DocSection(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
