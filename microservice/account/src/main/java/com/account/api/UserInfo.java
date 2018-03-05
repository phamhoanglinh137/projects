package com.account.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author linhpham
 *
 */
@Getter @Setter 
@Builder
public class UserInfo {
	private String userName;
	private String address;
}
