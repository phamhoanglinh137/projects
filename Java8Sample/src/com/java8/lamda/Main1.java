package com.java8.lamda;

public class Main1 {
	
	public static void main(String args[]) {
		Main1 main1 = new Main1();
		Person p = main1.new Person();
		p.setAge(12);

		CheckPerson checkPerson =  p1 -> p1.getAge() > 0;
		
		System.out.println(checkPerson.checkPerson(p));
		
	}
	
	public class Person {
		int age;
		String name;
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
		
	}
	public interface CheckPerson {
		boolean checkPerson(Person p);
	}
}

