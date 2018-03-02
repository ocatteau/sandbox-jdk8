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

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Foo {
	public String name;
	public List<Bar> bars = new ArrayList<>();

	public Foo(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Foo{");
		sb.append("name='").append(name).append('\'');
		sb.append(", bars=").append(bars);
		sb.append('}');
		return sb.toString();
	}
}
