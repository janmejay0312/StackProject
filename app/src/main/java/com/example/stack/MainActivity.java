package com.example.stack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
ArrayList<String> list=new ArrayList<String>();
ListView listView;
    private ArrayAdapter<String> mArrayAdapter;
EditText   editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
listView=findViewById(R.id.listview);

        Button buttonParse = findViewById(R.id.button_parse);

        mQueue = Volley.newRequestQueue(this);
        mArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,android.R.id.text1, list);
        listView.setAdapter(mArrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
            ////    String value=mArrayAdapter.getItem(position);
               // mTextViewResult.setText(value);

            }
        });

        listView.setItemsCanFocus(false);
        String url = "https://api.stackexchange.com/2.2/tags?order=desc&sort=popular&site=stackoverflow";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject stacksub = jsonArray.getJSONObject(i);

                                String firstName = stacksub.getString("name");
                                list.add(firstName);



                            }
                            mArrayAdapter.notifyDataSetChanged();

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
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p=0;
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position1 = checked.keyAt(i);
                    // Add item if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i)&&p<4){
                        selectedItems.add(mArrayAdapter.getItem(position1));
                        p++;
                    }
                }
                String[] outputStrArr = new String[selectedItems.size()];

                for (int i = 0; i < selectedItems.size(); i++) {
                    outputStrArr[i] = selectedItems.get(i);
                }

                Intent intent = new Intent(getApplicationContext(),
                        Questionaire.class);

                // Create a bundle object
                Bundle b = new Bundle();
                b.putStringArray("selectedItems", outputStrArr);

                // Add the bundle to the intent.
                intent.putExtras(b);

                // start the ResultActivity
                startActivity(intent);
            }
        });
    }


}


