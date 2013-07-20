package com.titanserver;

import java.io.Serializable;
import java.util.HashMap;

public class ReturnCommand implements Serializable {
	public boolean isError;
	public String message;
	public HashMap<String, Object> map = new HashMap<String, Object>();

	public ReturnCommand() {
		isError = false;
	}
}
