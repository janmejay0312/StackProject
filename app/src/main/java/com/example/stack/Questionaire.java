package com.example.stack;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Questionaire extends AppCompatActivity {
    private RequestQueue mQueue;
    private TextView textView;
   private ArrayList<String> list=new ArrayList<String>();
   private ListView listView;
   private ListView listView1;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    private List<String> listOflinks=new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);
       listView=findViewById(R.id.list1View1);
       dl=findViewById(R.id.activity_questionaire);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();
        nv=findViewById(R.id.nv);
        listView1=nv.findViewById(R.id.menuList);
       nv.removeHeaderView(nv.getHeaderView(0));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mQueue = Volley.newRequestQueue(this);
        Bundle b = getIntent().getExtras();
        String[] resultArr = b.getStringArray("selectedItems");
        adapter = new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1,android.R.id.text1, list);
        listView.setAdapter(adapter);
        adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1, resultArr);
        listView1.setAdapter(adapter1);
        String selectedTags="";
        for(int i=0;i<resultArr.length;i++){
            selectedTags=selectedTags+resultArr[i]+";";
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
               // String value=adapter.getTransitionN(position);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(listOflinks.get(position)));
                startActivity(i);
            }
        });
        String url = "https://api.stackexchange.com/2.2/questions?order=desc&sort=activity&site=stackoverflow"+"&tagged="+selectedTags;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            String title="";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject stackques = jsonArray.getJSONObject(i);

                                String link=stackques.getString("link");
                                listOflinks.add(link);
                                title = stackques.getString("title");
                                list.add(title);
                                    adapter.notifyDataSetChanged();

                            }
                            //mArrayAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
if(t.onOptionsItemSelected(menuItem))
    return true;
return super.onOptionsItemSelected(menuItem);
    }
}
