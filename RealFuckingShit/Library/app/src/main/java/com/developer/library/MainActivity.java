package com.developer.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    Button signIn;
    ProgressDialog progressDialog;
    SharedPreferences preferences_logged_in, preferences_username;
    SharedPreferences.Editor editor, editor_username;
    FirebaseUser user;
    Intent toHome;

    final int RC_SIGN_IN = 1;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        toHome = new Intent(this, HomeScreenActivity.class);
        toHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        preferences_logged_in = this.getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        editor = preferences_logged_in.edit();

        preferences_username = this.getSharedPreferences(getString(R.string.preference_name_username), MODE_PRIVATE);
        editor_username = preferences_username.edit();

        mAuth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.sign_in);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        if(preferences_logged_in.getBoolean(getString(R.string.boolean_name), false)){

            // user already logged in
            startActivity(toHome);

        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                // Successfully signed in
                // ...
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

                editor_username.putString("current_user", name.toString());
                editor_username.commit();

                editor.putBoolean(getString(R.string.boolean_name), true);
                editor.commit();
                startActivity(toHome);

            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }
}
