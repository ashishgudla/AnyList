/*
 * Copyright 2014 AshishGudla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ashish.anylist;

public class MainList {

	int id;
	String name;
	String created_on;
	String modified_on;
	
	@Override
	public String toString()
	{
		return this.name;
	}
	public MainList()
	{
	}
	
	public MainList(String name)
	{
		this.name=name;
	}
	
	public MainList(String name, String created_on,String modified_on)
	{
		this.name=name;
		this.created_on = created_on;
		this.modified_on = modified_on;
	}
	
	public void set_id(int id)
	{
		this.id=id;
	}
	
	public void set_name(String name)
	{
		this.name = name;
	}
	
	public void set_created_on(String created_on)
	{
		this.created_on = created_on;
	}
	
	public void set_modified_on(String modified_on)
	{
		this.modified_on = modified_on;
	}
	
	
	public int get_id()
	{
		return this.id;
	}
	
	public String get_name()
	{
		return this.name;
	}
	
	public String get_created_on()
	{
		return this.created_on;
	}
	
	public String get_modified_on()
	{
		return this.modified_on;
	}
	
}
