package com.app.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by liyanan on 2018/3/5.
 */

public class CheckPermissionActivity extends AppCompatActivity {
    private final int REQUEST_CODE_PERMISSION = 1000;
    private static final String EXTRA_PERMISSIONS = "permissions";
    private static PermissionListener permissionListener;

    public static void startActivity(Context context, String[] permissions, PermissionListener listener) {
        permissionListener = listener;
        Intent intent = new Intent(context, CheckPermissionActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        context.startActivity(intent);
    }

    private void invasionStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invasionStatusBar(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            finishAndCallBack();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
        String[] denyPermissions;
        ArrayList<String> denyList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                denyList.add(permission);
        }
        if (denyList.size() > 0) {
            //有未授权
            denyPermissions = denyList.toArray(new String[denyList.size()]);
            ActivityCompat.requestPermissions(this, denyPermissions, REQUEST_CODE_PERMISSION);
        } else {
            //已全部授权
            finishAndCallBack();
        }
    }

    private void finishAndCallBack() {
        if (permissionListener != null) {
            permissionListener.onPermissionGranted();
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> deniedPermissions = new ArrayList();
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }

            }
        }
        if (deniedPermissions.size() > 0) {
            if (permissionListener != null) {
                permissionListener.onPermissionDenied(deniedPermissions);
            }

        } else {
            if (permissionListener != null) {
                permissionListener.onPermissionGranted();
            }
        }
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (permissionListener != null) {
            permissionListener = null;
        }
    }

    public interface PermissionListener {
        /**
         * 用户允许
         */
        void onPermissionGranted();

        /**
         * 用户拒绝
         */
        void onPermissionDenied(ArrayList<String> deniedPermissions);
    }
}
