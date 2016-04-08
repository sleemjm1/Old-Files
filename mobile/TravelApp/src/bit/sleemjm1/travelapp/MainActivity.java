package bit.sleemjm1.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class MainActivity extends Activity 
{
	ListView menuGroupListView;
	String[] groups;
	Class<?>[] classes;	

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        classes = new Class<?>[] {Activities.class, Shopping.class, Dining.class, Services.class };
        setUpMenuList();
        
        ListView menuGroupListView = (ListView) findViewById(R.id.lvNavigation);
        menuGroupListView.setOnItemClickListener(new ListViewClickHandler());
       
    }
    
    public void setUpMenuList()
    {
    	String[] groups =  {"Activities", "Shopping", "Dining", "Services"};
  
    	ArrayAdapter<String> breedGroupAdapter = new ArrayAdapter<String>(this, R.layout.group_list_item, groups);
    	
    	ListView menuGroupListView = (ListView) findViewById(R.id.lvNavigation);
    	menuGroupListView.setAdapter(breedGroupAdapter);
    }
    
    public class ListViewClickHandler implements OnItemClickListener
    {

		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long id) 
		{
			String clickedItemString = list.getItemAtPosition(position).toString();
			Intent goToIntent = new Intent(MainActivity.this, classes[position]);
			
			startActivity(goToIntent);
						
			
			
		}
    	
    }


   
}
