package com.feature.foreach;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author linhpham
 * 
 *         foreach feature in java 8. much faster than previous jdk due to <b> internal loop</b>
 * 
 *         before java 1.8, use <b>external loop</b>. loop outside of collection
 *         class java 1.8, using <b>internal loop</b> from collection
 * 
 */
public class ForEachFeature {

	public static void main(String args[]) {
		List<String> values = Arrays.asList("A", "B", "C", "D");

		values.forEach(i -> {
			System.out.println(i);
		});
	}
}
