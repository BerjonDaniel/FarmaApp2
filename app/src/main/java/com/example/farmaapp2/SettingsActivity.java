package com.example.farmaapp2;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class SettingsActivity extends AppCompatActivity {

    private TextView email_Usuario;
    private TextView nombre_Usuario;
    private EditText contraseña;

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preference2, rootKey);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            setContentView(R.layout.settings_with_user_loged);
            setUserData();
        } else {
            // No user is signed in
            setContentView(R.layout.settings_activity);
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    private void setUserData() {
        email_Usuario = findViewById(R.id.emailLoged);
        nombre_Usuario = findViewById(R.id.saludoUsuario);

    }

    public void openAuthentication(View view) {
        // Do something in response to button
        startActivity(new Intent(SettingsActivity.this, AuthenticationActivity.class));

    }
    public void cerrarSesion(View view){
        setContentView(R.layout.cerrar_sesion);
    }
    public void confirmCerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelar(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }



}