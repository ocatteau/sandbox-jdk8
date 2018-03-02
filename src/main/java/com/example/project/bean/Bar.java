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
public class Bar {
	public String name;

	public Bar(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Bar{");
		sb.append("name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
