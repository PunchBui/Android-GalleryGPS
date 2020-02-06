package com.example.punchja.gallerygpss;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.punchja.gallerygpss.DataBase.DataBaseHelper;
import com.example.punchja.gallerygpss.Model.GalleryItem;
import com.example.punchja.gallerygpss.Adapter.GalleryListAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_ID;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_IMAGE;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_LOCATION;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_TITLE;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private DataBaseHelper mHelper;
    private SQLiteDatabase mDb;
    private List<GalleryItem> mGalleryItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DataBaseHelper(MainActivity.this);
        mDb = mHelper.getWritableDatabase();

        Button addGalleryItemButton = findViewById(R.id.add_photo_item_button);
        addGalleryItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addPhotoActivity.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadGalleryData();
        setupListView();
    }

    private void loadGalleryData() {
        Cursor c = mDb.query(TABLE_NAME, null, null, null, null, null, null);

        mGalleryItemList = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(COL_ID));
            String title = c.getString(c.getColumnIndex(COL_TITLE));
            String number = c.getString(c.getColumnIndex(COL_LOCATION));
            String image = c.getString(c.getColumnIndex(COL_IMAGE));

            GalleryItem item = new GalleryItem(id, title, number, image);
            mGalleryItemList.add(item);
        }
        c.close();
    }

    private void setupListView() {
        GalleryListAdapter adapter = new GalleryListAdapter(
                MainActivity.this,
                R.layout.activity_picture_item,
                mGalleryItemList
        );
        ListView lv = findViewById(R.id.result_list_view);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GalleryItem item = mGalleryItemList.get(position);

                Toast t = Toast.makeText(MainActivity.this, item.title, Toast.LENGTH_SHORT);
                t.show();

                //Intent intent = new Intent(MainActivity.this,ShowPicture.class);
                //startActivity(intent);

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                String[] items = new String[]{
                        "Edit",
                        "Delete"
                };

                new AlertDialog.Builder(MainActivity.this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final GalleryItem phoneItem = mGalleryItemList.get(position);

                                switch (i) {
                                    case 0: // Edit
                                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                        intent.putExtra("title", phoneItem.title);
                                        intent.putExtra("number", phoneItem.location);
                                        intent.putExtra("id", phoneItem._id);
                                        startActivity(intent);
                                        break;
                                    case 1: // Delete
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setMessage("ต้องการลบข้อมูลเบอร์โทรนี้ ใช่หรือไม่")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        mDb.delete(
                                                                TABLE_NAME,
                                                                COL_ID + " = ?",
                                                                new String[]{String.valueOf(phoneItem._id)}
                                                        );
                                                        loadGalleryData();
                                                        setupListView();
                                                    }
                                                })
                                                .setNegativeButton("No", null)
                                                .show();
                                        break;
                                }
                            }
                        })
                        .show();

                return true;
            }
        });
    }
}

