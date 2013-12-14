package com.titanserver;

import java.io.Serializable;

import org.apache.http.Header;

public class HttpResult implements Serializable {
	public String content;
	public Header headers[];
}
