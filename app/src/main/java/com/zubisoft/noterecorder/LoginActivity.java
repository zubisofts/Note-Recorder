package com.zubisoft.noterecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zubisoft.noterecorder.data.UserData;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputPassword;
    private TextInputEditText edtPassword;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CategoryViewModelFactory categoryViewModelFactory = new CategoryViewModelFactory(getApplication());
        categoryViewModel = categoryViewModelFactory.create(CategoryViewModel.class);

        setContentView(R.layout.activity_login);
        MaterialButton btn_login = findViewById(R.id.btn_login);

        inputPassword = findViewById(R.id.inputPassword);
        edtPassword = findViewById(R.id.edt_password);
        btn_login.setEnabled(false);

        btn_login.setOnClickListener(view -> {
            categoryViewModel.getUserData(edtPassword.getText().toString()).observe(this, new Observer<UserData>() {
                @Override
                public void onChanged(UserData userData) {
                    if (userData!=null){
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Invalid Password! Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 5) {
                    inputPassword.setError("Password must not be less than 5 characters");
                    btn_login.setEnabled(false);
                } else if (TextUtils.isEmpty(s)) {
                    inputPassword.setError("Password must not be empty");
                    btn_login.setEnabled(false);
                } else {
                    inputPassword.setError(null);
                    btn_login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}