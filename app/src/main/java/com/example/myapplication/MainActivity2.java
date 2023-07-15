package com.example.myapplication;

import static com.example.myapplication.SecondFragment.states;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMain2Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    public static String hhh = "";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMain2Binding binding;

    String id_mol;
    String code;
    String nom ;
    String valuta ;
    String narkhi_o;
    String narkhi_f ;
    String id_kurb ;
    String bokimonda_m ;


    double kurs_tjs = MainActivity.tjs;
    double kurs_usd = MainActivity.usd;
    double kurs_rub = MainActivity.rub;
    double kurs_eur = MainActivity.eur;
    double kurs_yuan = MainActivity.yuan;
    double nf;
    double no;

    EditText etMikdor;
    EditText etNarkh;
    EditText etSumma;
    Button addBtn;
    Button cancelBtn;
    ListView lists;
  //  FloatingActionButton fab;

    @Override
    protected void onStart() {
        super.onStart();

        if (hhh!=("")){
            find();
        }
        hhh = "";
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),scannerView.class));
//            }
//        });



    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void find(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Загрузка...");
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity2.this);
        String urlll=MainActivity.ip+"/find/"+hhh;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    String status  = jsonObject.getString("status");
//                    String msg = jsonObject.getString("message");
//                    String nameUser = jsonObject.getString("name");


                    if (jsonObject.has("status")){
                       // Toast.makeText(MainActivity2.this,"# - " +jsonObject.getString( "nom") + "!", Toast.LENGTH_SHORT).show();
//

                         id_mol = jsonObject.getString("id_mol").toString();
                         code = jsonObject.getString("kod").toString();
                         nom = jsonObject.getString("nom").toString();
                         valuta = jsonObject.getString("valuta").toString();
                         narkhi_o = jsonObject.getString("narkhi_o").toString();
                         narkhi_f = jsonObject.getString("narkhi_f").toString();
                         id_kurb = jsonObject.getString("id_kurb").toString();
                         bokimonda_m = jsonObject.getString("bokimonda_m").toString();
                        hhh="";
                        progressDialog.dismiss();


                        if(id_kurb.equals("1") ){
                            nf = Double.parseDouble(narkhi_f) * kurs_tjs;
                            no = Double.parseDouble(narkhi_o) * kurs_tjs;
                        }else if(id_kurb.equals("2")){
                            nf = Double.parseDouble(narkhi_f) * kurs_usd;
                            no = Double.parseDouble(narkhi_o) * kurs_usd;
                        }else if(id_kurb.equals("3")){
                            nf = Double.parseDouble(narkhi_f) * kurs_rub;
                            no = Double.parseDouble(narkhi_o) * kurs_rub;
                        }else if(id_kurb.equals("4")){
                            nf = Double.parseDouble(narkhi_f) * kurs_yuan;
                            no = Double.parseDouble(narkhi_o) * kurs_yuan;
                        }else if(id_kurb.equals("5")){
                            nf = Double.parseDouble(narkhi_f) * kurs_eur;
                            no = Double.parseDouble(narkhi_o) * kurs_eur;
                        }else{
                            nf =  Double.parseDouble(narkhi_f);
                            no = Double.parseDouble(narkhi_o);
                        }

                        nf =  Math.round(nf*100.0)/100.0;
                        no =  Math.round(no*100.0)/100.0;


                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity2.this);
                        View mView = getLayoutInflater().inflate(R.layout.hisob_dialog, null);
                        etNarkh = (EditText) mView.findViewById(R.id.editTextNumberDecimal2);
                        etMikdor = (EditText) mView.findViewById(R.id.editTextNumberDecimal3);
                        etSumma = (EditText) mView.findViewById(R.id.editTextNumberDecimal4);
                        cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
                        addBtn = (Button) mView.findViewById(R.id.addBtn);

                        etNarkh.setText(String.valueOf(nf).toString());


                        etMikdor.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                try {
                                    if(etNarkh.getText().toString().equals("0") || etMikdor.getText().toString().equals("0") || etMikdor.getText().toString().equals("") || etNarkh.getText().toString().equals("") ){
                                        Context context = MainActivity2.this;
                                        CharSequence text = "Введите количество и цена продажи";
                                        int duration = Toast.LENGTH_SHORT;
                                        addBtn.setEnabled(false);
//                                    Toast toast = Toast.makeText(context, text, duration);
//                                    toast.show();
                                    }else{
                                        if(no <= Double.parseDouble(etNarkh.getText().toString())){
                                            etSumma.setText(String.valueOf(Math.round(Double.valueOf(etMikdor.getText().toString()) * Double.valueOf(etNarkh.getText().toString()) * 100.0)/100.0));
                                            addBtn.setEnabled(true);
                                        }else{
                                            addBtn.setEnabled(false);
                                        }

                                    }
                                }catch (Exception e){
//                                Context context = getContext().getApplicationContext();
//                                CharSequence text = e.getMessage().toString();
//                                int duration = Toast.LENGTH_SHORT;
//
//                                Toast toast = Toast.makeText(context, text, duration);
//                                toast.show();
                                    addBtn.setEnabled(false);
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        etNarkh.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                try {
                                    if(etNarkh.getText().toString().equals("0") || etMikdor.getText().toString().equals("0") || etMikdor.getText().toString().equals("") || etNarkh.getText().toString().equals("")){
                                        Context context = MainActivity2.this;
                                        CharSequence text = "Введите количество и цена продажи";
                                        int duration = Toast.LENGTH_SHORT;
                                        addBtn.setEnabled(false);
//                                    Toast toast = Toast.makeText(context, text, duration);
//                                    toast.show();
                                    }else{
                                        if(no <= Double.parseDouble(etNarkh.getText().toString())){
                                            etSumma.setText(String.valueOf(Math.round(Double.valueOf(etMikdor.getText().toString()) * Double.valueOf(etNarkh.getText().toString()) * 100.0)/100.0));
                                            addBtn.setEnabled(true);
                                        }else{
                                            addBtn.setEnabled(false);
                                        }

                                    }
                                }catch (Exception e){
//                                Context context = getContext().getApplicationContext();
//                                CharSequence text = e.getMessage().toString();
//                                int duration = Toast.LENGTH_SHORT;
//
//                                Toast toast = Toast.makeText(context, text, duration);
//                                toast.show();
                                    addBtn.setEnabled(false);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });


                        mBuilder.setView(mView);
                        mBuilder.setTitle(nom);
                        mBuilder.setIcon(R.drawable.cartt);
                        mBuilder.setCancelable(false);
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        etMikdor.requestFocus();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);



                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                nf = 0;
                                no = 0;

                                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                            }
                        });

                        addBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(etNarkh.getWindowToken(), 0);

                                if(no <= Double.parseDouble(etNarkh.getText().toString()) ){

                                    states.add(new State(nom, etMikdor.getText().toString(), etNarkh.getText().toString(), code, id_mol, etSumma.getText().toString(), R.drawable.good ));
                                    lists = (ListView) findViewById(R.id.lists);

                                    StateAdapter stateAdapter = new StateAdapter(MainActivity2.this, R.layout.list_item, SecondFragment.states);
                                    lists.setAdapter(stateAdapter);

                                    int count = stateAdapter.getCount();
                                    double summa = 0;

                                    for(int i=0; i< count;i++){

                                        double shtuk1 = Double.parseDouble(stateAdapter.getItem(i).getPcs().toString());
                                        double narkh1 = Double.parseDouble(stateAdapter.getItem(i).getNarkh()) ;
                                        double res = shtuk1*narkh1;
                                        summa = summa+res;



                                    }

                                    FirstFragment.sendBtn.setText(String.valueOf(summa)+" TJS");


                                    dialog.dismiss();
                                    if (lists.getCount()!=0){
                                        FirstFragment.sendBtn.setEnabled(true);

                                    }


                                }else{

                                    addBtn.setEnabled(false);
                                    Context context = MainActivity2.this;
                                    CharSequence text = "Продажная цена выше себестоимость товара!";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();

                                }
                            }
                        });

                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity2.this,"Ошибка 1. \n  unable to Parse Json", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity2.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        hhh = "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String, String>();

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
    }
}