package com.example.gyumi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG=MainActivity.class.getName();
    private static final int secret=69;
    private static final String prefkey=RegisterActivity.class.getPackage().toString();
    private SharedPreferences pref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private NotificationHandler mNotificationHandler;
    EditText emailET;
    EditText passwordET;
    private Button b;
    private Animation rAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailET=findViewById(R.id.Email);
        passwordET=findViewById(R.id.Password);

        pref=getSharedPreferences(prefkey,MODE_PRIVATE);
        auth=FirebaseAuth.getInstance();
        mNotificationHandler=new NotificationHandler(this);
        rAnimation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate);
        b= findViewById(R.id.login);
    }

    @Override
    protected void onStart() {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user!=null){
            auth.signOut();
        }
        String regemail = pref.getString("email", "");
        emailET.setText(regemail);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("email","");
        passwordET.setText("");
        editor.apply();
    }

    public void login(View view) {
        String email=emailET.getText().toString();
        String password=passwordET.getText().toString();

        Intent loginIntent=new Intent(this,ShopActivity.class);
        loginIntent.putExtra("secret",secret);
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this,"Minden mező kitöltése kötelező!",Toast.LENGTH_LONG).show();
            b.startAnimation(rAnimation);
            return;
        }
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                startActivity(loginIntent);
                mNotificationHandler.send("Friss gyümölcs érdekel?");
            }else{
                b.startAnimation(rAnimation);
                Log.d(LOG_TAG,"Hiba!");
                Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(View view) {
        Intent intent=new Intent(this,RegisterActivity.class);
        intent.putExtra("secret",secret);
        startActivity(intent);
    }
}