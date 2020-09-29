package com.example.read_file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView lvUsers;
    Button btnAdd;
    TextView txtTitle;
    String collection = "users";

    ArrayList<Student> studentArrayList;
    StudentAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mapping
        mapping();

        // on init
        onInit();

        // list all
        listAll();

        initListView();
    }

    private void onInit(){
        studentArrayList = new ArrayList<>();
        txtTitle.setText("Users");
        btnAdd.setText("Add");
        onAddBtnClick();
    }

    // list view
    private void initListView(){
        adapter = new StudentAdapter(this, R.layout.student_layout, studentArrayList);
        lvUsers.setAdapter(adapter);
        onListViewClick();
    }

    private void onListViewClick(){
        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUserDetailDialog(studentArrayList.get(position));
            }
        });
    }

    private void showUserDetailDialog(final Student std){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_view_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);

        TextView txtId = (TextView) dialog.findViewById(R.id.txtId);
        TextView txtName = (TextView) dialog.findViewById(R.id.txtName);
        TextView txtMajor = (TextView) dialog.findViewById(R.id.txtMajor);
        Button btnEdit = (Button) dialog.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) dialog.findViewById(R.id.btnDelete);

        txtId.setText("ID: "+std.getId());
        txtName.setText("Name: "+std.getName());
        txtMajor.setText("Major: "+std.getMajor());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClick(std);
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteWarning(std);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDeleteWarning(final Student std){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Do you want to delete \""+std.getName()+"\"");

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.collection(collection).document(std.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "item deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void onEditClick(Student std){
        Intent i = new Intent(MainActivity.this, Form.class);
        Bundle b = new Bundle();
        b.putString("key", std.getKey());
        i.putExtra("params", b);
        startActivity(i);
    }

    // add
    private void onAddBtnClick(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Form.class);
                startActivity(i);
            }
        });
    }

    // list all
    private void listAll(){
        db.collection(collection).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                studentArrayList.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot: value){
                    Student std = new Student(
                            queryDocumentSnapshot.getId(),
                            queryDocumentSnapshot.getString("id"),
                            queryDocumentSnapshot.getString("name"),
                            queryDocumentSnapshot.getString("major")
                    );
                    studentArrayList.add(std);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    // mapping
    private void mapping() {
        lvUsers = (ListView) findViewById(R.id.lvUsers);
        txtTitle = (TextView)  findViewById(R.id.txtTitle);
        btnAdd = (Button) findViewById(R.id.btnTitle);
    }
}