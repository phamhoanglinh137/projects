package com.feature.lamda;

/**
 * 
 * @author linhpham
 *
 */
public class LamdaFeature {
	
	public static void main(String args[]) {
		Bank p = () -> {
			System.out.println("getBankName");
		};
		p.getBankName();
		
		Recipient r = (String name, String address, int age) -> {
			System.out.println(" name " + name);
			System.out.println(" address " + address);
			System.out.println(" age " + age);
		};
		r.setInfo("Lamda", "419740", 28);
		
		int[] total = new int[1];
		Runnable r1 = () ->  {
			System.out.print(total[0]);
			total[0]++;
			System.out.print(total[0]);
		};
		r1.run();
		System.out.print(total[0]);
	}
}

interface Bank {
	void getBankName();
}

interface Recipient {
	void setInfo(String name, String address, int age);
}
