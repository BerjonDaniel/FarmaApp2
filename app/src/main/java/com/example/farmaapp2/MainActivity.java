package com.example.farmaapp2;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String API_URL  = "https://cima.aemps.es/cima/rest/medicamento";

    private TextView tvBarCode;
    private ArrayList<String> resultado = new ArrayList<String>();
    private EditText codigo_nacional;

    private MedicamentoAdapter dbAdapter;
    private ListView m_listview;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflamos el layout
        setContentView(R.layout.activity_notepad);

        //creamos el adaptador de la BD y la abrimos
        dbAdapter = new MedicamentoAdapter(this);
        dbAdapter.open();

        // Creamos un listview que va a contener el título de todas las notas y
        // en el que cuando pulsemos sobre un título lancemos una actividad de editar
        // la nota con el id correspondiente
        m_listview = (ListView) findViewById(R.id.id_list_view);
        m_listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                        Intent i = new Intent(view.getContext(), GuardarMedicamento.class);
                        i.putExtra(MedicamentoAdapter.KEY_ROWID, id);
                        startActivityForResult(i,1);
                    }
                }
        );

        // rellenamos el listview con los títulos de todas las notas en la BD
        fillData();
    }

    private void fillData() {
        Cursor notesCursor = dbAdapter.fetchAllNotes();

        // Creamos un array con los campos que queremos mostrar en el listview (sólo el título de la nota)
        String[] from = new String[]{MedicamentoAdapter.KEY_TITLE};

        // array con los campos que queremos ligar a los campos del array de la línea anterior (en este caso sólo text1)
        int[] to = new int[]{R.id.text1};

        // Creamos un SimpleCursorAdapter y lo asignamos al listview para mostrarlo
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to, 0);
        m_listview.setAdapter(notes);
    }

    //-------------------Escaneo Cod-Barras y Busqueda en Api------------------------------

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //fillData();

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent );
        if(result != null)
            if (result.getContents() != null){
                String resultindex = result.getContents().substring(6,12);
                tvBarCode = findViewById(R.id.resultado);

                APIFromCIMATask api = new APIFromCIMATask();
                api.cn = resultindex;
                api.execute();

                tvBarCode.setText("El código de barras es:\n" + resultindex);
            }else{
                Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_LONG).show();
            }
    }



    private class APIFromCIMATask extends AsyncTask<String, String, String> {

        String cn;
        String response;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        protected String doInBackground(String... urls) {
            // We make the connection
            try {
                // Creamos la conexión
                URL url = new URL(API_URL + "?cn=" + cn);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//                conn.setDoOutput(true);

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                response = "";

                for (int c; (c = in.read()) >= 0; )
                    response += (char) c;

            } catch (IOException e) {
                e.printStackTrace();
                response = "ERROR: " + e.getLocalizedMessage();
            }

//            SetPrescriptionData(response);
            Log.i("RECEIVED", response);

            return response;
        }

        protected void onPostExecute(String result){
            switchMaintoBarCode(response);
        }

    }
    private void switchMaintoBarCode(String result) {
        // Creamos el Intent que va a lanzar la activity de editar medicamento (ApiCodeBar)
        Intent intent = new Intent(this, GuardarMedicamento.class);
        startActivityForResult(intent, 1);
        // Creamos la informacion a pasar entre actividades
        //Bundle b = new Bundle();
        //b.putString("result", result);

        // Asociamos esta informacion al intent
        intent.putExtra("Result", result);
        // Iniciamos la nueva actividad
        startActivity(intent);
    }



    //---------------------Creamos menu con las tres opciones de añadir------------------

    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popupmenu);
        popup.show();
        LinearLayout dim_layout = (LinearLayout) findViewById(R.id.dim_layout);
        dim_layout.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        /* ESto no funciona del todo porque cuando pulsas fuera no vuelve a iluminarse*/

        LinearLayout dim_layout = (LinearLayout) findViewById(R.id.dim_layout);
        dim_layout.setVisibility(View.INVISIBLE);

        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Codigo de barras", Toast.LENGTH_SHORT);
               escanear();
                return true;

            case R.id.item2:
                Toast.makeText(this, "Codigo nacional", Toast.LENGTH_SHORT);
                createNoteFromCN();
                return true;


            case R.id.action_settings:
                Toast.makeText(this, "setiings", Toast.LENGTH_SHORT);
                switchMaintoSettings();
                return true;
            default:

                return false;
        }
    }
    //menu para setting


    public void  escanear(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }
    private void createNoteFromCN() {
        /*Intent i = new Intent(this, com.example.pharminder_2_0.EditActivity.class);
        startActivityForResult(i, 1);*/
        setContentView(R.layout.busqueda_cn);
    }
    public void createNoteFromCN(View view){
        codigo_nacional = (EditText) findViewById(R.id.title_cn);
        String codigonacional = codigo_nacional.getText().toString();
        APIFromCIMATask api = new APIFromCIMATask();
        api.cn = codigonacional;
        api.execute();
    }


    private void switchMaintoSettings() {

        startActivity(new Intent(MainActivity.this, SettingsActivity.class));

    }
    

    //Creamos menu settings con las tres opciones de añadir

    public void showsettings(View view) {
        PopupMenu popupsettings = new PopupMenu(this, view);
        popupsettings.setOnMenuItemClickListener(this);
        popupsettings.inflate(R.menu.menu_settings);
        popupsettings.show();

    }
}

