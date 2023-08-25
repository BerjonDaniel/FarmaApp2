package com.example.farmaapp2;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.farmaapp2.R;
/*
@RunWith(AndroidJUnit4.class)
public class GuardarMedicamentoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    @After
    public void tearDown() {
        // Realiza tareas de limpieza aquí si es necesario
    }

    @Test
    public void testGuardarMedicamento() {
        // Simula la interacción del usuario con los elementos de la vista
        // Puedes usar ViewMatchers para localizar los elementos y ViewActions para interactuar con ellos
        ViewInteraction nombreMedicamentoEditText = Espresso.onView(ViewMatchers.withId(R.id.nombre_medicamento));
        nombreMedicamentoEditText.perform(ViewActions.typeText("Ibuprofeno"), ViewActions.closeSoftKeyboard());

        // Repite lo mismo para los demás elementos de la vista

        // Llama al método guardar mediante la acción de hacer clic en el botón
        ViewInteraction guardarButton = Espresso.onView(ViewMatchers.withId(R.id.guardar_medicamento));
        guardarButton.perform(ViewActions.click());

        // Verificamos que el método insertarMedicamento fue llamado con los datos correctos
        // En este caso, se verifica que el nombre "Ibuprofeno" se haya pasado al método insertarMedicamento
        // Dependiendo de cómo se use el MedicamentoAdapter, podrías necesitar verificar otros datos también
        // Mockito.verify(medicamentoAdapter).insertarMedicamento("Ibuprofeno", /* otros datos );

        // Aseguramos que se muestra el Toast correcto (esto dependerá de cómo manejas los Toasts)
        // assertTrue(guardarMedicamento.isToastShown()); // Debes implementar este método en GuardarMedicamento

        // Aseguramos que la actividad se cierre después de guardar el medicamento
        // assertTrue(guardarMedicamento.isActivityFinished()); // Debes implementar este método en GuardarMedicamento
    }
}

 */


