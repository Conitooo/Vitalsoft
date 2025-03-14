package com.pmdm.pruebalistview.acciones;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pmdm.pruebalistview.R;
import com.pmdm.pruebalistview.adapter.ContactoAdapter;
import com.pmdm.pruebalistview.utilidades.Contacto;
import com.pmdm.pruebalistview.utilidades.Contactos;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ListView lvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_list);

        lvContacts = findViewById(R.id.lvContacts);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {

            cargarContactos();
        }
    }

    private void cargarContactos() {
        ArrayList<Contacto> listaContactos = Contactos.listarContactos(this);
        ContactoAdapter adapter = new ContactoAdapter(this, listaContactos);
        lvContacts.setAdapter(adapter);

        String actionType = getIntent().getStringExtra("action_type");

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Contacto selected = listaContactos.get(position);
                Intent intent;
                if ("call".equals(actionType)) {
                    intent = new Intent(ContactList.this, com.pmdm.pruebalistview.acciones.DetallesLlamada.class);
                    intent.putExtra("phoneNumber", selected.getNumero());
                    startActivity(intent);
                } else if ("message".equals(actionType)) {
                    intent = new Intent(ContactList.this, com.pmdm.pruebalistview.acciones.DetallesMensaje.class);
                    intent.putExtra("phoneNumber", selected.getNumero());
                    startActivity(intent);
                } else {
                    Toast.makeText(ContactList.this, "Acción no definida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cargarContactos();
            } else {
                Toast.makeText(this, "El permiso para leer contactos es necesario.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
