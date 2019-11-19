package com.example.alimentadordeperritos.ui.recargar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.alimentadordeperritos.R;



public class RecargarFragment extends Fragment {

    private RecargarViewModel recargarViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recargarViewModel =
                ViewModelProviders.of(this).get(RecargarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recargar, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        recargarViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    } //onCreateView



}//FRAGMENT