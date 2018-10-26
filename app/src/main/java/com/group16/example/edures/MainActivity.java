package com.group16.example.edures;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Logging in...");
        progressDialog.setMessage("It will take few seconds!!");
        signupBtn= (Button)findViewById(R.id.signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                v.startAnimation(shake);
                startActivity(new Intent(MainActivity.this,Signup.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent i = new Intent(MainActivity.this,FunctionActivity.class);
            startActivity(i);
            this.finish();
        }
    }

    public void login(View v){
        EditText e1 = findViewById(R.id.editText);
        EditText e2 = findViewById(R.id.editText2);
        String email = e1.getText().toString();
        String password = e2.getText().toString();
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        v.startAnimation(shake);

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please Enter Login Details", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
                                // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                                progressDialog.dismiss();
                                checkEmailVerification();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                updateUI(null);
                            }
                        }
                    });
        }

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag){
            finish();
            startActivity(new Intent(MainActivity.this,FunctionActivity.class));
        }
        else{
            Toast.makeText(this, "Please Verify your Email", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
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


}