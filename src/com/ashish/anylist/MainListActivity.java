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
import android.content.SharedPreferences;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainListActivity extends ListActivity 
{
	
	private ArrayAdapter<MainList> mainList;
	private DBHelper db;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//Transition animation
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_right);
		setContentView(R.layout.activity_main_list);
		context = this;
		
		//Add shortcut to home screen if the app is run for the first time
		SharedPreferences prefs = getSharedPreferences("ShortCutPrefs", MODE_PRIVATE);
	    if(!prefs.getBoolean("isFirstTime", false))
	    {
	        addShortcut();
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putBoolean("isFirstTime", true);
	        editor.commit();
	    } 
	    
        db = new DBHelper(context);
        mainList = new ArrayAdapter<MainList>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, db.fetchAllMainLists());
        setListAdapter(mainList);
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
                            	//Delete list and all its elements on swipe
                            	String temp ="";
                                for (int position : reverseSortedPositions) 
                                {
                                	db=new DBHelper(context);
                                	db.deleteListElementofList(mainList.getItem(position).get_id());
                                	db.deleteMainList(mainList.getItem(position).get_id());
                                	temp = mainList.getItem(position).get_name();
                                }
                                mainList.clear();
                	            mainList.addAll(db.fetchAllMainLists());
                	            mainList.notifyDataSetChanged();
                	            db.close();
                	            Toast.makeText(context,"Deleted list " + temp,Toast.LENGTH_SHORT).show();
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
            	 //On long press open alert box to modify the list name
            	 final MainList ml = (MainList)getListAdapter().getItem(position);
                 final EditText input = new EditText(context);
                 input.setText(ml.toString());
         		
         		new AlertDialog.Builder(context)
         	    .setTitle("Modify List Title")
         	    .setMessage("")
         	    .setView(input)
         	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
         	    {
         	        public void onClick(DialogInterface dialog, int whichButton) 
         	        {
         	            Editable name = input.getText();
         	            ml.set_name(name.toString());
         	            db = new DBHelper(context);
         	            db.updateMainList(ml);
         	            mainList.clear();
         	            mainList.addAll(db.fetchAllMainLists());
         	            mainList.notifyDataSetChanged();
         	            db.close();
         	            Toast.makeText(context,"Updated list name to " + name.toString(),Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.main_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add:
	            openAddList();
	            return true;
	 
	        case R.id.action_about:
	        	Intent j = new Intent(getApplicationContext(), AboutActivity.class);
	            startActivity(j);
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openAddList()
	{
		//Open alert box to read input from user and create a new list
		final EditText input = new EditText(this);
		
		new AlertDialog.Builder(this)
	    .setTitle("Enter List Title")
	    .setMessage("")
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int whichButton) 
	        {
	            Editable name = input.getText(); 
	            db = new DBHelper(getApplicationContext());
	            MainList ml = new MainList(name.toString());
	            db.createMainList(ml);
	            mainList.clear();
	            mainList.addAll(db.fetchAllMainLists());
	            mainList.notifyDataSetChanged();
	            db.close();
	            Toast.makeText(context,"Added new list " + name.toString(),Toast.LENGTH_SHORT).show();
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int whichButton) 
	        {
	            // Do nothing.
	        }
	    }).show();
	}

	@Override
    protected void onListItemClick(ListView listView, View view, int position, long id) 
	{
		//Open the list elements of the list clicked
        Intent i = new Intent(getApplicationContext(), ListElementActivity.class);
        i.putExtra("list_id",((MainList)getListAdapter().getItem(position)).get_id());
        i.putExtra("list_name",((MainList)getListAdapter().getItem(position)).get_name());
        startActivity(i);    
    }
	
	private void addShortcut() 
	{
	    //Adding shortcut for app on home screen
	    Intent shortcutIntent = new Intent(getApplicationContext(),MainListActivity.class);
	    shortcutIntent.setAction(Intent.ACTION_MAIN);
	    Intent addIntent = new Intent();
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Any List");
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(getApplicationContext(),R.drawable.ic_launcher));
	    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	    context.sendBroadcast(addIntent);
	}
}
