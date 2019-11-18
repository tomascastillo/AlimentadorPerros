package com.example.alimentadordeperritos.ui.comidas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.alimentadordeperritos.R;

public class ComidasFragment extends Fragment {

    private ComidasViewModel comidasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        comidasViewModel =
                ViewModelProviders.of(this).get(ComidasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_comidas, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        comidasViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}