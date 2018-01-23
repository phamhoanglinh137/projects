package com.apache.obj;

import java.io.Serializable;

public class Account extends Base implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String accountId;
	private String fullName;
	private String nric;
	
	public Account(String accountId, String fullName, String nric) {
		this.accountId = accountId;
		this.fullName = fullName;
		this.nric = nric;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", fullName=" + fullName + ", nric=" + nric + "]";
	}
	
}
