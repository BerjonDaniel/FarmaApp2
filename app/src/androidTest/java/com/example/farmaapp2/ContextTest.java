package com.example.farmaapp2;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ContextTest {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;


    //Codigo que usaremos para pruebas de contexto en permisos de la aplicacion
    @Before
    public void setup() {
        // Revoca los permisos antes de cada prueba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            context.getPackageManager().setApplicationEnabledSetting(
                    context.getPackageName(),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
        }
    }

    @Test
    public void contextTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String appName = context.getResources().getString(R.string.app_name);
        assertEquals(appName, "FarmaApp2");
    }
}

