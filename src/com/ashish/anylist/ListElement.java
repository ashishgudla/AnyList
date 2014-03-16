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

public class ListElement {
	
	int id;
	int list_id;
	String element;
	
	@Override
	public String toString()
	{
		return this.element;
	}
	public ListElement()
	{	
	}
	
	public ListElement(int list_id,String element)
	{
		this.list_id = list_id;
		this.element = element;
	}
	
	public ListElement(String element)
	{
		this.element= element;
	}
	
	public void set_id(int id)
	{
		this.id=id;
	}
	
	public void set_list_id(int list_id)
	{
		this.list_id=list_id;
	}
	
	public void set_elem(String element)
	{
		this.element = element;
	}
	
	public int get_id()
	{
		return this.id;
	}
	
	public int get_list_id()
	{
		return this.list_id;
	}
	
	public String get_element()
	{
		return this.element;
	}

}
