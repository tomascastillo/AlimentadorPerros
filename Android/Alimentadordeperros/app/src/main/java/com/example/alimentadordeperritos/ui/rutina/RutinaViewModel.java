package com.example.alimentadordeperritos.ui.rutina;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RutinaViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public RutinaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mostrar rutina");
    }

    public void init(){
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void sendMessage(String msg){
        mText.setValue(msg);
    }
}
