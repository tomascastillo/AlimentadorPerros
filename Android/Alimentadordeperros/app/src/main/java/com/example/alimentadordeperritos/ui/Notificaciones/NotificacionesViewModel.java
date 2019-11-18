package com.example.alimentadordeperritos.ui.Notificaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificacionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mostrar el historial de notificaciones");
    }

    public LiveData<String> getText() {
        return mText;
    }
}