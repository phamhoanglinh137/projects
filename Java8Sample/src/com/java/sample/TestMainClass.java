package com.java.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;

public class TestMainClass {

	public static void main(String[] args) {
		
//		String name1 ="a";
//		String nameRegex = "^[a-zA-Z .]";
//		Pattern namePartten = Pattern.compile(nameRegex);
//		
//		if(namePartten.matcher(name1).matches()) {
//			System.out.println("matched");
//		}
//		
//		String name = ". adf H 3ass";
//		String[] nameArr = name.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
//		for (String string : nameArr) {
//			System.out.println(string);
//		}
		
		
		List<String> listOne = new ArrayList(Arrays.asList("1","a","a","c","b"));
	    //List<String> listTwo = new ArrayList(Arrays.asList("b","c","c","a","1"));
	    List<String> listTwo = new ArrayList(Arrays.asList("b","c","a","a","1"));
	    
	    System.out.println(compareByCollection(listOne, listTwo));
	    //System.out.println(compareList(listOne, listTwo));
	}
	
	public static boolean compareList(List<String> ls1, List<String> ls2) {
		Collections.sort(ls1);
	    Collections.sort(ls2);
	    if(ls1.size() == ls2.size()) {
	    	for(int i = 0; i< ls1.size(); i ++) {
	    		if(!ls1.get(i).equalsIgnoreCase(ls2.get(i))) return false;
	    	}
	    	return true;
	    }
	    return false;
	}
	
	public static boolean compareByCollection(List<String> ls1, List<String> ls2) {
		return CollectionUtils.isEqualCollection(ls1, ls2);
	}

}
