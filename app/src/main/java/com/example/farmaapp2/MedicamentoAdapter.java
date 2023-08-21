package com.example.farmaapp2;

import java.sql.*;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicamentoAdapter {
    private Connection conexion = null;
    private static final String TABLA_MEDICAMENTOS = "medicamentos";
    private static final String TABLA_DATES = "dates";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_BODY = "descripcion";
    public static final String KEY_PRESCRIPCION = "prescripcion";
    public static final String KEY_VIADMIN = "via_administracion";
    public static final String KEY_URL_PROSPECTO = "url_prospecto";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";

    private DataBaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mContext;

    private static final String[] TODOS_CAMPOS = {KEY_ROWID, KEY_NOMBRE, KEY_BODY, KEY_PRESCRIPCION, KEY_VIADMIN, KEY_URL_PROSPECTO};

    public MedicamentoAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        private static final String NOMBRE_BASE_DATOS = "medicamentos.db";
        private static final int VERSION_BASE_DATOS = 4;

        private static final String CREAR_TABLA_MEDICAMENTOS =
                "CREATE TABLE " + TABLA_MEDICAMENTOS + " ("
                        + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_NOMBRE + " TEXT NOT NULL, "
                        + KEY_BODY+ " TEXT NOT NULL, "
                        + KEY_PRESCRIPCION + " TEXT NOT NULL, "
                        + KEY_VIADMIN + " TEXT NOT NULL, "
                        + KEY_URL_PROSPECTO + " TEXT NOT NULL);";

        private static final String CREAR_TABLA_DATES =
                "CREATE TABLE " + TABLA_DATES + " ("
                        + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_DATE + " TEXT NOT NULL, "
                        + KEY_NOMBRE+ " TEXT NOT NULL, "
                        + COLUMN_TIME + " TEXT NOT NULL);";

        public DataBaseHelper(Context context) {
            super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREAR_TABLA_MEDICAMENTOS);
            db.execSQL(CREAR_TABLA_DATES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_MEDICAMENTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_DATES);
            onCreate(db);
        }
    }

    public MedicamentoAdapter abrir() throws SQLException {
        mDbHelper = new DataBaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        mDbHelper.close();
    }

    //---------- Metodos para la tabla de Medicamentos ----------

    public long insertarMedicamento(String nombre, String descripcion, String prescripcion, String viaAdmin, String url_prospecto) {
        ContentValues valoresIniciales = new ContentValues();
        valoresIniciales.put(KEY_NOMBRE, nombre);
        valoresIniciales.put(KEY_BODY, descripcion);
        valoresIniciales.put(KEY_PRESCRIPCION, prescripcion);
        valoresIniciales.put(KEY_VIADMIN, viaAdmin);
        valoresIniciales.put(KEY_URL_PROSPECTO, url_prospecto);
        return mDb.insert(TABLA_MEDICAMENTOS, null, valoresIniciales);
    }

    public boolean actualizarMedicamento(long rowId, String nombre, String descripcion, String prescripcion, String viaAdmin, String url_prospecto) {
        ContentValues valores = new ContentValues();
        valores.put(KEY_NOMBRE, nombre);
        valores.put(KEY_BODY, descripcion);
        valores.put(KEY_PRESCRIPCION, prescripcion);
        valores.put(KEY_VIADMIN, viaAdmin);
        valores.put(KEY_URL_PROSPECTO, url_prospecto);
        return mDb.update(TABLA_MEDICAMENTOS, valores, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean eliminarMedicamento(long rowId) {
        return mDb.delete(TABLA_MEDICAMENTOS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor obtenerTodosLosMedicamentos() {
        return mDb.query(TABLA_MEDICAMENTOS, TODOS_CAMPOS, null, null, null, null, null);
    }

    public Cursor obtenerMedicamento(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, TABLA_MEDICAMENTOS, TODOS_CAMPOS, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean borrarTodosLosMedicamentos() {
        return mDb.delete(TABLA_MEDICAMENTOS, null, null) > 0;
    }

    /*
    ------------Otra opción de como hacerlo----------------
    public void insertarMedicamento(String nombre, String descripcion, String prescripcion, String via_administracion) {
        String insertarMedicamento = "INSERT INTO " + TABLA_MEDICAMENTOS + " (" +
                KEY_NOMBRE + ", " + KEY_BODY + ", " + KEY_PRESCRIPCION + ", " + KEY_VIADMIN +
                ") VALUES ('" + nombre + "', '" + descripcion + "', '" + prescripcion + "', '" + via_administracion + "')";
        ejecutar(insertarMedicamento);
    }

    public void actualizarMedicamento(long rowId, String nombre, String descripcion, String prescripcion, String viaAdmin) {
        ContentValues valores = new ContentValues();
        valores.put(KEY_NOMBRE, nombre);
        valores.put(KEY_BODY, descripcion);
        valores.put(KEY_PRESCRIPCION, prescripcion);
        valores.put(KEY_VIADMIN, viaAdmin);
        String where = KEY_ROWID + "=" + rowId;
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLA_MEDICAMENTOS, valores, where, null);
    }

    public void actualizarMedicamento(long rowId, String nombre, String descripcion, String prescripcion, String via_administracion) {
        String actualizarMedicamento = "UPDATE " + TABLA_MEDICAMENTOS + " SET " +
                KEY_NOMBRE + "='" + nombre + "', " +
                KEY_BODY + "='" + descripcion + "', " +
                KEY_PRESCRIPCION + "='" + prescripcion + "', " +
                KEY_VIADMIN + "='" + via_administracion + "' WHERE " + KEY_ROWID + "=" + rowId;
        ejecutar(actualizarMedicamento);
    }

    public void eliminarMedicamento(long rowId) {
        String eliminarMedicamento = "DELETE FROM " + TABLA_MEDICAMENTOS + " WHERE " + KEY_ROWID + "=" + rowId;
        ejecutar(eliminarMedicamento);
    }
     */

    //---------- Metodos para la tabla de Dates ----------

    public Cursor getMedicationsByDate(String date) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {KEY_NOMBRE, COLUMN_TIME};
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};
        return db.query(TABLA_DATES, projection, selection, selectionArgs, null, null, null);
    }

    public Long insertMedication(String date, String medication, String time) {
        ContentValues valoresIniciales = new ContentValues();
        valoresIniciales.put(COLUMN_DATE, date);
        valoresIniciales.put(KEY_NOMBRE, medication);
        valoresIniciales.put(COLUMN_TIME, time);
        return mDb.insert(TABLA_DATES, null, valoresIniciales);
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
            valoresIniciales.put(KEY_NOMBRE, medication);
            valoresIniciales.put(COLUMN_TIME, time);
            long result = mDb.insert(TABLA_DATES, null, valoresIniciales);

            if (result != -1) {
                insertedRows++;
            }
        }

        return insertedRows;
    }

    // Método para insertar una lista de medicamentos en la base de datos SQLite
    /*
    public long insertarListaMedicamentos(List<Map<String, Object>> listaMedicamentos) {
        long totalInsertedRows = 0;

        for (Map<String, Object> medicamento : listaMedicamentos) {
            String nombre = medicamento.get(KEY_NOMBRE).toString();
            String descripcion = medicamento.get(KEY_BODY).toString();
            String prescripcion = medicamento.get(KEY_PRESCRIPCION).toString();
            String viaAdmin = medicamento.get(KEY_VIADMIN).toString();
            String url_prospecto = medicamento.get(KEY_URL_PROSPECTO).toString();

            long insertedRowId = insertarMedicamento(nombre, descripcion, prescripcion, viaAdmin, url_prospecto);

            if (insertedRowId != -1) {
                totalInsertedRows++;
            }
        }

        return totalInsertedRows;
    }

     */
    // Método para insertar una lista de medicamentos en la base de datos SQLite
    public long insertarListaMedicamentos2(List<Map<String, Object>> listaMedicamentos) {
        long totalInsertedRows = 0;

        for (Map<String, Object> medicamento : listaMedicamentos) {
            String nombre = medicamento.get("nombre").toString();

            // Verificar si el medicamento con el mismo nombre ya existe en la base de datos
            if (!existeMedicamento(nombre)) {
                String descripcion = medicamento.get(KEY_BODY).toString();
                String prescripcion = medicamento.get(KEY_PRESCRIPCION).toString();
                String viaAdmin = medicamento.get(KEY_VIADMIN).toString();
                String url_prospecto = medicamento.get(KEY_URL_PROSPECTO).toString();

                long insertedRowId = insertarMedicamento(nombre, descripcion, prescripcion, viaAdmin, url_prospecto);

                if (insertedRowId != -1) {
                    totalInsertedRows++;
                }
            }
        }

        return totalInsertedRows;
    }

    // Método para verificar si un medicamento con el mismo nombre ya existe en la base de datos
    public boolean existeMedicamento(String nombre) {
        Cursor cursor = mDb.query(TABLA_MEDICAMENTOS, new String[]{KEY_NOMBRE},
                KEY_NOMBRE + "=?", new String[]{nombre}, null, null, null);

        boolean existe = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return existe;
    }
}

