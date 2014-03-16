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
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class AboutActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitle("Any List");
		
		String str="<html><body style=\"text-align:justify\"><p>Any List, as the name suggests can be used to create any kind of list. Just tap the \'+\' on the action bar to add a list. Click on the list and do the same to add elements to the list. Swipe away to delete and long press to edit.</p></body></html>";
		WebView web=(WebView)findViewById(R.id.wv1);
		web.loadData(str,"text/html","utf-8");
		web.setBackgroundColor(Color.TRANSPARENT);
		
		String str2="<html><body style=\"text-align:justify\"><p>The code will be soon made open source and will be available on GitHub. You can report bugs or suggest improvements by email to \'ashish.gudla@gmail.com\'.</p> </body></html>";
		WebView web2=(WebView)findViewById(R.id.wv2);
		web2.loadData(str2,"text/html","utf-8");
		web2.setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle presses on the action bar items
	    switch (item.getItemId()) 
	    {
	            
	        case R.id.action_home:
	        	Intent j = new Intent(getApplicationContext(), MainListActivity.class);
	            startActivity(j);
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
