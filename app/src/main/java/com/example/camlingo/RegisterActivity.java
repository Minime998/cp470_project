package com.example.camlingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private EditText UsernameEditText;
    private EditText PassWEditText;
    private EditText PassWEditText2;
    private SharedPreferences prefs;
    private FirebaseAuth mAuth;

    public static String TAG = "RegisterActivity";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i(TAG, "User already logged in");
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // reference parent activity
        View parentLayout = findViewById(R.id.registerActivity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);
        UsernameEditText = findViewById(R.id.username_input);
        PassWEditText = findViewById(R.id.password_input);
        PassWEditText2 = findViewById(R.id.password_input2);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        FormValidator formValidator = new FormValidator();

        //when the login button is pressed
        registerButton.setOnClickListener(view -> {
            String email = String.valueOf(UsernameEditText.getText());
            String password1 = String.valueOf(PassWEditText.getText());
            String password2 = String.valueOf(PassWEditText2.getText());

            // simple form validation
            if(!formValidator.checkUEmail(email)){
                Toast toast = Toast.makeText(this, "Invalid email",Toast.LENGTH_LONG);
                toast.show();
            }
            else if(!formValidator.checkPassword(password1)){
                Toast toast = Toast.makeText(this, "Invalid password. 6 chars min",Toast.LENGTH_LONG);
                toast.show();
            }else if(!password1.equals(password2) || !formValidator.checkPassword(password2)){
                Toast toast = Toast.makeText(this, "Password mismatch",Toast.LENGTH_LONG);
                toast.show();
            }else {
                mAuth.createUserWithEmailAndPassword(email, password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(RegisterActivity.this, "Successfully Registered.",
                                            Toast.LENGTH_SHORT).show();

                                    // redirect user to login
                                    // start login activity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginButton.setOnClickListener(view -> {
            // start the register activity
            Intent intent =  new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

}