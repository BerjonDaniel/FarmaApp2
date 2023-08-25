package com.example.farmaapp2;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

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

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/*
@RunWith(AndroidJUnit4.class)
public class MapsTestSimulatedLocation {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Habilitar ubicaciones simuladas en el emulador o dispositivo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                // Si no se han otorgado los permisos para ubicaciones simuladas, solicítalos.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

        // Configurar ubicación simulada
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false,
                false, false, true, true, true,
                android.location.Criteria.POWER_HIGH, android.location.Criteria.ACCURACY_FINE);

        // Establecer la ubicación simulada
        Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
        mockLocation.setLatitude(40.7128); // Coordenada de latitud simulada
        mockLocation.setLongitude(-74.0060); // Coordenada de longitud simulada
        mockLocation.setAltitude(0); // Altitud simulada
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setAccuracy(1.0f); // Precisión simulada
        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation);

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

    @Test
    public void testActualLocationIsDisplayed() {
        // Espera 5 segundos para que la aplicación obtenga la ubicación simulada
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verificar que el TextView con las coordenadas esté visible en la actividad
        onView(withId(R.id.locationTextView)).check(matches(isDisplayed()));

        // Obtener el texto actual del TextView
        ViewInteraction locationTextView = onView(withId(R.id.locationTextView));
        String actualText = "";
        locationTextView.check(matches(withText(actualText)));

        // Obtener las coordenadas simuladas del dispositivo
        double actualLatitude = 40.7128;
        double actualLongitude = -74.0060;
        actualText = actualLatitude + "," + actualLongitude;

        // Verificar que el TextView muestre las coordenadas simuladas del dispositivo
        locationTextView.check(matches(withText(actualText)));
    }


}

 */
