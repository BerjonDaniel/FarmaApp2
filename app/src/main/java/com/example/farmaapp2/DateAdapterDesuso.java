package com.example.farmaapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DateAdapterDesuso {

    private static final String TABLA_DATES = "dates";
    public static final String KEY_ROWID = "_id";
    public static final String TABLE_NAME = "datePicked";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MEDICATION = "medication";
    public static final String COLUMN_TIME = "time";

    private static final String[] TODOS_CAMPOS = {KEY_ROWID, COLUMN_DATE, COLUMN_MEDICATION, COLUMN_DATE};


    private DateAdapterDesuso.DataBaseHelper dateDbHelper;
    private SQLiteDatabase dateDb;
    private final Context dateContext;

    public DateAdapterDesuso(Context context) {
        this.dateContext = context;
        dateDbHelper = new DateAdapterDesuso.DataBaseHelper(context);
        dateDb = dateDbHelper.getWritableDatabase();
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        private static final String NOMBRE_BASE_DATOS = "datePicked.db";
        private static final int VERSION_BASE_DATOS = 3;

        private static final String CREAR_TABLA_DATES =
                "CREATE TABLE " + TABLA_DATES + " ("
                        + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_DATE + " TEXT NOT NULL, "
                        + COLUMN_MEDICATION+ " TEXT NOT NULL, "
                        + COLUMN_TIME + " TEXT NOT NULL);";

        public DataBaseHelper(Context context) {
            super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREAR_TABLA_DATES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_DATES);
            onCreate(db);
        }
    }

    public DateAdapterDesuso abrir() throws SQLException {
        dateDbHelper = new DateAdapterDesuso.DataBaseHelper(dateContext);
        dateDb = dateDbHelper.getWritableDatabase();
        return this;
    }

    public Long insertMedication(String date, String medication, String time) {
        ContentValues valoresIniciales = new ContentValues();
        valoresIniciales.put(COLUMN_DATE, date);
        valoresIniciales.put(COLUMN_MEDICATION, medication);
        valoresIniciales.put(COLUMN_TIME, time);
        return dateDb.insert(TABLA_DATES, null, valoresIniciales);
    }

    //El mismo método que el anterior pero en vez de una sola fecha y hora
    //  le estamos pasando todas las fechas y horas de un solo medicamento
    public Long insertListMedication(List<String> dates, String medication, List<String> times) {
        long insertedRows = 0;
        // Iterar sobre los elementos de las listas
        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            String time = times.get(i);

            // Insertar el medicamento en la base de datos
            ContentValues valoresIniciales = new ContentValues();
            valoresIniciales.put(COLUMN_DATE, date);
            valoresIniciales.put(COLUMN_MEDICATION, medication);
            valoresIniciales.put(COLUMN_TIME, time);
            long result = dateDb.insert(TABLA_DATES, null, valoresIniciales);

            if (result != -1) {
                insertedRows++;
            }
        }

        return insertedRows;
    }


    public Cursor getMedicationsByDate(String date) {
        SQLiteDatabase db = dateDbHelper.getReadableDatabase();
        String[] projection = {COLUMN_MEDICATION, COLUMN_TIME};
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};
        return db.query(TABLA_DATES, projection, selection, selectionArgs, null, null, null);
    }
    /*
    public List<Map<String, String>> getMedicationsByDate(String date) {
        List<Map<String, String>> medicationsList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_MEDICATION, COLUMN_TIME};
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> medicationMap = new HashMap<>();
                String medication = cursor.getString(cursor.getColumnIndex(COLUMN_MEDICATION));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                medicationMap.put(COLUMN_MEDICATION, medication != null ? medication : "");
                medicationMap.put(COLUMN_TIME, time != null ? time : "");
                medicationsList.add(medicationMap);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return medicationsList;
    }

     */

}

