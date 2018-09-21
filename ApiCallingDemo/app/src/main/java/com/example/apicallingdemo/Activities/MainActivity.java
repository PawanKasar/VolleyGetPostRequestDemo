package com.example.apicallingdemo.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.apicallingdemo.Adapters.RecyclerViewAdapter;
import com.example.apicallingdemo.Adapters.ViewPagerAdapter;
import com.example.apicallingdemo.Interface.DrawableClickListener;
import com.example.apicallingdemo.Model.DataModelClass;
import com.example.apicallingdemo.R;
import com.example.apicallingdemo.Session.SessionManager;
import com.example.apicallingdemo.Utilities.ModEditText;
import com.example.apicallingdemo.Utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<DataModelClass> dataModelClassArrayList;
    DataModelClass dataModelClass;
    RecyclerView recyclerView;

    // Session Manager Class
    SessionManager session;

    ViewPagerAdapter viewPagerAdapter;
    //ViewPager viewPager;
    EditText edt_contact;
    ModEditText modEditText;
    private static final int PICK_CONTACT = 1000;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        recyclerView = findViewById(R.id.rceyclerview);
        dataModelClassArrayList = new ArrayList<>();

        //checking User Login
        session.checkLogin();

        // Fetching JSON data
        callApi();

        viewPagerAdapter = new ViewPagerAdapter(this);

        /*viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);*/

        edt_contact = findViewById(R.id.edit_contact);
        edt_contact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edt_contact.getRight() - edt_contact.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        pickAContactNumber(view);
                        return true;
                    }
                }
                return false;
            }
        });
    }


        public void callApi() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Utils.JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        getResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        // add it to the RequestQueue
        queue.add(jsonObjectRequest);

    }

    public void getResponse(JSONObject jsonObject){
        try {
            JSONObject responseJson = new JSONObject(jsonObject.toString());

            JSONArray jsonArray = responseJson.getJSONArray("heroes");

            for (int i = 0; i < jsonArray.length(); i++){
                if (jsonArray.isNull(i)){
                    Toast.makeText(MainActivity.this,"No Data Present",Toast.LENGTH_SHORT).show();
                }else{
                    //getting the json object of the particular index inside the array
                    JSONObject heroObject = jsonArray.getJSONObject(i);

                    dataModelClass = new DataModelClass();

                    dataModelClass.setName(heroObject.getString("name"));
                    dataModelClass.setImageurl(heroObject.getString("imageurl"));

                    dataModelClassArrayList.add(dataModelClass);
                }
            }

            RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataModelClassArrayList,dataModelClass,getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new RecyclerViewAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Log.d("MainActivity", "onItemClick position: " + position);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_logout:
                logOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut(){
        // Clear the session data
        // This will clear all session data and
        // redirect user to LoginActivity
        session.logoutUser();
    }

    //Todo when button is clicked
    public void pickAContactNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    @SuppressLint("Recycle") Cursor phone = null;
                    if (contactData != null) {
                        phone = getContentResolver().query(contactData, null, null, null, null);
                    }
                    if (phone != null && phone.moveToFirst()) {
                        String contactNumberName = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // Todo something when contact number selected
                        edt_contact.setText(contactNumberName);
                        Log.d("MainActivity", "Getting Contact Name " + contactNumberName);
                    }
                }
                break;
        }
    }
}
