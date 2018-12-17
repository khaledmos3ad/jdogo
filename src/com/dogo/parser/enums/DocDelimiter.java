package com.dogo.parser.enums;

public enum DocDelimiter {

	SPLIT_DELIMETER("|"), OBJECT_SIGN("#"), 
	DATA_OPEN_SIGN("<"),DATA_CLOSE_SIGN(">");

	private final String value;

	private DocDelimiter(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
