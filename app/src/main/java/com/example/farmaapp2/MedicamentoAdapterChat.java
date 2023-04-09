package com.example.farmaapp2;

import java.sql.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicamentoAdapterChat {
    private Connection conexion = null;
    private static final String TABLA_MEDICAMENTOS = "medicamentos";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_BODY = "descripcion";
    public static final String KEY_PRESCRIPCION = "prescripcion";
    public static final String KEY_VIADMIN = "via_administracion";
    public static final String KEY_URL_PROSPECTO = "url_prospecto";

    private DataBaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mContext;

    private static final String[] TODOS_CAMPOS = {KEY_ROWID, KEY_NOMBRE, KEY_BODY, KEY_PRESCRIPCION, KEY_VIADMIN, KEY_URL_PROSPECTO};

    public MedicamentoAdapterChat(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        private static final String NOMBRE_BASE_DATOS = "medicamentos.db";
        private static final int VERSION_BASE_DATOS = 3;

        private static final String CREAR_TABLA_MEDICAMENTOS =
                "CREATE TABLE " + TABLA_MEDICAMENTOS + " ("
                        + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_NOMBRE + " TEXT NOT NULL, "
                        + KEY_BODY+ " TEXT NOT NULL, "
                        + KEY_PRESCRIPCION + " TEXT NOT NULL, "
                        + KEY_VIADMIN + " TEXT NOT NULL, "
                        + KEY_URL_PROSPECTO + " TEXT NOT NULL);";

        public DataBaseHelper(Context context) {
            super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREAR_TABLA_MEDICAMENTOS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_MEDICAMENTOS);
            onCreate(db);
        }
    }

    public MedicamentoAdapterChat abrir() throws SQLException {
        mDbHelper = new DataBaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        mDbHelper.close();
    }

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
}

