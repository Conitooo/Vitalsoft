package com.pmdm.pruebalistview.acciones;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pmdm.pruebalistview.R;
import com.pmdm.pruebalistview.recognizer.VoiceAssistant;
import com.pmdm.pruebalistview.utilidades.Contactos;

public class DetallesVoz extends AppCompatActivity implements VoiceAssistant.VoiceCallback {
    private VoiceAssistant va;
    private TextView tvTextoReconocido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_acciones);

        va = new VoiceAssistant(this, this);

        Button btnIniciarVoz = findViewById(R.id.btnIniciarVoz);
        tvTextoReconocido = findViewById(R.id.tvTextoReconocido);
        ImageView ivMicrofono = findViewById(R.id.ivMicrofono);

        btnIniciarVoz.setOnClickListener(view -> {
            tvTextoReconocido.setText("Escuchando...");
            va.startListening();
        });
    }

    @Override
    public void onDecisionReceived(String accion, String contacto) {
        tvTextoReconocido.setText("Acción: " + accion + "\nContacto: " + contacto);

        // Se intenta obtener el número de teléfono usando el nombre del contacto
        int numeroInt = Contactos.obtenerNumeroPorNombre(this, contacto);
        if (numeroInt == -1) {
            Toast.makeText(this, "No se encontró el número para " + contacto, Toast.LENGTH_SHORT).show();
            return;
        }
        // Convertir el número obtenido (int) a String
        String phoneNumber = String.valueOf(numeroInt);

        if ("llamada de telefono".equalsIgnoreCase(accion)) {
            Intent intent = new Intent(this, DetallesLlamada.class);
            intent.putExtra("phoneNumber", phoneNumber);
            startActivity(intent);
        } else if ("mandar mensaje".equalsIgnoreCase(accion)) {
            Intent intent = new Intent(this, DetallesMensaje.class);
            intent.putExtra("phoneNumber", phoneNumber);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Acción no reconocida", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }
}
