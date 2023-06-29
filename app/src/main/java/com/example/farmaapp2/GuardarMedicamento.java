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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class GuardarMedicamento extends AppCompatActivity {

    private Long mRowId;
    private TextView nombre_med;
    private TextView p_activo;
    private TextView c_presc;
    private TextView via_admin;
    private String url_prospecto;
    private TextView codigonacional;
    private Button btnGuardar;
    String error;

    private MedicamentoAdapter medicamentoAdapter;
    //FirebaseFirestore db = FirebaseFirestore.getInstance();

    //variables para las alarmas
    Calendar c = Calendar.getInstance();
    int cyear = c.get(Calendar.YEAR);
    int cmonth = c.get(Calendar.MONTH);
    int cday = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    int year, month, day, year1, month1, day1;
    Button timeButton, dateButton, dateButton1;
    private NumberPicker picker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resumen_medicamento);

        medicamentoAdapter = new MedicamentoAdapter(this);

        try { //Accederemos aquí de dos formas: 1. Al escanear un medicamento
              //                                2. Al pulsar un medicamento en la lista
            Bundle bundle = this.getIntent().getExtras();
            if(bundle.getString("Result")!=null){ // ----- Caso 1
                String respuesta = bundle.getString("Result");
                String cn = bundle.getString("CodigoNacional");
                SetPrescriptionDataJSON(respuesta, cn);

            }else if(bundle.getLong("RowId") != 0){// ---- Caso 2
                Long respuesta = bundle.getLong("RowId");
                Log.d(TAG, "La respuesta obtenida es: " + respuesta); // Imprimir la cadena en el registro
                Cursor cursor = medicamentoAdapter.obtenerMedicamento(respuesta);
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

        // Asignamos el OnClickListener al botón
        /*
        btnGuardar = findViewById(R.id.guardar_medicamento);
        btnGuardar.setOnClickListener(guardarClickListener);

         */

        //Para las alarmas
        timeButton = findViewById(R.id.timeButton);
        dateButton = findViewById(R.id.dateButton);
        dateButton1 = findViewById(R.id.dateButton1);
        picker1 = findViewById(R.id.np);
        picker1.setMaxValue(24);
        picker1.setMinValue(1);
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = picker1.getValue();
                Log.d("picker value", String.valueOf(valuePicker1));
            }
        });
    }

    // ------ Para cuando llegamos a GuardarMedicamento a traves del escaneo ------
    public void SetPrescriptionDataJSON(String data, String Cn) {

        nombre_med = (TextView) findViewById(R.id.nombre_medicamento);
        p_activo = (TextView) findViewById(R.id.princ_Activo);
        c_presc = (TextView) findViewById(R.id.presc_med);
        //ImageView imagen = (ImageView) findViewById(R.id.imagen_medicamento) ;
        //url_prospecto = (TextView) findViewById(R.id.button2);
        via_admin = (TextView) findViewById(R.id.vias_administracion);
        codigonacional = (TextView) findViewById(R.id.resultado222);


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
        url_prospecto = urlProspecto;
        codigonacional.setText(Cn);
        //Loop para escribir lista de VIAS DE ADMINISTRACION
        /*for(int i = 0;i < viasAdministracion.size(); i++){
            vias_administracion = viasAdministracion.get(i);
        }*/

        //imagen.setImage();

    }

    // ------ Para caundo llegamos a GuardarMedicamento a traves de la lista del menú principal ------
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
        String idMed = null;
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
            idMed = obj.getString("_id");
            nombreMedicamento = obj.getString("nombre");
            pActivo = obj.getString("descripcion");
            cPresc = obj.getString("prescripcion");
            //JSONArray documentosArray = obj.getJSONArray("docs");
            viasAdministracion = obj.getString("via_administracion");
            urlProspecto = obj.getString("url_prospecto");

