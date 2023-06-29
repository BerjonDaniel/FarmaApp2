package com.example.farmaapp2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;

import static org.hamcrest.CoreMatchers.not;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BarcodeScanTest {

    private IdlingResource idlingResource;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
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
    public void testBarcodeScanning() {
        // Mock the Intent for barcode scanning
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, createResultData("8470006927199")));

        //Verificamos que se ve la vista principal
        onView(withId(R.id.resultado)).check(matches(isDisplayed()));

        // Hacer clic en el botón que infla el menú
        onView(withId(R.id.button)).perform(click());

        // Click on the barcode scan item
        onView(withText("Codigo de barras")).perform(click());

        //Verificamos que se ha abierto la vista de resumen del medicamento
        onView(withId(R.id.resultado222)).check(matches(isDisplayed()));

        // Verify that the barcode text is displayed
        onView(withId(R.id.resultado222)).check(matches(withText("692719")));
    }

    private Intent createResultData(String barcode) {
        Intent intent = new Intent();
        intent.putExtra("SCAN_RESULT", barcode);
        return intent;
    }
}
