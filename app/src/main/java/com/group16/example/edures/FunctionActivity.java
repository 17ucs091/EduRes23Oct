package com.group16.example.edures;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class FunctionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        mAuth = FirebaseAuth.getInstance();
    }

    public void note(View v){
        if(isNetworkAvailable()) {
            Intent i = new Intent(FunctionActivity.this, Notes_activity.class);
            startActivity(i);
        }else{
            Toast.makeText(this,"Network Unavailable!",Toast.LENGTH_SHORT).show();
        }
    }

    public void question(View v){
        if(isNetworkAvailable()) {
            Intent i = new Intent(FunctionActivity.this,Question_activity.class);
            startActivity(i);
        }else{
            Toast.makeText(this,"Network Unavailable!",Toast.LENGTH_SHORT).show();
        }
    }

    public void forum(View v){
        if(isNetworkAvailable()) {
            Intent i = new Intent(FunctionActivity.this,QueryActivity.class);
            startActivity(i);
        }else{
            Toast.makeText(this,"Network Unavailable!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications");
                mAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(FunctionActivity.this,MainActivity.class));
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(FunctionActivity.this);
        builder.setMessage("You are quitting the app\nAre you sure?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}