//--------------------------------------------------------------------------
//Copyright (c) 2008 by LEGISWAY.
//All Rights Reserved.
//
//N O T I C E
//
//THIS MATERIAL IS CONSIDERED A TRADE SECRET BY LEGISWAY.
//UNAUTHORIZED ACCESS, USE, REPRODUCTION OR DISTRIBUTION IS PROHIBITED.
//--------------------------------------------------------------------------
package com.example.project.bean;
/**
 *
 */
public class Person {
	public String name;
	public int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return name;
	}
}
