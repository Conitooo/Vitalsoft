package com.pmdm.pruebalistview.acciones;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pmdm.pruebalistview.R;

public class DetallesMensaje extends AppCompatActivity {

    // Almacena el número de teléfono como String
    private String numeroContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_mensaje);

        TextView tvNumeroContacto = findViewById(R.id.tvNumeroContacto);
        EditText etMensaje = findViewById(R.id.etMensaje);
        Button btnEnviarWhatsApp = findViewById(R.id.btnEnviarWhatsApp);

        // Obtener el número de teléfono pasado en el Intent con la clave "phoneNumber"
        numeroContacto = getIntent().getStringExtra("phoneNumber");

        if (numeroContacto != null && !numeroContacto.trim().isEmpty()) {
            tvNumeroContacto.setText("Enviar mensaje al número: " + numeroContacto);
        } else {
            tvNumeroContacto.setText("No se proporcionó un número válido.");
        }

        btnEnviarWhatsApp.setOnClickListener(view -> {
            String mensaje = etMensaje.getText().toString().trim();
            if (numeroContacto != null && !numeroContacto.trim().isEmpty() && !mensaje.isEmpty()) {
                enviarMensajeWhatsApp(numeroContacto, mensaje);
            } else {
                Toast.makeText(this, "Ingrese un mensaje válido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Abre WhatsApp para enviar un mensaje al número especificado.
     * @param numero Número de teléfono en formato String.
     * @param mensaje El mensaje a enviar.
     */
    private void enviarMensajeWhatsApp(String numero, String mensaje) {
        try {
            // Se construye la URI para WhatsApp
            String uri = "https://wa.me/" + numero.replace("+", "").replace(" ", "")
                    + "?text=" + Uri.encode(mensaje);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.whatsapp");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
