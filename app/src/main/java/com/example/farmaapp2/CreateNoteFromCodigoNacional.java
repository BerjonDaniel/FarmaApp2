package com.example.farmaapp2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CreateNoteFromCodigoNacional extends AppCompatActivity{
    private EditText codigo_nacional;
    private int Numero_de_Digitos = 6;
    private static final String API_URL  = "https://cima.aemps.es/cima/rest/medicamento";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda_cn);

    }


    public void createNoteFromCN(View view){
        codigo_nacional = findViewById(R.id.title_cn);
        String codigonacional = codigo_nacional.getText().toString();

        if(codigonacional != null){
            if(codigonacional.length() == Numero_de_Digitos){
                CreateNoteFromCodigoNacional.APIFromCIMATask api = new CreateNoteFromCodigoNacional.APIFromCIMATask();
                api.cn = codigonacional;
                api.execute();
            }else{
                Toast.makeText(this, "Introduzca un código válido", Toast.LENGTH_LONG).show();
                setContentView(R.layout.activity_notepad);
            }
        }else{
            Toast.makeText(this, "Introduzca un código válido", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_notepad);
        }
    }

    private class APIFromCIMATask extends AsyncTask<String, String, String> {

        String cn;
        String response;

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
                //response = "ERROR: " + e.getLocalizedMessage();
                response = "ERROR";
                Log.i("RECEIVED", response);
            }

//            SetPrescriptionData(response);
            Log.i("RECEIVED", response);

            return response;
        }

        protected void onPostExecute(String result){
            switchMaintoBarCode(response, cn);
        }

    }
    private void switchMaintoBarCode(String result, String cn) {

        if(result!="ERROR"){
            // Creamos el Intent que va a lanzar la activity de editar medicamento (ApiCodeBar)
            Intent intent = new Intent(this, GuardarMedicamento.class);
            startActivityForResult(intent, 1);
            // Creamos la informacion a pasar entre actividades
            //Bundle b = new Bundle();
            //b.putString("result", result);

            // Asociamos esta informacion al intent
            intent.putExtra("Result", result);
            intent.putExtra("CodigoNacional", cn);
            // Iniciamos la nueva actividad
            startActivity(intent);
        }else{
            Toast.makeText(this, "El codigo introducido no es válido", Toast.LENGTH_LONG).show();

            /*
            tvBarCode.findViewById(R.id.resultado);
            tvBarCode.setText("El codigo introducido no es válido");
             */

        }

    }
}
