package com.example.read_file;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Form extends AppCompatActivity {
    String collection = "users";
    String formType;
    Student item;

    EditText edtId, edtName, edtMajor;
    Button btnBack, btnSave;
    TextView txtTitle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Bundle b = getIntent().getBundleExtra("params");
        if (b != null) {
            setItemByKey(b.getString("key"));
            formType = "edit";
        } else formType = "add";

        mapping();
        onInit();
    }

    private void setItemByKey(String key) {
        db.collection(collection).document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Student std = new Student(
                        documentSnapshot.getId(),
                        documentSnapshot.getString("id"),
                        documentSnapshot.getString("name"),
                        documentSnapshot.getString("major")
                );
                item = std;
                setFormData();
            }
        });
    }

    private void setFormData() {
        if (item != null) {
            edtId.setText(item.getId());
            edtName.setText(item.getName());
            edtMajor.setText(item.getMajor());
        }
    }

    private void onInit() {
        txtTitle.setText("Form");
        btnBack.setText("Back");

        // init events
        onBackBtnClick();
        onSaveBtnClick();
    }

    private void onBackBtnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onSaveBtnClick() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String major = edtMajor.getText().toString().trim();

                if (checkValid(id, name, major)) {
                    if (formType == "add") {
                        onInsert(id, name, major);
                    } else if (formType == "edit") {
                        onUpdate(id, name, major);
                    }
                } else Toast.makeText(Form.this, "All field required", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void onInsert(String id, String name, String major) {
        resetData();
        Student std = new Student(null, id, name, major);
        db.collection(collection).add(std.getStoredData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Form.this, "Item inserted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onUpdate(String id, String name, String major) {
        item.setId(id);
        item.setName(name);
        item.setMajor(major);

        db.collection(collection).document(item.getKey()).set(item.getStoredData()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Form.this, "Item updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkValid(String id, String name, String major) {
        if (id.equals("") || name.equals("") || major.equals("")) return false;
        return true;
    }

    private void resetData() {
        edtId.setText("");
        edtName.setText("");
        edtMajor.setText("");
    }

    private void mapping() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnBack = (Button) findViewById(R.id.btnTitle);
        btnSave = (Button) findViewById(R.id.btnSave);

        edtId = (EditText) findViewById(R.id.edtId);
        edtName = (EditText) findViewById(R.id.edtName);
        edtMajor = (EditText) findViewById(R.id.edtMajor);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Form.this, MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}