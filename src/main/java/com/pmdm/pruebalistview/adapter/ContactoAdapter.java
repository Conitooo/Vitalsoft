package com.pmdm.pruebalistview.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pmdm.pruebalistview.R;
import com.pmdm.pruebalistview.utilidades.Contacto;

import java.util.List;

public class ContactoAdapter extends BaseAdapter {

    private Context context;
    private List<Contacto> contactos;

    public ContactoAdapter(Context context, List<Contacto> contactos) {
        this.context = context;
        this.contactos = contactos;
    }

    @Override
    public int getCount() {
        return contactos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; // O si tienes un id único en Contacto, retornarlo.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutiliza la vista si ya existe
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.card_contacto, parent, false);
        }

        // Obtén las referencias a los TextView del layout
        TextView tvContactName = convertView.findViewById(R.id.tvContactName);
        TextView tvContactNumber = convertView.findViewById(R.id.tvContactNumber);

        // Obtén el objeto Contacto correspondiente a la posición
        Contacto contacto = contactos.get(position);

        // Asigna los valores del contacto a los TextView
        tvContactName.setText(contacto.getNombre());
        tvContactNumber.setText(contacto.getNumero());

        return convertView;
    }
}
