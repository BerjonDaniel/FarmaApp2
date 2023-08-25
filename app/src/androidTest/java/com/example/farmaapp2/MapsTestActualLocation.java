package com.example.farmaapp2;

import androidx.test.platform.app.InstrumentationRegistry;

import android.content.Context;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import androidx.test.annotation.UiThreadTest;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.equalTo;

/*
@RunWith(AndroidJUnit4.class)
public class MapsTestActualLocation {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String actualCoordinates;
    private Location actualLocation;

    String preSetText = "Actual Position";


    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Actualizar el TextView con la ubicación actual del dispositivo
                String actualText = location.getLatitude() + "," + location.getLongitude();
                ViewInteraction locationTextView = onView(withId(R.id.locationTextView));
                locationTextView.check(matches(withText(actualText)));

                // Almacenar las coordenadas en la variable actualCoordinates
                actualCoordinates = actualText;
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    @Test
    public void testMapIsDisplayed() {

        // Realiza clic en el botón en la pantalla principal
        Espresso.onView(ViewMatchers.withId(R.id.imageButton)).perform(ViewActions.click());

        // Verificar que el mapa esté visible en la actividad
        onView(withId(R.id.mapsView)).check(matches(isDisplayed()));
    }

    @Test
    public void testLocationTextViewIsDisplayed() {

        // Realiza clic en el botón en la pantalla principal
        Espresso.onView(ViewMatchers.withId(R.id.imageButton)).perform(ViewActions.click());

        // Verificar que el TextView para mostrar la ubicación esté visible en la actividad
        onView(withId(R.id.locationTextView)).check(matches(isDisplayed()));
    }
    /*
    @Test
    public void testActualLocationIsDisplayed() {
        // Espera 5 segundos para obtener una ubicación real del dispositivo
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Realiza clic en el botón en la pantalla principal
        Espresso.onView(ViewMatchers.withId(R.id.imageButton)).perform(ViewActions.click());

        // Verificar que el TextView con las coordenadas esté visible en la actividad
        onView(withId(R.id.locationTextView)).check(matches(isDisplayed()));

        // Comprobar que la ubicación del dispositivo está habilitada
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Solicitar una única actualización de la ubicación del dispositivo
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);

            // Obtener el texto actual del TextView
            ViewInteraction locationTextView = onView(withId(R.id.locationTextView));
        } else {
            // Si la ubicación no está habilitada, falla la prueba
            throw new RuntimeException("La ubicación del dispositivo no está habilitada");
        }
    }

    @Test
    public void testActualLocationIsDisplayed() {
        // Realiza clic en el botón en la pantalla principal
        Espresso.onView(ViewMatchers.withId(R.id.imageButton)).perform(ViewActions.click());

        // Verificar que el TextView con las coordenadas esté visible en la actividad
        onView(withId(R.id.locationTextView)).check(matches(isDisplayed()));

        // Comprobar que la ubicación del dispositivo está habilitada
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Solicitar una única actualización de la ubicación del dispositivo
            activityRule.getScenario().onActivity(activity -> {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
            });

            // Esperar a que la interfaz de usuario esté inactiva
            InstrumentationRegistry.getInstrumentation().waitForIdleSync();

            // Verificar el texto actual del TextView con las coordenadas
            onView(withId(R.id.locationTextView)).check(matches(withText(not(isEmptyOrNullString()))));
            onView(withId(R.id.locationTextView)).check(matches(withText(not(equalTo(preSetText)))));

        } else {
            // Si la ubicación no está habilitada, falla la prueba
            throw new RuntimeException("La ubicación del dispositivo no está habilitada");
        }
    }

}

 */
