package com.example.punchja.gallerygpss;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.punchja.gallerygpss.DataBase.DataBaseHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_IMAGE;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_LOCATION;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_TITLE;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.TABLE_NAME;

public class addPhotoActivity extends AppCompatActivity {

    private static final String TAG = addPhotoActivity.class.getName();

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDb;
    private String mLogoFilename = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        mHelper = new DataBaseHelper(this);
        mDb = mHelper.getWritableDatabase();

        final Button getLoca = (Button) findViewById(R.id.get_location_btn);
        getLoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doInsertGalleryItem();
            }
        });

        ImageView logoImageView = findViewById(R.id.logo_image_view);
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(addPhotoActivity.this, "เลือกรูปภาพ", 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> list, EasyImage.ImageSource imageSource, int i) {
                File logoFile = list.get(0);
                Log.i(TAG, logoFile.getAbsolutePath());
                Log.i(TAG, logoFile.getName());

                File privateDir = getFilesDir();
                File dstFile = new File(privateDir, logoFile.getName());
                try {
                    copy(logoFile, dstFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mLogoFilename = logoFile.getName();
                ImageView logoImageView = findViewById(R.id.logo_image_view);

                Bitmap bitmap = BitmapFactory.decodeFile(logoFile.getAbsolutePath(), null);
                logoImageView.setImageBitmap(bitmap);
            }
        });
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    private void doInsertGalleryItem() {
        EditText titleEditText = findViewById(R.id.title_edit_text);
        EditText numberEditText = findViewById(R.id.location_edit_text);

        String title = titleEditText.getText().toString();
        String number = numberEditText.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_LOCATION, number);
        cv.put(COL_IMAGE, mLogoFilename);
        mDb.insert(TABLE_NAME, null, cv);

        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
