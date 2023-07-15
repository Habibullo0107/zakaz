package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;



public class CustomDialogFragment extends DialogFragment {

    EditText etMikdor;
    EditText etNarkh;
    EditText etSumma;
    Button addBtn;


    double kurs_tjs = MainActivity.tjs;
    double kurs_usd = MainActivity.usd;
    double kurs_rub = MainActivity.rub;
    double kurs_eur = MainActivity.eur;
    double kurs_yuan = MainActivity.yuan;
    double nf;



    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        String id_mol = getArguments().getString("id_mol");
        String kod = getArguments().getString("kod");
        String nom = getArguments().getString("nom");
        String valuta = getArguments().getString("valuta");
        String narkhi_o = getArguments().getString("narkhi_o");
        String narkhi_f = getArguments().getString("narkhi_f");
        String id_kurb = getArguments().getString("id_kurb");

        if(id_kurb.equals("1") ){
             nf = Double.parseDouble(narkhi_f) * kurs_tjs;
        }else if(id_kurb.equals("2")){
             nf = Double.parseDouble(narkhi_f) * kurs_usd;
        }else if(id_kurb.equals("3")){
             nf = Double.parseDouble(narkhi_f) * kurs_rub;
        }else if(id_kurb.equals("4")){
             nf = Double.parseDouble(narkhi_f) * kurs_yuan;
        }else if(id_kurb.equals("5")){
             nf = Double.parseDouble(narkhi_f) * kurs_eur;
        }else{
             nf = Double.parseDouble(narkhi_f);
        }

        View mView = getLayoutInflater().inflate(R.layout.hisob_dialog, null);
        etNarkh = (EditText) mView.findViewById(R.id.editTextNumberDecimal2);
        etMikdor = (EditText) mView.findViewById(R.id.editTextNumberDecimal3);
        etSumma = (EditText) mView.findViewById(R.id.editTextNumberDecimal4);
        addBtn = (Button) mView.findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity().getApplicationContext();
                CharSequence text = String.valueOf(nf).toString();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });







        return builder
                .setTitle(nom)
                .setIcon(R.drawable.cartt)
              //  .setMessage("id_mol = " + id_mol + "| kod=" + kod + " | nom = " + nom)
                .setView(R.layout.hisob_dialog)
               // .setPositiveButton("OK", null)
               // .setNegativeButton("Отмена", null)
                .create();




    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);






    }
}
