package com.pmdm.pruebalistview.acciones;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pmdm.pruebalistview.R;

public class DetallesLlamada extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    // Almacena el número de teléfono como String
    private String numeroContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_llamada);

        TextView tvNumeroContacto = findViewById(R.id.tvNumeroContacto);
        Button btnLlamar = findViewById(R.id.btnLlamar);
        ImageView ivTelefono = findViewById(R.id.ivTelefono);

        // Recupera el número de teléfono pasado en el Intent con la clave "phoneNumber"
        numeroContacto = getIntent().getStringExtra("phoneNumber");

        if (numeroContacto != null && !numeroContacto.isEmpty()) {
            tvNumeroContacto.setText(numeroContacto);
        } else {
            tvNumeroContacto.setText("No se encontró el número");
        }

        btnLlamar.setOnClickListener(view -> {
            if (numeroContacto != null && !numeroContacto.isEmpty()) {
                llamar(numeroContacto);
            } else {
                Toast.makeText(this, "No hay un número válido para llamar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Inicia la llamada si se cuenta con el permiso CALL_PHONE.
     * @param numero El número de teléfono en formato String.
     */
    private void llamar(String numero) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + numero));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                llamar(numeroContacto);
            } else {
                Toast.makeText(this, "Permiso denegado para llamadas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
