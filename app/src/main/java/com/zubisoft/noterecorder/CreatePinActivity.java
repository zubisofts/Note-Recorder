package com.zubisoft.noterecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zubisoft.noterecorder.utils.NoteRecorderPreference;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;

import java.util.Objects;

public class CreatePinActivity extends AppCompatActivity {

    private TextInputLayout inputPassword;
    private TextInputEditText edtPassword;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CategoryViewModelFactory categoryViewModelFactory=new CategoryViewModelFactory(getApplication());
        categoryViewModel= categoryViewModelFactory.create(CategoryViewModel.class);

        setContentView(R.layout.activity_create_pin);
        MaterialButton btn_create = findViewById(R.id.btn_create);

        inputPassword=findViewById(R.id.inputPassword);
        edtPassword=findViewById(R.id.edt_password);
        btn_create.setEnabled(false);

        btn_create.setOnClickListener(view -> {
            categoryViewModel.createPassword(Objects.requireNonNull(edtPassword.getText()).toString());
            startActivity(new Intent(CreatePinActivity.this,HomeActivity.class));
            NoteRecorderPreference.getInstance(CreatePinActivity.this)
                    .setFirstUser(false);
            finish();
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<5){
                    inputPassword.setError("Password must not be less than 5 characters");
                    btn_create.setEnabled(false);
                }else if(TextUtils.isEmpty(s)){
                    inputPassword.setError("Password must not be empty");
                    btn_create.setEnabled(false);
                }else {
                    inputPassword.setError(null);
                    btn_create.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}