package com.example.farmaapp2;

import androidx.test.core.app.ActivityScenario;
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

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertThat;


import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;


@RunWith(AndroidJUnit4.class)
public class CodigoNacionalTest {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testBarcodeConfirmation_ShowsProductName() {
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();
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

/*

    @Rule
    public IntentsTestRule<MyActivity> intentsTestRule =
            new IntentsTestRule<>(MyActivity.class);



    @Before
    public void stubScanIntent(){

        // Initializes Intents and begins recording intents.
        Intents.init();

        Instrumentation.ActivityResult result = ActivityResultStub();

        // Stub the Intent.
        //intending(hasAction()).respondWith(result);
    }

    @After
    public void tearDown(){
        // Clears Intents state.
        Intents.release();
    }

    @Test
    public void testMenuItemClick_ShowsBarcodeView() {

        // Hacer clic en el botón que infla el menú
        onView(withId(R.id.button)).perform(click());

        // Hacer clic en el primer elemento del menú (Código de barras)
        onView(withText("Codigo de barras")).perform(click());

        //La camara con el escaner se abrirá, y el código de test detectará el intent

    }

    private Instrumentation.ActivityResult ActivityResultStub() {
        Bundle bundle = new Bundle();
        //Añadirle la info al bundle

        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

 */



}
