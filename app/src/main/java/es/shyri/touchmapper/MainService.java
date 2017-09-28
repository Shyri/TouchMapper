package es.shyri.touchmapper;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by shyri on 05/09/17.
 */

public class MainService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        try {
            EventInput eventInput = new EventInput();
            for (int i = 0; i < 100; i++) {
                Log.d("SHIT", getApkLocation());
//                eventInput.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 0, SystemClock.uptimeMillis(), 2, 1500,
                //                                             500, 0);
                //                eventInput.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN, 1, SystemClock
                // .uptimeMillis(), 2, 1500,
                //                                             500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getApkLocation() {
        PackageManager pm = getPackageManager();

        for (ApplicationInfo app : pm.getInstalledApplications(0)) {
            //            Log.d("PackageList", "package: " + app.packageName + ", sourceDir: " + app.sourceDir);
            if (app.packageName.equals(getPackageName())) {
                return app.sourceDir;
            }
        }
        return null;
    }
}
