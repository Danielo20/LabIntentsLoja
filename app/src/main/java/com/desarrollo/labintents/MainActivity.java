package com.desarrollo.labintents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int CAMERA_PIC_REQUEST=1;
    private int contador=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Permisos para usar el GPS
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){

            } else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
        //Fin Permisos para usar el GPS

        final EditText edt_nombre = (EditText)findViewById(R.id.edt_nombre);
        final EditText edt_apellido = (EditText)findViewById(R.id.edt_apellido);
        final EditText edt_dni = (EditText)findViewById(R.id.edt_dni);
        final EditText edt_codigo = (EditText)findViewById(R.id.edt_codigo);
        final ImageView img_camera = (ImageView)findViewById(R.id.img_camera);

        Button btn_camera = (Button)findViewById(R.id.btn_camera);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_PIC_REQUEST);
            }
        });

        EditText edt_msn = (EditText)findViewById(R.id.edt_nombre);
        ((Button)findViewById(R.id.btn_send)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent EnviarIntent = new Intent();
                if(contador == 0){
                    Toast.makeText(getApplicationContext(),"Asegurese de tomar una foto",Toast.LENGTH_SHORT).show();
                }
                else {
                    Bitmap bitmap = ((BitmapDrawable)img_camera.getDrawable()).getBitmap();
                    EnviarIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    EnviarIntent.putExtra("nombre",edt_nombre.getText().toString());
                    EnviarIntent.putExtra("apellido",edt_apellido.getText().toString());
                    EnviarIntent.putExtra("dni",edt_dni.getText().toString());
                    EnviarIntent.putExtra("codigo",edt_codigo.getText().toString());
                    EnviarIntent.putExtra("imagen",bitmap);
                    EnviarIntent.setType("text/plain");
                    if(EnviarIntent.resolveActivity(getPackageManager()) != null){
                        startActivity(Intent.createChooser(EnviarIntent, getResources().getText(R.string.chooser_msn)));
                    }
                }
            }
        });

        Button btn_location=(Button) findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                    LocationListener locationListener = new LocationListener() {
                        @SuppressLint("WrongViewCast")
                        @Override
                        public void onLocationChanged(Location location) {
                            ((EditText) findViewById(R.id.txt_location)).setText("" + location.getLatitude() + " " + location.getLongitude());
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                    int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_PIC_REQUEST){
            if(resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                ImageView img_camera = (ImageView)findViewById(R.id.img_camera);
                img_camera.setImageBitmap(bitmap);
            }
        }
    }


}
