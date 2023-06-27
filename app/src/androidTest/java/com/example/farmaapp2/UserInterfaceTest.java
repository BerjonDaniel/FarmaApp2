package com.example.farmaapp2;

import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.pm.PackageManager;

@RunWith(AndroidJUnit4.class)
public class UserInterfaceTest {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;


    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

/*
    @Test
    public void testMenuItemClick_ShowsBarcodeView() {
        // Hacer clic en el botón que infla el menú
        onView(withId(R.id.button)).perform(click());

        // Hacer clic en el primer elemento del menú (Código de barras)
        onView(withText("Codigo de barras")).perform(click());

        // Verificar que la vista de código de barras se muestra
        onView(withId(R.id.barcode_view)).check(matches(isDisplayed()));
    }

 */

    @Test
    public void testBarcodeConfirmation_ShowsProductName() {
        // Hacer clic en el botón que infla el menú
        onView(withId(R.id.button)).perform(click());

        // Hacer clic en el segundo elemento del menú (Código nacional)
        onView(withText("Codigo nacional")).perform(click());

        // Introducir un código de barras en el campo de texto
        onView(withId(R.id.title_cn)).perform(typeText("692719"), closeSoftKeyboard());

        // Hacer clic en el botón de confirmar
        onView(withId(R.id.confirm)).perform(click());

        // Verificar que la vista de nombre del medicamento se muestra
        onView(withId(R.id.nombre_medicamento)).check(matches(isDisplayed()));

        // Verificar que el nombre del medicamento se muestra correctamente
        onView(withId(R.id.nombre_medicamento)).check(matches(withText("ELOCOM 1 MG/G CREMA")));
    }

    @Test
    public void testMapsView() {
        // Realiza clic en el botón en la pantalla principal
        Espresso.onView(ViewMatchers.withId(R.id.imageButton)).perform(ViewActions.click());

        // Verifica que se haya abierto la vista de Google Maps
        Espresso.onView(ViewMatchers.withId(R.id.mapsView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    /*
    @Test
    public void testViewDiaSeleccionado(){

    }
    @Test
    public void testButtonActualDate(){

    }

     */


}
