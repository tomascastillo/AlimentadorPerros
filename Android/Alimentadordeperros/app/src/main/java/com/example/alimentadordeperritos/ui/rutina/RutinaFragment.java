package com.example.alimentadordeperritos.ui.rutina;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alimentadordeperritos.R;

public class RutinaFragment extends Fragment {

    private RutinaViewModel rutinaViewModel;

    private TextView tvRES;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rutinaViewModel =
                ViewModelProviders.of(this).get(RutinaViewModel.class);
        View root = inflater.inflate(R.layout.rutina_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_rutina);
        rutinaViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //String res = "defecto";

              //res  = this.getArguments().getString("abc");


                  //ACA agrego codigo para vistas del fragment
                  tvRES = (TextView) root.findViewById(R.id.textViewRES);//ADD
                  //tvRES.setText("El resultado es: " + res);

        return root;
    }

    //Este metodo de abajo lo tuve que agregar que RutinaFragment puede recibir info de la MainActivity!!
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rutinaViewModel = ViewModelProviders.of(getActivity()).get(RutinaViewModel.class);
        rutinaViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvRES.setText(s);
                //Toast.makeText(getContext(),"ACA: "+s,Toast.LENGTH_LONG).show();
            }
        });
    }
}
