package com.feature.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * source --> stream --> immediate functions --> terminal functions
 * Main interfaces :
 * - Optional : wrap to avoid null pointer
 * - Predicate : one or more than one arguments and return boolean
 * - Function<T,R> : accept one argument and produces a result R
 * - Consumer: accepts one more than one arguments and return void
 * - Operator: argument and return are same type
 * 
 * Main immediate functions: 
 * - filter
 * - map
 * - sort
 * 
 * 
 * @author linhpham
 *
 */
public class StreamFeature {

	public static class Staff {

		private int age;
		private String name;
		private String jobTittle;

		public Staff(int age, String name, String jobTittle) {
			this.age = age;
			this.name = name;
			this.jobTittle = jobTittle;
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

	public static void main(String args[]) {
		staffList().stream()
		.filter(staff -> staff.getAge() > 25)
		.map(staff -> staff.getName())
		.sorted((str1, str2) -> str1.compareTo(str2))
		.forEach(action -> System.out.println("name :" + action));
		
		staffList().parallelStream().skip(1).forEach(str -> System.out.println(str.getName()));
	}
	

	public static List<Staff> staffList() {
		List<Staff> staffs = new ArrayList<Staff>();
		staffs.add(new Staff(25, "Staff 2", "Dev 2"));
		staffs.add(new Staff(23, "Staff 1", "Dev 1"));
		staffs.add(new Staff(27, "Staff 4", "Dev 4"));
		staffs.add(new Staff(27, "Staff 3", "Dev 3"));
		
		return staffs;
	}
}
