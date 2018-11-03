package com.feature.method.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 
 * @author linhpham
 *	
 * https://www.codementor.io/eh3rrera/using-java-8-method-reference-du10866vx
 */
public class MethodReferenceMain {
	public static boolean isMoreThanFifty(int n1, int n2) {
	    return (n1 + n2) > 50;
	  }
	  public static List<Integer> findNumbers(
	    List<Integer> l, BiPredicate<Integer, Integer> p) {
	      List<Integer> newList = new ArrayList<>();
	      for(Integer i : l) {
	        if(p.test(i, i + 10)) {
	          newList.add(i);
	        }
	      }
	      return newList;
	  }
	  
	public static void main(String args[]) {
		List<Integer> list = Arrays.asList(12,5,45,18,33,24,40);
		findNumbers(list, MethodReferenceMain::isMoreThanFifty);
	}
}
