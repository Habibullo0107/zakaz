package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.FragmentSecondBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SecondFragment extends Fragment {
    String ip = "http://192.168.90.21/public/api";

    double kurs_tjs = MainActivity.tjs;
    double kurs_usd = MainActivity.usd;
    double kurs_rub = MainActivity.rub;
    double kurs_eur = MainActivity.eur;
    double kurs_yuan = MainActivity.yuan;
    double nf;
    double no;

    private FragmentSecondBinding binding;

    ArrayList<String> users = new ArrayList<String>();
    ArrayList<String> selectedUsers = new ArrayList<String>();

    public static ArrayList<State> states = new ArrayList();


    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    ListView userList;
    EditText userFilter;

    EditText etMikdor;
    EditText etNarkh;
    EditText etSumma;
    Button addBtn;
    Button cancelBtn;
    ImageButton downloadBtn;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentSecondBinding.inflate(inflater, container, false);



        return binding.getRoot();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    db = sqlHelper.getReadableDatabase();



                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Загрузка...");
                    progressDialog.show();
                    AppHttp requestQueue = AppHttp.getInstance(getActivity().getApplicationContext());
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("id_user", 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MainActivity.ip+"/all_molho", postData, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                db.execSQL("delete from molho");
                                JSONArray jsonArray = response.getJSONArray("molho");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject employee = jsonArray.getJSONObject(i);
                                    int id_mol = employee.getInt("id_mol");
                                    String nom = employee.getString("nom");
                                    int id_kurb = employee.getInt("id_kurb");
                                    String narkhi_o = employee.getString("narkhi_a");
                                    String narkhi_f = employee.getString("narkhi_f");
                                    String valuta = employee.getString("valuta");
                                    int kod = employee.getInt("kod");
                                    //  System.out.println(nom);
                                    db.execSQL("INSERT INTO molho (id_mol, nom, id_kurb,  kod, narkhi_o, narkhi_f, valuta )  VALUES( "+id_mol+" , '"+ nom +"' , " + id_kurb + " , "+ kod +" , '" + narkhi_o + "' , '"+ narkhi_f+"', '"+ valuta +"' ) "  );



                                }
                                progressDialog.dismiss();


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("login error: ", error.toString());
                                    error.printStackTrace();


                                    // show user login error with a Toast
                                }
                            }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Accept", "application/json;charset=utf-8");
                            params.put("Content-Type", "application/json;charset=utf-8");

                            return params;
                        }
                    };

                    requestQueue.addToRequestQueue(jsonObjectRequest);
                }catch (Exception e){
                    System.out.println(e.getMessage().toString());
                }

            }
        });



        try {

            db = sqlHelper.getReadableDatabase();

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
            String[] headers = new String[]{DatabaseHelper.COLUMN_NOM, DatabaseHelper.COLUMN_KOD};
            userAdapter = new SimpleCursorAdapter(this.getActivity(), android.R.layout.two_line_list_item,
                    userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

            // если в текстовом поле есть текст, выполняем фильтрацию
            // данная проверка нужна при переходе от одной ориентации экрана к другой
            if(!userFilter.getText().toString().isEmpty())
                userAdapter.getFilter().filter(userFilter.getText().toString());

            // установка слушателя изменения текста
            userFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                // при изменении текста выполняем фильтрацию
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    userAdapter.getFilter().filter(s.toString());
                }
            });

            // устанавливаем провайдер фильтрации
            userAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {

                    String input = constraint.toString();


                    if (constraint == null || constraint.length() == 0) {

                        return db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
                    }
                    else {
                        try {
                            Integer.parseInt(input);
                            return db.rawQuery("select * from " + DatabaseHelper.TABLE + " where kod like ? ", new String[]{"%" + constraint.toString() + "%"});

                        }catch (NumberFormatException e){

                            return db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                                    DatabaseHelper.COLUMN_NOM + " like ? ", new String[]{"%" + constraint.toString() + "%"});
                        }



                    }
                }
            });

            userList.setAdapter(userAdapter);



            // обработка установки и снятия отметки в списке
            userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("MissingInflatedId")
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    userList.getItemAtPosition(position);



                    // System.out.println(userAdapter.getCursor().getString(1).toString());
                    String id_mol = userAdapter.getCursor().getString(1).toString();
                    String kod = userAdapter.getCursor().getString(2).toString();
                    String nom = userAdapter.getCursor().getString(3).toString();
                    String valuta = userAdapter.getCursor().getString(4).toString();
                    String narkhi_o = userAdapter.getCursor().getString(5).toString();
                    String narkhi_f = userAdapter.getCursor().getString(6).toString();
                    String id_kurb = userAdapter.getCursor().getString(7).toString();

                    //       CustomDialogFragment dialog = new CustomDialogFragment();



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


                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
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
                                    Context context = getContext().getApplicationContext();
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
                                    Context context = getContext().getApplicationContext();
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

                    //        etSumma.setText(String.valueOf(Double.parseDouble(etMikdor.getText().toString()) * Double.parseDouble(etNarkh.getText().toString()) ).toString());


                    mBuilder.setView(mView);
                    mBuilder.setTitle(nom);
                    mBuilder.setIcon(R.drawable.cartt);
                    mBuilder.setCancelable(false);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    etMikdor.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            nf = 0;
                            no = 0;

                            InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etMikdor.getWindowToken(), 0);
                        }
                    });

                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etMikdor.getWindowToken(), 0);

                            if(no <= Double.parseDouble(etNarkh.getText().toString()) ){

                                states.add(new State(nom, etMikdor.getText().toString(), etNarkh.getText().toString(), kod, id_mol, etSumma.getText().toString(), R.drawable.good ));
                                dialog.dismiss();




                            }else{

                                addBtn.setEnabled(false);
                                Context context = getContext().getApplicationContext();
                                CharSequence text = "Продажная цена выше себестоимость товара!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();

                            }




                        }
                    });


                }
            });

        }catch (SQLException ex){}
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });

        userList = (ListView) getView().findViewById(R.id.userList);
        userFilter = (EditText)getView().findViewById(R.id.userFilter);
        downloadBtn = (ImageButton) getView().findViewById(R.id.imageButton);

        sqlHelper = new DatabaseHelper(getActivity().getApplicationContext());



        

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();

        binding = null;
    }

}