/*
            //Loop para buscar PROSPECTO
            for (int i = 0; i < documentosArray.length(); i++)
            {
                if(documentosArray.getJSONObject(i).getInt("tipo")==2){
                    urlProspecto = documentosArray.getJSONObject(i).getString("url");
                }
            }
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
        url_prospecto = urlProspecto;
        mRowId = Long.parseLong(idMed);
        //Loop para escribir lista de VIAS DE ADMINISTRACION
        /*for(int i = 0;i < viasAdministracion.size(); i++){
            vias_administracion = viasAdministracion.get(i);
        }*/

        //imagen.setImage();

    }
    public void guardar(View view){
        // Obtenemos los datos del medicamento
        String nombre = nombre_med.getText().toString();
        Log.i("----------------------RECEIVED", nombre);
        String descripcion = p_activo.getText().toString();
        Log.i("----------------------RECEIVED", descripcion);
        String cPresc = c_presc.getText().toString();
        Log.i("----------------------RECEIVED", cPresc);
        String viaAdmin = via_admin.getText().toString();
        Log.i("----------------------RECEIVED", viaAdmin);
        String urlProspecto = url_prospecto;
        Log.i("----------------------RECEIVED", urlProspecto);

        // Establecemos las alarmas
        // ------ Aqui empezará el codigo de las alarmas ------
        double diferenciadias1 = (year - cyear) * 365 + (month - cmonth) * 12 + (day - cday);
        double diferenciadias2 = (year1 - year) * 365 + (month1 - month) * 12 + (day1 - day);
        double diferenciadias3 = (year1 - cyear) * 365 + (month1 - cmonth) * 12 + (day1 - cday);

        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        Context context = getApplicationContext();
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        AlarmManager alarms =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //hago un loop para cada alarma
        long frecuencia = picker1.getValue() * 3600000;
        long timeOrLengthofWait = (long) ((((hour - mHour) * 60 + minute - mMinute) * 60000)+ diferenciadias1*86400000);
        if(timeOrLengthofWait<0){
            timeOrLengthofWait+=24*3600000;
        }

        //Creamos notificación
        NotificationManager notificationManager;

        // crea canal de notificaciones
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "com.uc3m.it.helloallarmappmov.notify_001");

        //pendingIntent para abrir la actividad cuando se pulse la notificación
        //pendingIntent para abrir la actividad cuando se pulse la notificación
        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, PendingIntent.FLAG_IMMUTABLE);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.pharminder_logo_circular);
        mBuilder.setContentTitle("Alarma activada");
        if (timeOrLengthofWait <= 0) {
        } else if (timeOrLengthofWait <= 60000) {
            mBuilder.setContentText("Alarma activada para dentro de " + timeOrLengthofWait / 1000 + " segundos");
        } else if (timeOrLengthofWait <= 3600000) {
            mBuilder.setContentText("Alarma activada para dentro de " + timeOrLengthofWait / 60000 + " minutos");
        } else if  (timeOrLengthofWait <= 86400000){
            mBuilder.setContentText("Alarma activada para dentro de " + timeOrLengthofWait / 3600000 + " horas");
        }else{
            mBuilder.setContentText("Alarma activada para dentro de " + timeOrLengthofWait / (3600000*24) + " dias");
        }
        notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal de HelloAlarmAppMov",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        notificationManager.notify(1, mBuilder.build());

        // Programamos la alarma
        Random random = new Random();

        int m = random.nextInt(9999 - 1000) + 1000;

        int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;


        Intent intentToFire = new Intent(this, MyBroadcastReceiver.class);
        intentToFire.putExtra("NAME", nombre_med.getText().toString());
        PendingIntent AlarmPendingIntent = PendingIntent.getBroadcast(this, m, intentToFire, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        intentArray.add(AlarmPendingIntent);
        alarms.setRepeating(alarmType, timeOrLengthofWait, frecuencia, AlarmPendingIntent);
        if (diferenciadias1 < 0) {
            alarms.cancel(AlarmPendingIntent);
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snack), "Establezca una fecha de inicio posterior a la actual", 5000);
            mySnackbar.show();
        }
        if (diferenciadias2 < 0) {
            alarms.cancel(AlarmPendingIntent);
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snack), "Establezca una fecha de fin posterior a la de inicio", 5000);
            mySnackbar.show();
        }
        if (diferenciadias3 < 0) {
            alarms.cancel(AlarmPendingIntent);
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snack), "Establezca una fecha de fin posterior a la actual", 5000);
            mySnackbar.show();
        }
        // Creamos un objeto Medicamento con los datos del medicamento
        // Insertamos el medicamento en la base de datos
        if (mRowId == null) {
            long id = medicamentoAdapter.insertarMedicamento(nombre, descripcion, cPresc, viaAdmin, urlProspecto);
            if (id!= -1) {
                Toast.makeText(this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar el medicamento", Toast.LENGTH_SHORT).show();
            }
        }else{
            boolean id = medicamentoAdapter.actualizarMedicamento(mRowId, nombre, descripcion, cPresc, viaAdmin, urlProspecto);
            if (id) {
                Toast.makeText(this, "Medicamento actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar el medicamento", Toast.LENGTH_SHORT).show();
            }
        }

        // ------ Hasta aquí el código de las alarmas ------

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //---------- Para borrar el medicamento de la base de datos ----------
    public void borrarMedicamento(MenuItem item){

        boolean eliminado = medicamentoAdapter.eliminarMedicamento(mRowId);

        // Verificar si el medicamento fue eliminado correctamente
        if (eliminado) {
            // El medicamento se eliminó exitosamente
            // Mostramos un mensaje al usuario
            Toast.makeText(GuardarMedicamento.this, "Medicamento eliminado correctamente", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // No se pudo eliminar el medicamento
            Toast.makeText(GuardarMedicamento.this, "Error al eliminar el medicamento", Toast.LENGTH_SHORT).show();
        }

    }

    //---------- Para desactivar las alarmas de este medicamento, pero sin borrarlo ----------
    public void desactivarAlarmas(MenuItem item){

        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        int cancelado = receiver.cancelNotifications(getApplicationContext());

        // Verificar si el medicamento fue eliminado correctamente
        if (cancelado == -1) {
            // Las Alarmas se desactivaron exitosamente
            // Mostramos un mensaje al usuario
            Toast.makeText(GuardarMedicamento.this, "Las Alarmas del Medicamento se han desactivado", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // No se pudo eliminar el medicamento
            Toast.makeText(GuardarMedicamento.this, "Error al desactivar las alarmas", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    public void PopTimeTicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override

            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, mHour, mMinute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void DateTicker(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int selecteyear, int selectemonth, int selecteday) {
                day = selecteday;
                month = selectemonth;
                year = selecteyear;
                dateButton.setText(String.format(Locale.getDefault(), "%02d/%02d/%02d", day, month, year));
            }
        };
        DatePickerDialog DatePickerDialog = new DatePickerDialog(this, /*style,*/ onDateSetListener, cyear, cmonth, cday);

        DatePickerDialog.setTitle("Select Date");
        DatePickerDialog.show();
    }

    public void DateTicker2(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int selecteyear1, int selectemonth1, int selecteday1) {
                day1 = selecteday1;
                month1 = selectemonth1;
                year1 = selecteyear1;
                dateButton1.setText(String.format(Locale.getDefault(), "%02d/%02d/%02d", day1, month1, year1));
            }
        };
        DatePickerDialog DatePickerDialog = new DatePickerDialog(this, /*style,*/ onDateSetListener, cyear, cmonth, cday);

        DatePickerDialog.setTitle("Select Date");
        DatePickerDialog.show();
    }
    /*
    private View.OnClickListener guardarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Obtenemos los datos del medicamento
            String nombre = nombre_med.getText().toString();
            Log.i("----------------------RECEIVED", nombre);
            String descripcion = p_activo.getText().toString();
            Log.i("----------------------RECEIVED", descripcion);
            String cPresc = c_presc.getText().toString();
            Log.i("----------------------RECEIVED", cPresc);
            String viaAdmin = via_admin.getText().toString();
            Log.i("----------------------RECEIVED", viaAdmin);
            String urlProspecto = url_prospecto;
            Log.i("----------------------RECEIVED", urlProspecto);

            // Establecemos las alarmas
            setAlarm(v);
            // Guardamos el medicamento en la base de datos
            guardarMedicamento(nombre, descripcion, cPresc, viaAdmin, urlProspecto);
            // Mostramos un mensaje al usuario
            Toast.makeText(GuardarMedicamento2.this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();
        }

    };

     */
    /*
    private void guardarMedicamento(String nombre, String descripcion, String cPresc, String viaAdmin) {
        // Creamos un objeto Medicamento con los datos del medicamento
        MedicamentoAdapterChat medicamento = new MedicamentoAdapterChat(nombre, descripcion, cPresc, viaAdmin);

        // Insertamos el medicamento en la base de datos
        medicamentoAdapter.insertarMedicamento(medicamento);
    }

     */
    /*
    private void guardarMedicamento(String nombre, String descripcion, String cPresc, String viaAdmin, String urlProspecto) {
        // Creamos un objeto Medicamento con los datos del medicamento

        // Insertamos el medicamento en la base de datos
        long id = medicamentoAdapterChat.insertarMedicamento(nombre, descripcion, cPresc, viaAdmin, urlProspecto);
        if (id != -1) {
            Toast.makeText(this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar el medicamento", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

     */

    //Para poder abrir el prospecto
    public void  abrirurl(View view){

        Uri uri = Uri.parse(url_prospecto); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.popup_borrar_medicamento, menu);
        return true;
    }

    //Para poder poner Alarmas


}

