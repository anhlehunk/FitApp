package com.example.anh.fitapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.makeText;

public class MainActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener{

    private static int RC_SIGN_IN = 0;
    private static String TAG = "MAIN_ACTIVITY";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            //if already logged in
            mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
            String name = mAuth.getCurrentUser().getEmail();
            mDatabase.child("Email").setValue(name);
            TextView currentUser = (TextView) findViewById(R.id.loginInfo);
            currentUser.setText("Welcome,  " + mAuth.getCurrentUser().getDisplayName());

        } else {
            //If not logged in, the login screen will be created
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    //assign a theme to the loginscreen
                    .setTheme(R.style.FirebaseLoginTheme)
                    .setProviders(
                            AuthUI.EMAIL_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                    .build(), RC_SIGN_IN);
            }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            //Check if the login is succesful
            if(resultCode == RESULT_OK){
                String userEmail = mAuth.getCurrentUser().getEmail();
                Toast succesful = makeText(MainActivity.this, "Logged in as: " + userEmail , Toast.LENGTH_SHORT);
                succesful.show();
                TextView currentUser = (TextView) findViewById(R.id.loginInfo);
                currentUser.setText("Welcome,  " + mAuth.getCurrentUser().getDisplayName());
            }
            else{
                Log.d("AUTH","not Authenticated");
            }
        }
    }


    public void step(View view) {
        Intent intent = new Intent(this, StepCountActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void run(View view) {
        Intent intent = new Intent(this, SaveActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void exercise (View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void stat (View view) {
        Intent intent = new Intent(this, StatActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void signOut(View view) {

        final Snackbar snackBar = Snackbar.make(findViewById(R.id.signOut),
                "Are you sure you want to log out?",
                Snackbar.LENGTH_INDEFINITE);

        snackBar.setAction("Yes!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("AUTH", "USER LOGGED OUT");
                                startActivityForResult(AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setTheme(R.style.FirebaseLoginTheme)
                                        .setProviders(
                                                AuthUI.EMAIL_PROVIDER,
                                                AuthUI.GOOGLE_PROVIDER)
                                        .build(), RC_SIGN_IN);
                                //toast
                                Toast succesful = makeText(MainActivity.this, "Logged out" , Toast.LENGTH_SHORT);
                                succesful.show();
                            }
                        });
            }
        })
                .setActionTextColor(Color.WHITE);
        snackBar.show();
    }
}
