package com.example.farmaapp2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
/*
@RunWith(AndroidJUnit4.class)
public class MedicamentoAdapterTest {

    @Mock
    private Context mockContext;
    private MedicamentoAdapter medicamentoAdapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        medicamentoAdapter = new MedicamentoAdapter(mockContext);
    }

    @After
    public void tearDown() throws Exception {
        medicamentoAdapter.cerrar();
    }

    @Test
    public void testInsertarMedicamento() {
        String nombre = "Paracetamol";
        String descripcion = "Analgésico y antipirético";
        String prescripcion = "Tomar cada 6 horas";
        String viaAdmin = "Oral";
        String url_prospecto = "https://ejemplo.com/prospecto";

        long result = medicamentoAdapter.insertarMedicamento(nombre, descripcion, prescripcion, viaAdmin, url_prospecto);
        assertNotEquals(-1, result);
    }

    @Test
    public void testObtenerMedicamento() {
        String nombre = "Ibuprofeno";
        String descripcion = "Antiinflamatorio no esteroideo";
        String prescripcion = "Tomar después de las comidas";
        String viaAdmin = "Oral";
        String url_prospecto = "https://ejemplo.com/prospecto";

        long id = medicamentoAdapter.insertarMedicamento(nombre, descripcion, prescripcion, viaAdmin, url_prospecto);

        try (Cursor cursor = medicamentoAdapter.obtenerMedicamento(id)) {
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());

            if (cursor.moveToFirst()) {
                assertEquals(nombre, cursor.getString(cursor.getColumnIndex(MedicamentoAdapter.KEY_NOMBRE)));
                assertEquals(descripcion, cursor.getString(cursor.getColumnIndex(MedicamentoAdapter.KEY_BODY)));
                assertEquals(prescripcion, cursor.getString(cursor.getColumnIndex(MedicamentoAdapter.KEY_PRESCRIPCION)));
                assertEquals(viaAdmin, cursor.getString(cursor.getColumnIndex(MedicamentoAdapter.KEY_VIADMIN)));
                assertEquals(url_prospecto, cursor.getString(cursor.getColumnIndex(MedicamentoAdapter.KEY_URL_PROSPECTO)));
            } else {
                fail("Cursor is empty!");
            }
        }
    }

    @Test
    public void testEliminarMedicamento() {
        String nombre = "Aspirina";
        String descripcion = "Analgésico y antipirético";
        String prescripcion = "Tomar con abundante agua";
        String viaAdmin = "Oral";
        String url_prospecto = "https://ejemplo.com/prospecto";

        long id = medicamentoAdapter.insertarMedicamento(nombre, descripcion, prescripcion, viaAdmin, url_prospecto);
        boolean isDeleted = medicamentoAdapter.eliminarMedicamento(id);
        assertTrue(isDeleted);

        Cursor cursor = medicamentoAdapter.obtenerMedicamento(id);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());

        cursor.close();
    }

    // Puedes agregar más pruebas unitarias para los demás métodos según tus necesidades.
}
*/
