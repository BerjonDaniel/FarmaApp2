package com.example.farmaapp2;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestGoogleMapsUI {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionLocation = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private double latitude = 40.405845; // Latitud de ejemplo
    private double longitude = -3.79297; // Longitud de ejemplo
    private String position = latitude + "," + longitude;

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown(){
        // Clears Intents state.
        Intents.release();
    }

    @Test
    public void testMapsView() {

        // Mock the Intent for barcode scanning
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, createResultData(position)));

        // Realiza clic en el botón en la pantalla principal
        Espresso.onView(ViewMatchers.withId(R.id.imageButton)).perform(ViewActions.click());

        // Verifica que se haya abierto la vista de Google Maps
        Espresso.onView(ViewMatchers.withId(R.id.mapsView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        //Verifica que se vean las coordenadas escritas
        Espresso.onView(ViewMatchers.withId(R.id.locationTextView)).check(matches(withText(containsString(40.405845 + "," + -3.79297))));
    }

    private Intent createResultData(String location) {
        Intent intent = new Intent();
        intent.putExtra("LOCATION", location);
        return intent;
    }

}

