package com.developer.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    List<AuthUI.IdpConfig> providers;
    // Button signOut;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseUser user;
    Intent toHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        toHome = new Intent(this, HomeScreenActivity.class);
        toHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        preferences = this.getSharedPreferences("isLoggedIn", MODE_PRIVATE);
        editor = preferences.edit();
        mAuth = FirebaseAuth.getInstance();
        // signOut = findViewById(R.id.sign_out);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

//        editor.putBoolean("loggedIn", false);
//        editor.commit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("checking login");
        progressDialog.show();

        if(preferences.getBoolean("loggedIn", false)){
            // user already logged in
            // signOut.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            startActivity(toHome);
        } else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                progressDialog.dismiss();
                // Successfully signed in
                // ...
                // signOut.setVisibility(View.VISIBLE);
                user = FirebaseAuth.getInstance().getCurrentUser();
                String[] userName = user.getDisplayName().split(" ");
                StringBuilder name = new StringBuilder();
                for(int i = 0; i < userName.length; i++){
                    name.append(userName[i].toString().toLowerCase());
                    if(i == userName.length - 1){
                        //
                    } else {
                        name.append("_");
                    }
                }
                Toast.makeText(this, "" + name.toString(), Toast.LENGTH_SHORT).show();
                editor.putBoolean("loggedIn", true);
                editor.commit();
                startActivity(toHome);
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }
}
