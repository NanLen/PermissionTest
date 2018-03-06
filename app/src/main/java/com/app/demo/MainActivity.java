package com.app.demo;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        CheckPermissionActivity.startActivity(MainActivity.this, permissions, new CheckPermissionActivity.PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //权限允许
                Log.d(TAG, "onPermissionGranted");
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //权限被拒绝,可提示用户去权限设置中打开权限
                Log.d(TAG, "onPermissionDenied");
            }
        });
    }
}
