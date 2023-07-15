package com.example.myapplication;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.internal.view.SupportSubMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.FragmentFirstBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment {

    ListView lists;
    public static Button sendBtn;
    ArrayAdapter<String> adapter;
    private FragmentFirstBinding binding;

    public static Map<String, String> ordersmap = new HashMap<>();
    public static ArrayList<String> orders = new ArrayList<String>();
    public static List<Map<String, String>> orderlist = new ArrayList<>();

    public int count;
    public int count1;
    public String[][] massiv;
    FloatingActionButton fab;
    public String comment;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        lists = (ListView) getView().findViewById(R.id.lists);
        sendBtn = (Button) getView().findViewById(R.id.button2);
        StateAdapter stateAdapter = new StateAdapter(getActivity().getApplicationContext(), R.layout.list_item, SecondFragment.states);
        lists.setAdapter(stateAdapter);

        count = stateAdapter.getCount();
        double summa = 0;
        for(int i=0; i< count;i++){


            double shtuk1 = Double.parseDouble(stateAdapter.getItem(i).getPcs().toString());
            double narkh1 = Double.parseDouble(stateAdapter.getItem(i).getNarkh()) ;
            double res = shtuk1*narkh1;
            summa = summa+res;


        }
        sendBtn.setText(String.valueOf(summa)+" TJS");


        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                State selectedState = (State)parent.getItemAtPosition(position);


                android.app.AlertDialog alertDialog_delete = new android.app.AlertDialog.Builder(getActivity()).create();
                // Указываем Title
                alertDialog_delete.setTitle(selectedState.getName());
                // Указываем текст сообщение
                alertDialog_delete.setMessage("Вы действительно хотите удалить данный товар?" );
                // задаем иконку
                alertDialog_delete.setIcon(R.drawable.cartt);
                // Обработчик на нажатие OK
                alertDialog_delete.setButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        stateAdapter.remove(selectedState);
                        lists.setAdapter(stateAdapter);


                        count = stateAdapter.getCount();
                        double summa = 0;
                        for(int i=0; i< count;i++){


                            double shtuk1 = Double.parseDouble(stateAdapter.getItem(i).getPcs().toString());
                            double narkh1 = Double.parseDouble(stateAdapter.getItem(i).getNarkh()) ;
                            double res = shtuk1*narkh1;
                            summa = summa+res;


                        }
                        sendBtn.setText(String.valueOf(summa)+" TJS");


                    }
                });



                // показываем Alert
                alertDialog_delete.show();

            }
        });




        fab = (FloatingActionButton)getView().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),scannerView.class));

            }
        });

        if(lists.getCount()!=0){
            sendBtn.setEnabled(true);
        }


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = stateAdapter.getCount();
                massiv = new String[count][3];

                for(int i=0; i< count;i++){

                    String nom =  stateAdapter.getItem(i).getName();
                    String shtuk =  stateAdapter.getItem(i).getPcs();
                    String mol_id =  stateAdapter.getItem(i).getId_mol();
                    String narkh = stateAdapter.getItem(i).getNarkh();
                    //   String id_user = Integer.toString(MainActivity.id_user);
                    //String getkod =  stateAdapter.getItem(i).getKod();

                    massiv[i][0]= mol_id;
                    massiv[i][1]= shtuk;
                    massiv[i][2]= narkh;
                    //massiv[i][3]= id_user;
                }

                if (massiv!=null){



                    // android.app.AlertDialog alertDialog_tip = new android.app.AlertDialog.Builder(getActivity()).create();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // Указываем Title
                    builder.setTitle("Выберите тип операции");
                    // Указываем текст сообщение
                    //builder.setMessage("Вы действительно хотите удалить данный товар?" );
                    // Set up the input
                    final  EditText input = new EditText(getContext());

                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                    input.setHint("Напишите что-нибудь...");
                    builder.setView(input);

                    // задаем иконку
                    builder.setIcon(R.drawable.cartt);
                    // Обработчик на нажатие OK

                    builder.setPositiveButton("Продажа", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            comment = input.getText().toString();
                            list_send();
                          //  MediaPlayer songSuccess = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.success);
                           // songSuccess.start();
                            //  alert(v);
                            stateAdapter.clear();
                            stateAdapter.notifyDataSetChanged();
                            lists.setAdapter(stateAdapter);
                             sendBtn.setText("Отправить");
                        }
                    });
                    builder.setNegativeButton("В кассу", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            comment = input.getText().toString();
                            list_send1();
                          //  MediaPlayer songSuccess = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.success);
                         //   songSuccess.start();
                            //  alert(v);
                            stateAdapter.clear();
                            stateAdapter.notifyDataSetChanged();
                            lists.setAdapter(stateAdapter);
                             sendBtn.setText("Отправить");
                        }
                    });

                    AlertDialog alert1 = builder.create();
                    alert1.show();




                }else {
                    MediaPlayer songFail = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fail);
                    songFail.start();
                    Toast.makeText(getActivity().getApplicationContext(),"Список пуста", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();



    }

    private void list_send() {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlll=MainActivity.ip+"/list";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")){
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {
                           // alert(getView());
                             Toast.makeText(getActivity().getApplicationContext(),jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            // cancel.callOnClick();
                            massiv = null;
                            MediaPlayer songSuccess = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.success);
                            songSuccess.start();


                        }
                        //etKodNom.setText("");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    massiv = null;
                    Toast.makeText(getActivity().getApplicationContext(),"Ошибка 1. (нет ответа) \n  unable to Parse Json", Toast.LENGTH_SHORT).show();
                   // etKodNom.setText("");
                   // MediaPlayer songFail = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fail);
                   // songFail.start();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonError.toString());
                        Toast.makeText(getActivity().getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String, String>();

                for(int i=0; i< count;i++){
                    params.put("id_mol_"+i,  massiv[i][0]);
                    params.put("shtuk_"+i,  massiv[i][1]);
                    params.put("narkh_"+i, massiv[i][2]);
                }
                params.put("id_user", Integer.toString(MainActivity.id_user));
                params.put("count", String.valueOf(count));
                params.put("tip", "1");
                params.put("comment", comment);

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
    private void list_send1() {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlll=MainActivity.ip+"/list";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")){
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {
                            // alert(getView());
                            Toast.makeText(getActivity().getApplicationContext(),jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            // cancel.callOnClick();
                            massiv = null;
                            MediaPlayer songSuccess = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.success);
                            songSuccess.start();


                        }
                        //etKodNom.setText("");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    massiv = null;
                    Toast.makeText(getActivity().getApplicationContext(),"Ошибка 1. (нет ответа) \n  unable to Parse Json", Toast.LENGTH_SHORT).show();
                    // etKodNom.setText("");
                    // MediaPlayer songFail = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fail);
                    // songFail.start();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonError.toString());
                        Toast.makeText(getActivity().getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String, String>();

                for(int i=0; i< count;i++){
                    params.put("id_mol_"+i,  massiv[i][0]);
                    params.put("shtuk_"+i,  massiv[i][1]);
                    params.put("narkh_"+i, massiv[i][2]);
                }
                params.put("id_user", Integer.toString(MainActivity.id_user));
                params.put("count", String.valueOf(count));
                params.put("tip", "2");
                params.put("comment", comment);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void alert(View v){
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this.getActivity()).create();
        // Указываем Title
        alertDialog.setTitle("Покупка товары");
        // Указываем текст сообщение
        alertDialog.setMessage("Список упешенно отправленно в кассу");
        // задаем иконку
        alertDialog.setIcon(R.drawable.cartt);
        // Обработчик на нажатие OK
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(getActivity().getApplicationContext(),MainActivity2.class));
//                getActivity().finish();

                // Код который выполнится после закрытия окна
                // Toast.makeText(getApplicationContext(), "Вы нажали OK", Toast.LENGTH_SHORT).show();

            }
        });
        // показываем Alert
        alertDialog.show();
    }



}