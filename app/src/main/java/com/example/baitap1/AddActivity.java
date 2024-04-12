package com.example.baitap1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddActivity extends AppCompatActivity {
    private DatabaseManager database;
    private EditText edtsbd,edtname,edttoan,edtly,edthoa;
    private Button addButton,backButton;
    private int type = 0;
    private Thisinh editThisinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        listener();
        Intent intent = getIntent();
        editThisinh =(Thisinh) intent.getSerializableExtra("Data");

        if (editThisinh != null){
            renderData(editThisinh);
            addButton.setText("Edit");
            type = 1;
        } else {
            Toast.makeText(this,"Not available",Toast.LENGTH_LONG).show();
        }

    }
    private void listener(){
        backButton.setOnClickListener(v->{
            finish();
        });
        addButton.setOnClickListener(v->{
            if (type ==0){
                addThiSinh();
            } else {
                setEditThisinh(editThisinh);
            }

        });

    }
    private void init(){
        database = new DatabaseManager(this);
        edtsbd = findViewById(R.id.sbd);
        edtname = findViewById(R.id.name);
        edttoan = findViewById(R.id.toan);
        edtly = findViewById(R.id.ly);
        edthoa = findViewById(R.id.hoa);
        backButton = findViewById(R.id.backButton);
        addButton = findViewById(R.id.addButton);
    }
    private void addThiSinh(){
        String id = edtsbd.getText().toString().trim();
        String name = edtname.getText().toString().trim();
        Double toan = Double.parseDouble( edttoan.getText().toString()) ;
        Double ly = Double.parseDouble( edtly.getText().toString()) ;
        Double hoa = Double.parseDouble( edthoa.getText().toString()) ;
        database.addThisinh(new Thisinh(id,name,toan,ly,hoa));

        finish();
    }
    private void renderData(Thisinh thisinh){
        edtsbd.setText(thisinh.getSbd());
        edtname.setText(thisinh.getName());
        edttoan.setText(String.valueOf(thisinh.getToan()));
        edtly.setText(String.valueOf(thisinh.getLy()));
        edthoa.setText(String.valueOf(thisinh.getHoa()));
        edtsbd.setVisibility(View.INVISIBLE);
    }
    private void setEditThisinh(Thisinh editThisinh){
        String id = editThisinh.getSbd();
        String name = edtname.getText().toString().trim();
        Double toan = Double.parseDouble( edttoan.getText().toString()) ;
        Double ly = Double.parseDouble( edtly.getText().toString()) ;
        Double hoa = Double.parseDouble( edthoa.getText().toString()) ;

        database.updateThisinh(new Thisinh(id,name,toan,ly,hoa));
        type = 0;
        finish();

    }
}