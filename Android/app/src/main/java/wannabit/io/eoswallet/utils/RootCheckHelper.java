package wannabit.io.eoswallet.utils;

import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RootCheckHelper {

    public static boolean isRooted() {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) { }

        if(canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su")) {
            return true;
        }

        return isEmulator(-1);
    }

    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                executedSuccesfully = true;
            } else {
                executedSuccesfully = false;
            }

        } catch (Exception e) {
            executedSuccesfully = false;

        } finally {
            if (process != null) process.destroy();
        }

        return executedSuccesfully;
    }


    public static boolean isEmulator(int rating) {
        int newRating = 0;
        if(rating < 0) {
            if (Build.PRODUCT.contains("sdk") ||
                    Build.PRODUCT.contains("Andy") ||
                    Build.PRODUCT.contains("ttVM_Hdragon") ||
                    Build.PRODUCT.contains("google_sdk") ||
                    Build.PRODUCT.contains("Droid4X") ||
                    Build.PRODUCT.contains("nox") ||
                    Build.PRODUCT.contains("sdk_x86") ||
                    Build.PRODUCT.contains("sdk_gphone_x86") ||
                    Build.PRODUCT.contains("sdk_google") ||
                    Build.PRODUCT.contains("vbox86p")) {
                newRating++;
            }

            if (Build.MANUFACTURER.equals("unknown") ||
                    Build.MANUFACTURER.equals("Genymotion") ||
                    Build.MANUFACTURER.contains("Andy") ||
                    Build.MANUFACTURER.contains("MIT") ||
                    Build.MANUFACTURER.contains("nox") ||
                    Build.MANUFACTURER.contains("TiantianVM")){
                newRating++;
            }

            if (Build.BRAND.equals("generic") ||
                    Build.BRAND.equals("generic_x86") ||
                    Build.BRAND.equals("TTVM") ||
                    Build.BRAND.contains("Andy")) {
                newRating++;
            }

            if (Build.DEVICE.contains("generic") ||
                    Build.DEVICE.contains("generic_x86") ||
                    Build.DEVICE.contains("Andy") ||
                    Build.DEVICE.contains("ttVM_Hdragon") ||
                    Build.DEVICE.contains("Droid4X") ||
                    Build.DEVICE.contains("nox") ||
                    Build.DEVICE.contains("generic_x86_64") ||
                    Build.DEVICE.contains("vbox86p")) {
                newRating++;
            }

            if (Build.MODEL.equals("sdk") ||
                    Build.MODEL.equals("google_sdk") ||
                    Build.MODEL.contains("Droid4X") ||
                    Build.MODEL.contains("TiantianVM") ||
                    Build.MODEL.contains("Andy") ||
                    Build.MODEL.equals("Android SDK built for x86_64") ||
                    Build.MODEL.equals("Android SDK built for x86")) {
                newRating++;
            }

            if (Build.HARDWARE.equals("goldfish") ||
                    Build.HARDWARE.equals("vbox86") ||
                    Build.HARDWARE.contains("nox") ||
                    Build.HARDWARE.contains("ranchu") ||
                    Build.HARDWARE.contains("ttVM_x86")) {
                newRating++;
            }

            if (Build.FINGERPRINT.contains("generic/sdk/generic") ||
                    Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86") ||
                    Build.FINGERPRINT.contains("Andy") ||
                    Build.FINGERPRINT.contains("ttVM_Hdragon") ||
                    Build.FINGERPRINT.contains("generic_x86_64") ||
                    Build.FINGERPRINT.contains("generic/google_sdk/generic") ||
                    Build.FINGERPRINT.contains("vbox86p") ||
                    Build.FINGERPRINT.contains("generic/vbox86p/vbox86p")) {
                newRating++;
            }

            try {
                String opengl = android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_RENDERER);
                if (opengl != null){
                    if( opengl.contains("Bluestacks") ||
                            opengl.contains("Translator")
                            )
                        newRating += 10;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                File sharedFolder = new File(Environment
                        .getExternalStorageDirectory().toString()
                        + File.separatorChar
                        + "windows"
                        + File.separatorChar
                        + "BstSharedFolder");

                if (sharedFolder.exists()) {
                    newRating += 10;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            rating = newRating;
        }
        return rating > 3;
    }
}
