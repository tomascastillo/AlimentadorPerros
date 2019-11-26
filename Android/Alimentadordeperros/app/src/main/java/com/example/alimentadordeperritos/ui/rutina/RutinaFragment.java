package com.example.alimentadordeperritos.ui.rutina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.alimentadordeperritos.R;

public class RutinaFragment extends Fragment {

    private RutinaViewModel rutinaViewModel;

    private TextView tvRES;

    private ListView listaHorarios;

    private String cadenaRutina="";



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
                  listaHorarios = (ListView) root.findViewById(R.id.listViewHorarios);
/*
                  ArrayList<String> array = new ArrayList<>();
                  array.add("Horario1");
                  array.add("Horario2");
                  array.add("Horario3");*/

        //ArrayList<String> array = getArguments().getStringArrayList("valuesArray");

                //  Bundle b = getArguments().getString("valuesArray");

        //Bundle b= new Bundle(); b.putStringArray("arrayname that use to retrive in frag2",StringArrayObject); Frag2.setArguments(b);
              /*   try{
                     //String str = getArguments().getString("valuesArray");
                     //String str = getArguments().getParcelable(getActivity().getResources().getString());
                     Log.d("MOSTRAR!!!!!!!","-------->"+str);


                 }catch (Exception e){
                     Log.d("---------------->", e.getMessage());
                 }*/


        //ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,array);
        //listaHorarios.setAdapter(adapter);



        //tvRES.setText(cadenaRutina);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getArguments()!=null) {
           cadenaRutina = getArguments().getString("rutina", "KHE");
            tvRES.setText(cadenaRutina);
        }

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
