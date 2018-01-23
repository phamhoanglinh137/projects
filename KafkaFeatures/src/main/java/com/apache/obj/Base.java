package com.apache.obj;

import java.util.UUID;

public class Base {
	public String getMsgId() {
		return UUID.randomUUID().toString();
	}
}
