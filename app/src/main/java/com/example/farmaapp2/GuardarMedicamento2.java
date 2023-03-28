package com.example.farmaapp2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class GuardarMedicamento2 extends AppCompatActivity {

    private TextView nombre_med;
    private TextView p_activo;
    private TextView c_presc;
    private TextView via_admin;
    private Button btnGuardar;
    String error;

    private MedicamentoAdapterChat medicamentoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resumen_medicamento);

        //Esto es lo que falla! preguntale a chatgpt que te lo haga
        try {
            Bundle bundle = this.getIntent().getExtras();
            if(bundle.getString("Result")!=null){
                String respuesta = bundle.getString("Result");
                SetPrescriptionData(respuesta);
            }else if(bundle.getString("RowId")!=null){
                String respuesta = bundle.getString("RowId");
                SetPrescriptionData(respuesta);
            }
        }catch (Exception e) {
            e.printStackTrace();
            error    = "ERROR: " + e.getLocalizedMessage();
        }


        nombre_med = (TextView) findViewById(R.id.nombre_medicamento);
        p_activo = (TextView) findViewById(R.id.princ_Activo);
        c_presc = (TextView) findViewById(R.id.presc_med);
        //ImageView imagen = (ImageView) findViewById(R.id.imagen_medicamento) ;
        //url_prospecto = (TextView) findViewById(R.id.button2);
        via_admin = (TextView) findViewById(R.id.vias_administracion);
        btnGuardar = findViewById(R.id.guardar_medicamento);

        // Asignamos el OnClickListener al botón
        btnGuardar.setOnClickListener(guardarClickListener);

        // Creamos una instancia del MedicamentoAdapter
        medicamentoAdapter = new MedicamentoAdapterChat(this);
    }

    public void SetPrescriptionData(String data) {

        nombre_med = (TextView) findViewById(R.id.nombre_medicamento);
        p_activo = (TextView) findViewById(R.id.princ_Activo);
        c_presc = (TextView) findViewById(R.id.presc_med);
        //ImageView imagen = (ImageView) findViewById(R.id.imagen_medicamento) ;
        //url_prospecto = (TextView) findViewById(R.id.button2);
        via_admin = (TextView) findViewById(R.id.vias_administracion);


        JSONObject obj = null;
        String jsonString = data;
        String nombreMedicamento = null;
        String pActivo = null;
        String cPresc = null;
        String urlProspecto = null;
        String viasAdministracion = null;
        //ArrayList<String> viasAdministracion = new ArrayList<String>();
        //String urlImagenMedicamento = null;
        if (data == null){

        }


        try {
            obj = new JSONObject(jsonString);
            nombreMedicamento = obj.getString("nombre");
            pActivo = obj.getString("pactivos");
            cPresc = obj.getString("cpresc");
            JSONArray documentosArray = obj.getJSONArray("docs");
            JSONArray viasAdminArray = obj.getJSONArray("viasAdministracion");

            //Loop para buscar PROSPECTO
            for (int i = 0; i < documentosArray.length(); i++)
            {
                if(documentosArray.getJSONObject(i).getInt("tipo")==2){
                    urlProspecto = documentosArray.getJSONObject(i).getString("url");
                }
            }
            //Loop para buscar VIAS DE ADMINISTRACION
            for (int i = 0; i < viasAdminArray.length(); i++)
            {
                //viasAdministracion.add(viasAdminArray.getJSONObject(i).getString("nombre"));
                viasAdministracion = viasAdminArray.getJSONObject(i).getString("nombre");
            }


        } catch (JSONException e) {
            e.printStackTrace();
            nombreMedicamento    = "ERROR: " + e.getLocalizedMessage();
        }

        nombre_med.setText(nombreMedicamento);
        p_activo.setText(pActivo);
        c_presc.setText(cPresc);
        via_admin.setText(viasAdministracion);
        //url_prospecto = urlProspecto;
        //Loop para escribir lista de VIAS DE ADMINISTRACION
        /*for(int i = 0;i < viasAdministracion.size(); i++){
            vias_administracion = viasAdministracion.get(i);
        }*/

        //imagen.setImage();

    }

    private View.OnClickListener guardarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Obtenemos los datos del medicamento
            String nombre = nombre_med.getText().toString();
            String descripcion = p_activo.getText().toString();
            String cPresc = c_presc.getText().toString();
            String viaAdmin = via_admin.getText().toString();

            // Guardamos el medicamento en la base de datos
            guardarMedicamento(nombre, descripcion, cPresc, viaAdmin);

            // Mostramos un mensaje al usuario
            Toast.makeText(GuardarMedicamento2.this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();

            // Volvemos a la pantalla principal
            finish();

        }
    };
    /*
    private void guardarMedicamento(String nombre, String descripcion, String cPresc, String viaAdmin) {
        // Creamos un objeto Medicamento con los datos del medicamento
        MedicamentoAdapterChat medicamento = new MedicamentoAdapterChat(nombre, descripcion, cPresc, viaAdmin);

        // Insertamos el medicamento en la base de datos
        medicamentoAdapter.insertarMedicamento(medicamento);
    }

     */

    private void guardarMedicamento(String nombre, String descripcion, String cPresc, String viaAdmin) {
        // Creamos un objeto Medicamento con los datos del medicamento

        // Insertamos el medicamento en la base de datos
        long id = medicamentoAdapter.insertarMedicamento(nombre, descripcion, cPresc, viaAdmin);
        if (id != -1) {
            Toast.makeText(this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar el medicamento", Toast.LENGTH_SHORT).show();
        }
    }

}

