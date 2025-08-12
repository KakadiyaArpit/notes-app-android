package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;

import com.example.notes_app.databinding.ActivityLoginScreenBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_screen extends AppCompatActivity {

    ActivityLoginScreenBinding binding;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = (ProgressBar)findViewById(R.id.spin_kit);

        binding.loginscreenSignupTxtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(login_screen.this, signup_screen.class);
                startActivity(signup);
            }
        });

        binding.loginscreenBtnLogin.setOnClickListener(V->loginuser());
    }

    void loginuser() {
        String email = binding.loginscreenInputlayoutEmail.getEditText().getText().toString();
        String password = binding.loginscreenInputlayoutPassword.getEditText().getText().toString();

        boolean isvalidated = validatedata(email,password);
        if (!isvalidated) {
            return;
        }

        loginaccountinfirebase(email, password);
    }

    void loginaccountinfirebase(String email,String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        changeinprogress(true);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinprogress(false);
                if(task.isSuccessful()){
                    // login is suceess
//                    if (auth.getCurrentUser().isEmailVerified()){
                        // go to main  activity
                        startActivity(new Intent(login_screen.this, MainActivity.class));
                        finish();

//                    }else {
//                        Utility.showToast(login_screen.this,"email not verified please verify first");
//                    }

                }else {
                    Utility.showToast(login_screen.this,task.getException().getLocalizedMessage());
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

    boolean validatedata(String email, String password) {
        // validate the data that are input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.loginscreenInputlayoutEmail.setErrorEnabled(true);
            binding.loginscreenInputlayoutEmail.setError("invalid email");
            return false;
        } else {
            binding.loginscreenInputlayoutEmail.setErrorEnabled(false);
        }

        if (password.length() < 6) {
            binding.loginscreenInputlayoutPassword.setErrorEnabled(true);
            binding.loginscreenInputlayoutPassword.setError("please enter 6 or more digit");
            return false;
        } else {
            binding.loginscreenInputlayoutPassword.setErrorEnabled(false);
        }

        return true;

    }
}