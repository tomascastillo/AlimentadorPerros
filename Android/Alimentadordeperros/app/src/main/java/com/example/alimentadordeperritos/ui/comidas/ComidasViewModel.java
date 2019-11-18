package com.example.alimentadordeperritos.ui.comidas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComidasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComidasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Cargar el historial de notificaciones ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}