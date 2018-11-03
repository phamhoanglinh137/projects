package com.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {

	public static void main(String[] args) {
		List<Person> personList = new ArrayList<Person>();
		IntStream.range(1, 5).forEach(i -> personList.add(new Person(i, "name" + i, "email")));
		
		List<Person> personListFiltered = personList.stream() 
				  .filter(distinctByKey(p -> p.getName())) 
				  .collect(Collectors.toList());
		
		System.out.println(personListFiltered.size());
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> ke) {
	   
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
	    return t -> {
	    	 System.out.println("-- :" + t);
	    	 return seen.putIfAbsent(ke.apply(t), Boolean.TRUE) == null;
	    	};
	    }

	public static class Person { 
	    private int age; 
	    private String name; 
	    private String email;
	    
	    
		public Person(int age, String name, String email) {
			super();
			this.age = age;
			this.name = name;
			this.email = email;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		} 
	}
	
}
	
