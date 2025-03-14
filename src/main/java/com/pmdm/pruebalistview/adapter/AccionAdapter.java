package com.pmdm.pruebalistview.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmdm.pruebalistview.acciones.ContactList;
import com.pmdm.pruebalistview.acciones.ContactList;
import com.pmdm.pruebalistview.acciones.DetallesVoz;
import com.pmdm.pruebalistview.R;

import java.util.List;

public class AccionAdapter extends RecyclerView.Adapter<AccionAdapter.ViewHolder> {

    private List<AccionPrincipal> listaAcciones;

    public AccionAdapter(List<AccionPrincipal> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    @NonNull
    @Override
    public AccionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout "accion_layout.xml"
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.accion_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccionAdapter.ViewHolder holder, int position) {
        // Obtenemos el elemento según la posición
        AccionPrincipal accion = listaAcciones.get(position);

        // Asignamos la imagen, el nombre y la descripción
        holder.aFoto.setImageDrawable(accion.getSrc());
        holder.aNombre.setText(accion.getNombre());
        holder.aDesc.setText(accion.getDescripcion());

        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent;

            // Si se selecciona "Llamada de telefono" o "Mandar mensaje" se redirige a la lista de contactos
            if ("Llamada de telefono".equals(accion.getNombre()) || "Mandar mensaje".equals(accion.getNombre())) {
                intent = new Intent(context, ContactList.class);
                // Se pasa un extra para diferenciar entre llamada y mensaje
                if ("Llamada de telefono".equals(accion.getNombre())) {
                    intent.putExtra("action_type", "call");
                } else {
                    intent.putExtra("action_type", "message");
                }
            } else if ("Usar voz".equals(accion.getNombre())) {
                intent = new Intent(context, DetallesVoz.class);
            } else {
                // Por defecto, redirige a ContactListActivity sin extra adicional
                intent = new Intent(context, ContactList.class);
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (listaAcciones != null) ? listaAcciones.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView aFoto;
        TextView aNombre;
        TextView aDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            aFoto   = itemView.findViewById(R.id.aFoto);
            aNombre = itemView.findViewById(R.id.aNombre);
            aDesc   = itemView.findViewById(R.id.aDesc);
        }
    }
}
