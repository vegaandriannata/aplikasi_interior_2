package com.example.aplikasi_interior.fragment.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("ini rencananya akan jadi histori pemesanan");
    }

    public LiveData<String> getText() {
        return mText;
    }
}