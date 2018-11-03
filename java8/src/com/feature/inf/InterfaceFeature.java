package com.feature.inf;

/**
 * 
 * @author linhpham
 * 
 * https://www.journaldev.com/2752/java-8-interface-changes-static-method-default-method
 * 
 * Interface in java2 has 2 new features
 * 
 * - Default methods 
 *  + low priority than same method in extend class. 
 *  + sub class must define method if it inherits same method in multiple interfaces.
 *  
 * - Static methods
 *  + scope is in interface only.
 *  + implementation class could not override static methods.mc
 * 
 * - same methods with Object class will be hidden.
 *
 */
public class InterfaceFeature {
	
	public static void main(String args[]) {
		System.out.println("-- show method in sub class --");
		SubA subA = new SubA();
		subA.show();
		
		System.out.println("-- show method in interface --");
		SubB subB = new SubB();
		subB.show();
		
		System.out.println("-- static method in interface A --");
		A.showtClassName();
	}
}

/* -- INTERFACE -- */
interface A {
	default void show() {
		System.out.println("A intereface");
	}
	
	static void showtClassName() {
		System.out.println(A.class.getName());
	}
}

interface B {
	default void show() {
		System.out.println("B intereface");
	}
	
	static void showtClassName() {
		System.out.println(B.class.getName());
	}
}

/* -- SUB CLASSES -- */
class SubA implements A, B {
	public void show() {
		System.out.println("Sub class A");
	}
}

class SubB implements B {
	
}