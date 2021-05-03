package com.example.sporteam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Permissions extends AppCompatActivity {
    private Button btnGrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        if(ContextCompat.checkSelfPermission(Permissions.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(Permissions.this,SearchFields.class));
            finish();
            return;
        }
        btnGrant = findViewById(R.id.btn_grant_permission);

        btnGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(Permissions.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        startActivity(new Intent(Permissions.this,SearchFields.class));
                        finish();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            if(permissionDeniedResponse.isPermanentlyDenied()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Permissions.this);
                                builder.setTitle(getString(R.string.permissionDeny)).setMessage(getString(R.string.permissionDeniedMessage))
                                        .setNegativeButton(getString(R.string.Cancel),null)
                                        .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                              Intent intent =   new Intent();
                                              intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                              intent.setData(Uri.fromParts("package",getPackageName(),null));

                                            }
                                        }).show();
                            }else{
                                Toast.makeText(Permissions.this,getString(R.string.permissionDeny),Toast.LENGTH_LONG);
                            }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });
    }
}