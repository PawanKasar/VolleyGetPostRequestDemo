package com.example.apicallingdemo.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apicallingdemo.R;
import com.example.apicallingdemo.Session.SessionManager;
import com.example.apicallingdemo.Utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edt_email, edt_password;
    Button btn_login, btn_cancel;
    private String login_Token = "";
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        btn_login = findViewById(R.id.btn_login);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_login.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //Here I am checking that user is login or not
        checkIsUserLogin();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btn_login:
                if (edt_email.getText().toString().isEmpty() && edt_password.getText().toString().isEmpty()){
                    edt_email.setError("Please Enter Field");
                    edt_password.setError("Please Enter password");
                }else if (!edt_email.getText().toString().matches(emailPattern)){
                    edt_email.setError("Please Enter Valid Email");
                }else{

                    loginRequestApi();
                }
                break;

            case R.id.btn_cancel:
                break;
        }

    }

    public void loginRequestApi(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Success Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            login_Token = jsonObject.getString("token");
                            if (login_Token.isEmpty()){
                                Toast.makeText(LoginActivity.this,"No Token Present",Toast.LENGTH_SHORT).show();
                            }else{

                                Log.d("MainActivity","login_Token "+login_Token);
                                session.createLoginSession(edt_email.getText().toString(), edt_password.getText().toString());

                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(loginIntent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", edt_email.getText().toString());
                params.put("password", edt_password.getText().toString());

                Log.d("LoginActivity","Data Sending "+params);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        // add it to the RequestQueue
        queue.add(postRequest);
    }

    public void checkIsUserLogin(){
        if (!session.isLoggedIn()){
            Toast.makeText(LoginActivity.this,"Session not stored",Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}
