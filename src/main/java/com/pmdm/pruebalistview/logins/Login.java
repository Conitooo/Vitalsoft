package com.pmdm.pruebalistview.logins;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.pmdm.pruebalistview.R;
import com.pmdm.pruebalistview.acciones.MenuDeAcciones;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Verifica si ya existe una sesión activa
        if (mAuth.getCurrentUser() != null) {
            // Usuario ya autenticado, redirige a MenuDeAcciones
            startActivity(new Intent(Login.this, MenuDeAcciones.class));
            finish();
            return;
        }

        // Carga el layout según la orientación del dispositivo
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.login); // Asegúrate que el layout se llame "login.xml"
        } else {
            setContentView(R.layout.login_landscape); // O "login_landscape.xml" para horizontal
        }

        // Referencias a los elementos del layout (ids definidos en el XML)
        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signButton = findViewById(R.id.signButton);

        // Acción para el botón de login: validar credenciales y redirigir si es exitoso
        loginButton.setOnClickListener(view -> loginUser());

        // Acción para el botón de registro: redirige a la actividad de registro (ajusta según tu proyecto)
        signButton.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, com.pmdm.pruebalistview.logins.MainActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validaciones básicas
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Ingrese un correo electrónico válido");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Ingrese una contraseña");
            passwordEditText.requestFocus();
            return;
        }

        // Intento de inicio de sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        // Redirige a MenuDeAcciones tras un login exitoso
                        startActivity(new Intent(Login.this, MenuDeAcciones.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
