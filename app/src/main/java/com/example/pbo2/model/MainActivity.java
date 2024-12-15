package com.example.pbo2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register1);
        TextInputLayout  textinputrole = findViewById(R.id.inputlayout);
        MaterialAutoCompleteTextView autoCompleteTextView = findViewById(R.id.inputTv);
        Button button = findViewById(R.id.btn);

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