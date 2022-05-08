package com.example.gyumi;

import androidx.appcompat.app.AppCompatActivity;
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

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG=RegisterActivity.class.getName();
    private static final String prefkey=RegisterActivity.class.getPackage().toString();
    private SharedPreferences pref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button b;
    private Animation fAnimation;

    EditText nevET;
    EditText jelszoET;
    EditText jelszoconfirmET;
    EditText emailET;
    EditText teloET;
    EditText cimET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user!=null){
            auth.signOut();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        int secretkey=getIntent().getIntExtra("secret",0);
        if(secretkey!=69){
            finish();
        }

        nevET=findViewById(R.id.Nev);
        jelszoET=findViewById(R.id.Password);
        jelszoconfirmET=findViewById(R.id.Password2);
        emailET=findViewById(R.id.Email);
        teloET=findViewById(R.id.Phone);
        cimET=findViewById(R.id.Cim);

        pref=getSharedPreferences(prefkey,MODE_PRIVATE);
        auth=FirebaseAuth.getInstance();
        fAnimation = AnimationUtils.loadAnimation(RegisterActivity.this,R.anim.fade);
        b=findViewById(R.id.register);
    }



    public void register(View view) {
        String nev=nevET.getText().toString();
        String jelszo=jelszoET.getText().toString();
        String jelszo2=jelszoconfirmET.getText().toString();
        String email=emailET.getText().toString();
        String telo=teloET.getText().toString();
        String cim=cimET.getText().toString();


        if(nev.isEmpty() || jelszo.isEmpty() || jelszo2.isEmpty() || email.isEmpty() || telo.isEmpty() || cim.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Minden mező kitöltése kötelező!",Toast.LENGTH_LONG).show();
            b.startAnimation(fAnimation);
            return;
        }

        if (!jelszo.equals(jelszo2)){
            Toast.makeText(RegisterActivity.this,"A 2 jelszó nem egyezik meg!",Toast.LENGTH_LONG).show();
            b.startAnimation(fAnimation);
            return;
        }


        Log.i(LOG_TAG,nev+" "+jelszo+" "+email+" "+telo+" "+cim);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("email",emailET.getText().toString());
        editor.apply();
        auth.createUserWithEmailAndPassword(email,jelszo).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Log.d(LOG_TAG,"Sikeres Regisztráció!");
                finish();
            }else{
                Log.d(LOG_TAG,"Hiba!");
                Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                b.startAnimation(fAnimation);
                return;
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}