package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.notes_app.databinding.ActivitySignupScreenBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup_screen extends AppCompatActivity {

    ActivitySignupScreenBinding binding;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = (ProgressBar)findViewById(R.id.spin_kit);

//        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
//        Sprite doubleBounce = new FadingCircle();
//        progressBar.setIndeterminateDrawable(doubleBounce);

        binding.signupscreenLoginTxtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(signup_screen.this, login_screen.class);
                startActivity(login);
            }
        });

        binding.signupscreenBtnSignup.setOnClickListener(V -> Createaccount());
    }

    void Createaccount() {
        String email = binding.inputlayoutEmail.getEditText().getText().toString();
        String password = binding.inputlayoutPassword.getEditText().getText().toString();
        String conformpassword = binding.inputlaoutConformpassword.getEditText().getText().toString();

        boolean isvalidated = validatedata(email, password, conformpassword);
        if (!isvalidated) {
            return;
        }

        createaccountinfirebase(email, password);

    }

    void createaccountinfirebase(String email, String password) {
        changeinprogress(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    changeinprogress(false);
                    Utility.showToast(signup_screen.this, "Successfully create account,Check email to verify");
//                    auth.getCurrentUser().sendEmailVerification();
                    auth.signOut();
                    finish();
                }else {
                    Utility.showToast(signup_screen.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeinprogress(boolean inprogress) {
        if (inprogress) {
            progressBar.setVisibility(View.VISIBLE);
            Sprite doubleBounce = new FadingCircle();
            progressBar.setIndeterminateDrawable(doubleBounce);
//            binding.signupscreenBtnSignup.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
//            binding.signupscreenBtnSignup.setVisibility(View.VISIBLE);
        }
    }

    boolean validatedata(String email, String password, String conformpassword) {
        // validate the data that are input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputlayoutEmail.setErrorEnabled(true);
            binding.inputlayoutEmail.setError("invalid email");
            return false;
        } else {
            binding.inputlayoutEmail.setErrorEnabled(false);
        }

        if (password.length() < 6) {
            binding.inputlayoutPassword.setErrorEnabled(true);
            binding.inputlayoutPassword.setError("please enter 6 or more digit");
            return false;
        } else {
            binding.inputlayoutPassword.setErrorEnabled(false);
        }

        if (!password.equals(conformpassword)) {
            binding.inputlaoutConformpassword.setErrorEnabled(true);
            binding.inputlaoutConformpassword.setError("password not match");
            return false;
        } else {
            binding.inputlaoutConformpassword.setErrorEnabled(false);
        }
        return true;

    }
}