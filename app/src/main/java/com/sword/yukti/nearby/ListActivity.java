package com.sword.yukti.nearby;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
  public ArrayList< Getter_setter> list;
    MapsListAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView=(ListView)findViewById(R.id.list_view);
        list=new ArrayList<Getter_setter>();

       list=(ArrayList<Getter_setter>)getIntent().getSerializableExtra("Maps_list");
        adapter = new MapsListAdapter(ListActivity.this,  list);
        listView.setAdapter(adapter);


    }
}
