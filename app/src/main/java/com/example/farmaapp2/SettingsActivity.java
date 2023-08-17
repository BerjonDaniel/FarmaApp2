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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class SettingsActivity extends AppCompatActivity {

    private TextView email_Usuario;
    private TextView nombre_Usuario;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a new user with a first and last name
    //Map<String, Object> userAux = new HashMap<>();

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
            setUserData(user);
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

    private void setUserData(FirebaseUser user) {
        email_Usuario = findViewById(R.id.emailLoged);
        nombre_Usuario = findViewById(R.id.saludoUsuario);

        String email = user.getEmail();

        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "setUserdataWithEmail:success");
                            for(QueryDocumentSnapshot document : task.getResult()){

                                Log.d(TAG, document.getId() + "-->" + document.getData());

                                String email2 = document.get("email").toString();
                                String saludo = "¡Hola, " + document.get("nombre").toString() + " " + document.get("apellido").toString() + "!";
                                //Cambiar esto por el nombre!!!!!!!!!!!!!!!!!!!!!!!!!!
                                email_Usuario.setText(email2);
                                nombre_Usuario.setText(saludo);

                            }

                        }else{
                            Log.w(TAG, "setUserDataWithEmail:failure", task.getException());

                        }
                    }
                });


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