package com.feature.stream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 
 * source --> stream / parallelStream --> intermediate operation --> terminal operation 
 * 
 * Main interfaces : 
 *  - Optional : wrap to avoid null pointer - Predicate : one or more than one arguments and return boolean 
 *  - Function<T,R> : accept one argument and produces a result R 
 *  - Consumer: accepts one more than one arguments and return void 
 *  - Operator: argument and return are same type
 * 
 * Main intermediate functions: 
 * - filter 
 * - map 
 * - sort 
 * - peek
 * - distinct 
 * - flatmap
 * 
 * Main terminal
 * - reduce
 * - collect
 * 
 * @author linhpham
 *
 */
public class StreamFeature {
	public static void main(String args[]) {
		
		intermediateFunc();
		
		flatmap();
		
		reduce();
		
		collect();
		
	}
	
	/**
	 * sample for intermediate functions.
	 * print staff name if age > 5 
	 */
	public static void intermediateFunc() {
		System.out.println("-- Intermediate Func - start");
		
		staffList(10).stream()
		.filter(staff -> staff.getAge() > 5)
		.map(staff -> staff.getName())
		.sorted((str1, str2) -> str1.compareTo(str2))
		.forEach(name -> System.out.println("name :" + name));
		
		System.out.println("-- Intermediate Func-- end");
	}
	
	/**
	 * get all distinct staff's bankcard.
	 */
	public static void flatmap() {
		System.out.println("-- Flat Map -- start");
		
		staffList(5).stream()
				.map(staff -> staff.bankCards)
				.peek(staff -> IntStream.range(1, 5).forEach(j -> staff.add("DBS Credit " + j)))
				.flatMap(str -> str.stream()).distinct()
				.forEach(System.out::println);
		
		System.out.println("-- Flat Map -- end");
	}
	
	/** 
	 * join all distinct credit card name together 
	 * 
	 */
	public static void reduce() {
		System.out.println("-- Reduce - start");
		
		staffList(5).stream()
		.map(staff -> staff.bankCards)
		.peek(staff -> IntStream.range(1, 5).forEach(j -> staff.add("DBS Credit " + j)))
		.flatMap(str -> str.stream()).distinct().reduce((base, card) -> base + card).ifPresent(s -> System.out.println(s));
		
		System.out.println("-- Reduce -- end");
	}
	
	public static void collect() {
		System.out.println("-- Collect - start");
		
		System.out.println("-- Collect - end");
	}
	
	public static void parallel() {
		System.out.println("-- Parallel - start");
		staffList(10).parallelStream().skip(1).forEach(str -> System.out.println(str.getName()));
		System.out.println("-- Parallel - end");
	}
	
	public static List<Staff> staffList(int size) {
		List<Staff> staffs = new ArrayList<Staff>();
		IntStream.range(1, size).forEach(i -> staffs.add(new Staff(i, "Staff " + i, "Dev " + i)));
		return staffs;
	}
	
	public static class Staff {

		private int age;
		private String name;
		private String jobTittle;
		
		private Set<String> bankCards = new HashSet<String>();

		public Staff(int age, String name, String jobTittle) {
			this.age = age;
			this.name = name;
			this.jobTittle = jobTittle;
		}
		
		public void add(String bankCard) {
			bankCards.add(bankCard);
		}
		public Set<String> bankCards() {
			return bankCards;
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

		public String getJobTittle() {
			return jobTittle;
		}

		public void setJobTittle(String jobTittle) {
			this.jobTittle = jobTittle;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + age;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Staff other = (Staff) obj;
			if (age != other.age)
				return false;
			return true;
		}
	}
	
	

}
