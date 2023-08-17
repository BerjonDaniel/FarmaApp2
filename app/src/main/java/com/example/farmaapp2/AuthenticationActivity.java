package com.example.farmaapp2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
class Medicamento {
    private String nombre;
    private String descripcion;
    private String prescripcion;
    private String viaAdmin;
    private String url_prospecto;

    // Constructors, getters, setters

    public Medicamento() {
        this.nombre = "";
        this.descripcion = "";
        this.prescripcion = "";
        this.viaAdmin = "";
        this.url_prospecto = "";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("descripcion", descripcion);
        map.put("prescripcion", prescripcion);
        map.put("viaAdmin", viaAdmin);
        map.put("url_prospecto", url_prospecto);
        return map;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void setPrescripcion(String prescripcion) {
        this.prescripcion = prescripcion;
    }
    public void setViaAdmin(String viaAdmin) {
        this.viaAdmin = viaAdmin;
    }
    public void setUrlProspecto(String url_prospecto) {
        this.url_prospecto = url_prospecto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrescripcion() {
        return prescripcion;
    }
    public String getViaAdmin(){ return viaAdmin;}
    public String getUrlProspecto(){ return url_prospecto;}
}
public class AuthenticationActivity extends AppCompatActivity {

    private EditText email;
    private EditText contraseña;
    private FirebaseAuth mAuth;

    private MedicamentoAdapter dbAdapter;


    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Inicializa FirebaseApp antes de usar Firebase
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        //creamos el adaptador de la BD y la abrimos
        dbAdapter = new MedicamentoAdapter(this);
        dbAdapter.abrir();

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    public void goToRegistrarUsuario(View view){
        setContentView(R.layout.registro_activity);
    }

    public void registrarUsuario(View view){
        EditText emailEditText = findViewById(R.id.newEmailEditText);
        EditText passwordEditText = findViewById(R.id.newPasswordEditText);
        EditText repeatpasswordEditText = findViewById(R.id.repeatPasswordEditText);
        EditText nombreEditText = findViewById(R.id.nombre_usuario);
        EditText apellidoEditText = findViewById(R.id.apellido_usuario);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatpasswordEditText.getText().toString();
        String nombreUsuario = nombreEditText.getText().toString();
        String apellidoUsuario = apellidoEditText.getText().toString();

        if(password.equals(repeatPassword)){
            if( email!= null && password !=null){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(AuthenticationActivity.this, "Signed up successfully",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    addUser(user, nombreUsuario, apellidoUsuario);
                                    accederUsuario(email, password);
                                    volverMain();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(AuthenticationActivity.this, "Sign in failed",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
                //Logeamos al usuario automaticamente

            }
        }else{
            Toast.makeText(AuthenticationActivity.this, "Las contraseñas deben coincidir",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void accederUsuario(View view){
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);


        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if( email!= null && password !=null){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Log in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(AuthenticationActivity.this, "Loged in successfully",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Cursor notesCursor = dbAdapter.obtenerTodosLosMedicamentos();
                                addMedtoUser(user, notesCursor);
                                updateUI(user);
                                volverMain();
                            } else {
                                // If Log in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(AuthenticationActivity.this, "Log in failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    public void accederUsuario(String email, String password){

        if( email!= null && password !=null){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Log in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(AuthenticationActivity.this, "Loged in successfully",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Cursor notesCursor = dbAdapter.obtenerTodosLosMedicamentos();
                                addMedtoUser(user, notesCursor);
                                updateUI(user);
                            } else {
                                // If Log in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(AuthenticationActivity.this, "Log in failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void reload() { }
    private void updateUI(FirebaseUser user) {

    }

    private void volverMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void addUser(FirebaseUser user, String nombre, String apellido){

        // Create a new user with a first and last name
        Map<String, Object> userAux = new HashMap<>();
        //Meter aqui los valores de nombre y apellido!!
        userAux.put("email", user.getEmail());
        userAux.put("nombre",nombre);
        userAux.put("apellido", apellido);

        Cursor notesCursor = dbAdapter.obtenerTodosLosMedicamentos();
        // Create a new user with a first and last name

        // Add a new document with a generated ID
        db.collection("users")
                .add(userAux)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void volverAtras(View view) {
        setContentView(R.layout.login_activity);
    }

    public void addMedtoUser(FirebaseUser user, Cursor medicamentos){
        if(user!=null){
            //Base de datos Cloud Firestore
            // Crea un nuevo mapa con los datos del cursor
            //Map<String, Object> listaMedicamentos = cursorToList(medicamentos);
            Map<String, Object> resultList = new HashMap<>();
            String nombreAux = "";

            if (medicamentos != null && medicamentos.moveToFirst()) {
                int columnCount = medicamentos.getColumnCount();
                do {
                    Map<String, Object> nuevoMedicamento = new HashMap<>();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = medicamentos.getColumnName(i);
                        Object columnValue = getColumnValue(medicamentos, i);
                        nuevoMedicamento.put(columnName, columnValue);
                        if(Objects.equals(columnName, "nombre")){
                            nombreAux = columnValue.toString();
                        }
                    }
                    //Realiza una consulta para buscar medicamentos con el mismo nombre
                    //Si hay algun medicamento con elmismo nombre, no lo añadirá nuevamente
                    db.collection("users").document(user.getEmail())
                            .collection("medicamentos")
                            .whereEqualTo("nombre", nombreAux)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    boolean medicamentoExists = !task.getResult().isEmpty();

                                    if (!medicamentoExists) {
                                        db.collection("users")
                                            .document(Objects.requireNonNull(user.getEmail()))
                                            .collection("medicamentos")
                                            .add(nuevoMedicamento)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });
                                    } else {
                                        // El medicamento ya existe, toma alguna acción
                                    }
                                } else {
                                    Log.w(TAG, "---------------Error al hacer la consulta--------------------------------");
                                }
                            });
                } while (medicamentos.moveToNext());
            }
        }
    }
    private Object getColumnValue(Cursor cursor, int columnIndex) {
        int columnType = cursor.getType(columnIndex);
        switch (columnType) {
            case Cursor.FIELD_TYPE_NULL:
                return null;
            case Cursor.FIELD_TYPE_INTEGER:
                return cursor.getLong(columnIndex);
            case Cursor.FIELD_TYPE_FLOAT:
                return cursor.getDouble(columnIndex);
            case Cursor.FIELD_TYPE_STRING:
                return cursor.getString(columnIndex);
            case Cursor.FIELD_TYPE_BLOB:
                // Handle blob data if needed
                return null;
            default:
                return null;
        }
    }
}
