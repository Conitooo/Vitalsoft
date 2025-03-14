package com.pmdm.pruebalistview.logins;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pmdm.pruebalistview.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Renombramos usernameEditText a emailEditText
    private EditText emailEditText, passwordEditText, confirmPassEditText;
    private CheckBox tycCheckBox;
    private Button signButton, backLoginButton;

    // Instancias de Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        // Referencias a los elementos de la interfaz
        // Puedes dejar el id "username" en el layout o cambiarlo a "email"
        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPassEditText = findViewById(R.id.confirmPass);
        tycCheckBox = findViewById(R.id.TyCCheck);
        signButton = findViewById(R.id.sign);
        backLoginButton = findViewById(R.id.backLogin);

        // Inicializamos FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        // DESHABILITA la verificación de app (reCAPTCHA) para pruebas; ¡solo para testing!
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        db = FirebaseFirestore.getInstance();

        // Acción al pulsar el botón de registro
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        // Botón para volver al login (finaliza esta actividad)
        backLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registrarUsuario() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPass = confirmPassEditText.getText().toString();

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
        if (confirmPass.isEmpty()) {
            confirmPassEditText.setError("Confirme su contraseña");
            confirmPassEditText.requestFocus();
            return;
        }
        if (!password.equals(confirmPass)) {
            confirmPassEditText.setError("Las contraseñas no coinciden");
            confirmPassEditText.requestFocus();
            return;
        }
        if (!tycCheckBox.isChecked()) {
            Toast.makeText(MainActivity.this, "Debe aceptar los Términos y Condiciones", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso: guardar datos adicionales en Firestore
                            String userId = mAuth.getCurrentUser().getUid();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            // Agrega más campos según necesites

                            db.collection("users").document(userId)
                                    .set(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                                // Redirigir a otra actividad si se desea
                                            } else {
                                                Toast.makeText(MainActivity.this, "Error al guardar datos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity.this, "Registro fallido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
