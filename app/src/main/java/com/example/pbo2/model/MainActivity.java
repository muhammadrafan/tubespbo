package com.example.pbo2.model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register1);
        TextInputLayout  textinputrole = findViewById(R.id.register_roles);
        MaterialAutoCompleteTextView autoCompleteTextView = findViewById(R.id.inputrole);
        Button button = findViewById(R.id.btn_register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().isEmpty()){
                    textinputrole.setError("Silakan Pilih");
                }else{
                    Toast.makeText(MainActivity.this,autoCompleteTextView.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}