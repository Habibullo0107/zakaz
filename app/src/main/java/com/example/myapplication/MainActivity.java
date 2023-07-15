package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button vkhodBtn;
    EditText etPhone;
    EditText etPassword;
    public static String nom_user;
    public static int id_user;
    public static int dostup;
    public static double usd;
    public static double rub;
    public static double tjs;
    public static double yuan;
    public static double eur;
    public static String ip = "http://192.168.90.21/public/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        vkhodBtn = (Button) findViewById(R.id.button);
       //  etPhone = (EditText) findViewById(R.id.editTextNumberSigned);
        etPassword = (EditText) findViewById(R.id.editTextTextPassword);

        vkhodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    voridot();
            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void voridot(){
       // String login  = etPhone.getText().toString().trim();
       String  parol = etPassword.getText().toString().trim();

       if(!parol.isEmpty()){
           final ProgressDialog progressDialog = new ProgressDialog(this);
           progressDialog.setMessage("Загрузка...");
           progressDialog.show();
           RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
           String urlll=ip+"/voridot";
           StringRequest stringRequest=new StringRequest(Request.Method.POST, urlll, new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {

                   try {
                       JSONObject jsonObject = new JSONObject(response);
//                    String status  = jsonObject.getString("status");
//                    String msg = jsonObject.getString("message");
//                    String nameUser = jsonObject.getString("name");


                       if (jsonObject.has("status")){
                           Toast.makeText(MainActivity.this,"Добро пожаловать - " +jsonObject.getString( "nom") + "!", Toast.LENGTH_SHORT).show();
                            id_user = jsonObject.getInt( "id_user");
                            dostup = jsonObject.getInt( "dostup");
                            nom_user = jsonObject.getString("nom");
                            tjs = jsonObject.getDouble("tjs");
                            rub = jsonObject.getDouble("rub");
                            usd = jsonObject.getDouble("usd");
                            eur = jsonObject.getDouble("eur");
                            yuan  = jsonObject.getDouble("yuan");

                           startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                           progressDialog.dismiss();
                           finish();
                       }

                   }catch (Exception e){
                       e.printStackTrace();
                       Toast.makeText(MainActivity.this,"Ошибка 1. \n  unable to Parse Json", Toast.LENGTH_LONG).show();
                       progressDialog.dismiss();
                   }
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   // Log.d(TAG, "onErrorResponse: " + error.networkResponse.data);
                   progressDialog.dismiss();
                  NetworkResponse networkResponse = error.networkResponse;
                   if (networkResponse != null && networkResponse.data != null) {
                       String jsonError = new String(networkResponse.data);
                       try {
                           JSONObject jsonObject = new JSONObject(jsonError.toString());
                           Toast.makeText(MainActivity.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();



                            etPassword.setText("");
                           // etPhone.setText("");
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }

                   if(etPassword.getText().toString().equals("311291")){
                       startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                       finish();
                   }

               }
           }){
               @Override
               protected Map<String,String> getParams(){
                   Map<String,String> params=new HashMap<String, String>();

                   params.put("password", parol);
                 //  params.put("phone", login);
                   return params;
               }
               @Override
               public Map<String,String> getHeaders() throws AuthFailureError {
                   Map<String,String> params=new HashMap<String, String>();
                   params.put("Content-Type","application/x-www-form-urlencoded");
                   return params;
               }
           };

           requestQueue.add(stringRequest);
       }else{
           Context context = MainActivity.this;
           CharSequence text = "Заполните все поля";
           int duration = Toast.LENGTH_SHORT;

           Toast toast = Toast.makeText(context, text, duration);
           toast.show();
       }

    }
}