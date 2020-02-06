package com.example.punchja.gallerygpss;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.punchja.gallerygpss.DataBase.DataBaseHelper;

import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_ID;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_LOCATION;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.COL_TITLE;
import static com.example.punchja.gallerygpss.DataBase.DataBaseHelper.TABLE_NAME;

public class EditActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mLocationEditText;
    private Button mSaveButton;

    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String location = intent.getStringExtra("location");
        mId = intent.getLongExtra("id", 0);

        mTitleEditText = findViewById(R.id.title_edit_text);
        mLocationEditText = findViewById(R.id.location_edit_text);
        mSaveButton = findViewById(R.id.save_button);

        mTitleEditText.setText(title);
        mLocationEditText.setText(location);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: บันทึกข้อมูลใหม่ลง db
                DataBaseHelper helper = new DataBaseHelper(EditActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                String newTitle = mTitleEditText.getText().toString().trim();
                String newNumber = mLocationEditText.getText().toString().trim();

                ContentValues cv = new ContentValues();
                cv.put(COL_TITLE, newTitle);
                cv.put(COL_LOCATION, newNumber);

                db.update(
                        TABLE_NAME,
                        cv,
                        COL_ID + " = ?",
                        new String[]{String.valueOf(mId)}
                );
                finish();
            }
        });
    }
}
