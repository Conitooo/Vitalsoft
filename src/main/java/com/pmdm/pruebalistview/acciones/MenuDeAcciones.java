package com.pmdm.pruebalistview.acciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.appbar.MaterialToolbar;
import com.pmdm.pruebalistview.R;
import com.pmdm.pruebalistview.adapter.AccionAdapter;
import com.pmdm.pruebalistview.adapter.AccionPrincipal;
import com.pmdm.pruebalistview.logins.Login;

import java.util.ArrayList;
import java.util.List;

public class MenuDeAcciones extends AppCompatActivity {

    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_de_acciones);


        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);


        RecyclerView recyclerView = findViewById(R.id.menuAcciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<AccionPrincipal> acciones = new ArrayList<>();
        acciones.add(new AccionPrincipal("Llamada de telefono", "Llama a un contacto o a un número de teléfono", ContextCompat.getDrawable(this, R.drawable.llamada)));
        acciones.add(new AccionPrincipal("Mandar mensaje", "Manda un mensaje a un contacto", ContextCompat.getDrawable(this, R.drawable.message)));
        acciones.add(new AccionPrincipal("Usar voz", "Usa nuestra IA para seleccionar tu acción", ContextCompat.getDrawable(this, R.drawable.microphone)));

        AccionAdapter ad = new AccionAdapter(acciones);
        recyclerView.setAdapter(ad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MenuDeAcciones.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
