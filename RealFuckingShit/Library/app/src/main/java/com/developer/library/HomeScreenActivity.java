package com.developer.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeScreenActivity extends AppCompatActivity {

    Button signOut;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onBackPressed() {
        if(preferences.getBoolean(getString(R.string.boolean_name), false)){
            // user is logged in
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        preferences = this.getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        editor = preferences.edit();

        signOut = findViewById(R.id.sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(HomeScreenActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                // startActivity(new Intent(MyActivity.this, SignInActivity.class));
                                editor.putBoolean(getString(R.string.boolean_name), false);
                                editor.commit();
                                Intent toMain = new Intent(HomeScreenActivity.this, MainActivity.class);
                                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(toMain);
                            }
                        });
            }
        });
    }
}
