package com.example.alimentadordeperritos.ui.recargar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecargarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecargarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mostrar shake para alimentar el perro ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}