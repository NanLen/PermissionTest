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
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        CheckPermissionActivity.startActivity(MainActivity.this, permissions, new CheckPermissionActivity.PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //权限允许
                Log.d(TAG, "onPermissionGranted");
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //权限被拒绝
                Log.d(TAG, "onPermissionDenied");
                //https://developer.android.com/training/permissions/requesting.html?hl=zh-cn
                //true则说明,申请了权限，但被用户拒绝,可在此处解释应用为何需要此权限
                //false则说明用户拒绝并勾选了下次不再询问
                boolean b = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[0]);
                Log.d(TAG, "shouldShowRequestPermissionRationale=" + b);
            }
        });
    }
}
