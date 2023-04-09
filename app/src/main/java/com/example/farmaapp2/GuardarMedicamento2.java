package com.example.farmaapp2;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

    private MedicamentoAdapterChat medicamentoAdapterChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resumen_medicamento);

        medicamentoAdapterChat = new MedicamentoAdapterChat(this);

        try { //Accederemos aquí de dos formas: 1. Al escanear un medicamento
              //                                2. Al pulsar un medicamento en la lista
            Bundle bundle = this.getIntent().getExtras();
            if(bundle.getString("Result")!=null){ // ----- Caso 1
                String respuesta = bundle.getString("Result");
                SetPrescriptionDataJSON(respuesta);
            }else if(bundle.getLong("RowId") != 0){// ---- Caso 2
                Long respuesta = bundle.getLong("RowId");
                Log.d(TAG, "La respuesta obtenida es: " + respuesta); // Imprimir la cadena en el registro
                Cursor cursor = medicamentoAdapterChat.obtenerMedicamento(respuesta);
                StringBuilder cursorAsString = new StringBuilder();
                cursorAsString.append("{");
                if (cursor.moveToFirst()) { //Convertimos de esta forma el cursor a String
                    do {
                        int columnsCount = cursor.getColumnCount();
                        //  Queremos este tipo de cadena: {"nregistro":"37453","nombre":"ANGINOVAG SOLUCION PARA PULVERIZACION BUCAL","pactivos":"DECUALINIO CLORURO, ENOXOLONA, HIDROCORTISONA ACETATO, TIROTRICINA, LIDOCAINA HIDROCLORURO","labtitular":"Ferrer Internacional, S.A.","cpresc":"Medicamento Sujeto A Prescripci\u00F3n M\u00E9dica","estado":{"aut":-249786000000},"comerc":true,"receta":true,"generico":false,"conduc":false,"triangulo":false,"huerfano":false,"biosimilar":false,"nosustituible":{"id":0,"nombre":"N/A"},"psum":false,"notas":false,"materialesInf":false,"ema":false,"docs":[{"tipo":1,"url":"https://cima.aemps.es/cima/pdfs/ft/37453/FT_37453.pdf","urlHtml":"https://cima.aemps.es/cima/dochtml/ft/37453/FT_37453.html","secc":true,"fecha":1617022513000},{"tipo":2,"url":"https://cima.aemps.es/cima/pdfs/p/37453/P_37453.pdf","urlHtml":"https://cima.aemps.es/cima/dochtml/p/37453/P_37453.html","secc":true,"fecha":1617022961000}],"atcs":[{"codigo":"R02A","nombre":"PREPARADOS PARA LA GARGANTA","nivel":3},{"codigo":"R02AA","nombre":"Antis\u00E9pticos","nivel":4},{"codigo":"R02AA52","nombre":"Decualinio, combinaciones con","nivel":5}],"principiosActivos":[{"id":709,"codigo":"709CL","nombre":"DECUALINIO CLORURO","cantidad":"1.0","unidad":"mg","orden":1},{"id":1410,"codigo":"1410A","nombre":"ENOXOLONA","cantidad":"0.6","unidad":"mg","orden":2},{"id":54,"codigo":"54AC","nombre":"HIDROCORTISONA ACETATO","cantidad":"0.6","unidad":"mg","orden":3},{"id":3287,"codigo":"3287A","nombre":"TIROTRICINA","cantidad":"4","unidad":"mg","orden":4},{"id":48,"codigo":"48CH","nombre":"LIDOCAINA HIDROCLORURO","cantidad":"1","unidad":"mg","orden":5}],"excipientes":[{"id":7316,"nombre":"SACARINA SODICA","cantidad":"3.2","unidad":"mg","orden":1},{"id":6996,"nombre":"PROPILENGLICOL","cantidad":"93.33","unidad":"mg","orden":2},{"id":873,"nombre":"ALCOHOL ETILICO (ETANOL)","cantidad":"1 ml","unidad":"Cantidad suficiente","orden":4}],"viasAdministracion":[{"id":49,"nombre":"USO BUCOFAR\u00CDNGEO"}],"presentaciones":[{"cn":"917914","nombre":"ANGINOVAG SOLUCION PARA PULVERIZACION BUCAL , 1 frasco de 20 ml","estado":{"aut":-249786000000},"comerc":true,"psum":false}],"formaFarmaceutica":{"id":50,"nombre":"SOLUCIÓN PARA PULVERIZACIÓN BUCAL"},"formaFarmaceuticaSimplificada":{"id":54,"nombre":"PULVERIZACION BUCAL"},"vtm":{"id":145371000140105,"nombre":"multicomponente"},"dosis":"1.0 mg / 0.6 mg / 0.6 mg / 4 mg / 1 mg"}
                        for (int i = 0; i < columnsCount; i++) {
                            String columnName = cursor.getColumnName(i);
                            String columnValue = cursor.getString(i);
                            cursorAsString.append('"' + columnName + '"' + ":" + '"' + columnValue + '"');
                            if(i+1<columnsCount){
                                cursorAsString.append(",");
                            }
                        }
                        cursorAsString.append("}");
                    } while (cursor.moveToNext());
                }

                Log.d(TAG, cursorAsString.toString()); // Imprimir la cadena en el registro

                SetPrescriptionData(cursorAsString.toString());
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
        medicamentoAdapterChat = new MedicamentoAdapterChat(this);
    }

    public void SetPrescriptionDataJSON(String data) {

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

    public void SetPrescriptionData(String data) {
        //  Queremos este tipo de cadena: {"nregistro":"37453","nombre":"ANGINOVAG SOLUCION PARA PULVERIZACION BUCAL","pactivos":"DECUALINIO CLORURO, ENOXOLONA, HIDROCORTISONA ACETATO, TIROTRICINA, LIDOCAINA HIDROCLORURO","labtitular":"Ferrer Internacional, S.A.","cpresc":"Medicamento Sujeto A Prescripci\u00F3n M\u00E9dica","estado":{"aut":-249786000000},"comerc":true,"receta":true,"generico":false,"conduc":false,"triangulo":false,"huerfano":false,"biosimilar":false,"nosustituible":{"id":0,"nombre":"N/A"},"psum":false,"notas":false,"materialesInf":false,"ema":false,"docs":[{"tipo":1,"url":"https://cima.aemps.es/cima/pdfs/ft/37453/FT_37453.pdf","urlHtml":"https://cima.aemps.es/cima/dochtml/ft/37453/FT_37453.html","secc":true,"fecha":1617022513000},{"tipo":2,"url":"https://cima.aemps.es/cima/pdfs/p/37453/P_37453.pdf","urlHtml":"https://cima.aemps.es/cima/dochtml/p/37453/P_37453.html","secc":true,"fecha":1617022961000}],"atcs":[{"codigo":"R02A","nombre":"PREPARADOS PARA LA GARGANTA","nivel":3},{"codigo":"R02AA","nombre":"Antis\u00E9pticos","nivel":4},{"codigo":"R02AA52","nombre":"Decualinio, combinaciones con","nivel":5}],"principiosActivos":[{"id":709,"codigo":"709CL","nombre":"DECUALINIO CLORURO","cantidad":"1.0","unidad":"mg","orden":1},{"id":1410,"codigo":"1410A","nombre":"ENOXOLONA","cantidad":"0.6","unidad":"mg","orden":2},{"id":54,"codigo":"54AC","nombre":"HIDROCORTISONA ACETATO","cantidad":"0.6","unidad":"mg","orden":3},{"id":3287,"codigo":"3287A","nombre":"TIROTRICINA","cantidad":"4","unidad":"mg","orden":4},{"id":48,"codigo":"48CH","nombre":"LIDOCAINA HIDROCLORURO","cantidad":"1","unidad":"mg","orden":5}],"excipientes":[{"id":7316,"nombre":"SACARINA SODICA","cantidad":"3.2","unidad":"mg","orden":1},{"id":6996,"nombre":"PROPILENGLICOL","cantidad":"93.33","unidad":"mg","orden":2},{"id":873,"nombre":"ALCOHOL ETILICO (ETANOL)","cantidad":"1 ml","unidad":"Cantidad suficiente","orden":4}],"viasAdministracion":[{"id":49,"nombre":"USO BUCOFAR\u00CDNGEO"}],"presentaciones":[{"cn":"917914","nombre":"ANGINOVAG SOLUCION PARA PULVERIZACION BUCAL , 1 frasco de 20 ml","estado":{"aut":-249786000000},"comerc":true,"psum":false}],"formaFarmaceutica":{"id":50,"nombre":"SOLUCIÓN PARA PULVERIZACIÓN BUCAL"},"formaFarmaceuticaSimplificada":{"id":54,"nombre":"PULVERIZACION BUCAL"},"vtm":{"id":145371000140105,"nombre":"multicomponente"},"dosis":"1.0 mg / 0.6 mg / 0.6 mg / 4 mg / 1 mg"}
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
            //Codigo para cuando los datos que reciba estén vacios
        }

        try {
            obj = new JSONObject(jsonString);
            nombreMedicamento = obj.getString("nombre");
            pActivo = obj.getString("descripcion");
            cPresc = obj.getString("prescripcion");
            //JSONArray documentosArray = obj.getJSONArray("docs");
            viasAdministracion = obj.getString("via_administracion");
            /*
            //Loop para buscar PROSPECTO
            for (int i = 0; i < documentosArray.length(); i++)
            {
                if(documentosArray.getJSONObject(i).getInt("tipo")==2){
                    urlProspecto = documentosArray.getJSONObject(i).getString("url");
                }
            }

             */

            /*
            //Loop para buscar VIAS DE ADMINISTRACION --------------- Cuando venimos desde el menu principal, no lo necesitamos
            for (int i = 0; i < viasAdminArray.length(); i++)       //(solo con escaneo será necesario)
            {
                //viasAdministracion.add(viasAdminArray.getJSONObject(i).getString("nombre"));
                viasAdministracion = viasAdminArray.getJSONObject(i).getString("nombre");
            }

             */


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
        long id = medicamentoAdapterChat.insertarMedicamento(nombre, descripcion, cPresc, viaAdmin);
        if (id != -1) {
            Toast.makeText(this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar el medicamento", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}

