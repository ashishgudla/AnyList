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

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListElementActivity extends ListActivity 
{

	private ArrayAdapter<ListElement> listElement;
	private DBHelper db;
	private Context context;
	private int list_id;
	private String list_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setTitle("Any List");
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
		setContentView(R.layout.activity_list_element);	
		context=this;
		
		list_id = getIntent().getExtras().getInt("list_id");
		list_name = getIntent().getExtras().getString("list_name");
		
		TextView text = (TextView) findViewById(R.id.listname);
		text.setText(list_name);
		
		db = new DBHelper(getApplicationContext());
        listElement = new ArrayAdapter<ListElement>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, db.fetchAllListElements(list_id));
        setListAdapter(listElement);
        db.close();
        
        ListView listView = getListView();
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() 
                        {
                            @Override
                            public boolean canDismiss(int position) 
                            {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) 
                            {
                            	//Delete list element on swipe
                            	String temp ="";
                                for (int position : reverseSortedPositions) {
                                	db=new DBHelper(context);
                                	db.deleteListElement(listElement.getItem(position).get_id());
                                	temp = listElement.getItem(position).get_element();
                                }
                                listElement.clear();
                                listElement.addAll(db.fetchAllListElements(list_id));
                                listElement.notifyDataSetChanged();
                                db.close();
                	            Toast.makeText(context,"Deleted list element " + temp,Toast.LENGTH_SHORT).show();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
        
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() 
        {
             public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) 
             {
            	 //On long press open alert box to modify list element name
            	 final ListElement le = (ListElement)getListAdapter().getItem(position);
                 final EditText input = new EditText(context);
                 input.setText(le.toString());
         		
         		new AlertDialog.Builder(context)
         	    .setTitle("Modify List Element")
         	    .setMessage("")
         	    .setView(input)
         	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
         	    {
         	        public void onClick(DialogInterface dialog, int whichButton) 
         	        {
         	            Editable name = input.getText();
         	            le.set_elem(name.toString());
         	            db = new DBHelper(context);
         	            db.updateListElement(le);
         	            listElement.clear();
         	            listElement.addAll(db.fetchAllListElements(list_id));
         	            listElement.notifyDataSetChanged();
         	            db.close();
         	            Toast.makeText(context,"Updated element name to " + name.toString(),Toast.LENGTH_SHORT).show();
         	        }
         	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
         	    {
         	        public void onClick(DialogInterface dialog, int whichButton) 
         	        {
         	            // Do nothing.
         	        }
         	    }).show();
                 return true;
             }
         });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_element, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle presses on the action bar items
	    switch (item.getItemId()) 
	    {
	        case R.id.action_add:
	            openAddListElement();
	            return true;
	            
	        case R.id.action_home:
	        	Intent i = new Intent(getApplicationContext(), MainListActivity.class);
	            startActivity(i);
	            return true;
	            
	        case R.id.action_about:
	        	Intent j = new Intent(getApplicationContext(), AboutActivity.class);
	            startActivity(j);
	            return true;
	                   
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openAddListElement()
	{
		//Open alert box to add new list element
		final EditText input = new EditText(this);
		
		new AlertDialog.Builder(this)
	    .setTitle("Enter List Element")
	    .setMessage("")
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int whichButton) 
	        {
	            Editable name = input.getText(); 
	            db = new DBHelper(getApplicationContext());
	            ListElement le = new ListElement(name.toString());
	            le.set_list_id(list_id);
	            db.createListElement(le);
	            listElement.clear();
	            listElement.addAll(db.fetchAllListElements(list_id));
	            listElement.notifyDataSetChanged();
	            db.close();
	            Toast.makeText(context,"Added new list element " + name.toString(),Toast.LENGTH_SHORT).show();
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int whichButton) 
	        {
	            // Do nothing.
	        }
	    }).show();
	}
}
