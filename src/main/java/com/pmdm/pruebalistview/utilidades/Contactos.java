package com.pmdm.pruebalistview.utilidades;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class Contactos {

    /**
     * Devuelve el número de teléfono como un int, dado el nombre de un contacto.
     * Si el número es muy grande o no se puede convertir a int, devolverá -1.
     * @param context El contexto para acceder al ContentResolver.
     * @param nombreContacto El nombre del contacto que se quiere buscar.
     * @return El número de teléfono en formato int o -1 si no se puede convertir.
     */
    public static int obtenerNumeroPorNombre(Context context, String nombreContacto) {
        // Validar el nombre
        if (nombreContacto == null || nombreContacto.trim().isEmpty()) {
            return -1;
        }

        // Construir la consulta
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {nombreContacto};

        // Ejecutar la consulta
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

        int numeroInt = -1;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Obtener el número como String
                String numeroStr = cursor.getString(0);

                // Eliminar caracteres que no sean dígitos (espacios, guiones, paréntesis, etc.)
                numeroStr = numeroStr.replaceAll("\\D+", "");

                try {
                    // Intentar convertir a int
                    numeroInt = Integer.parseInt(numeroStr);
                } catch (NumberFormatException e) {
                    // Si no se puede convertir, devolvemos -1
                    numeroInt = -1;
                }
            }
            cursor.close();
        }

        return numeroInt;
    }

    /**
     * Lista todos los contactos del teléfono y los devuelve en un ArrayList de Contacto.
     * Es necesario que la app tenga el permiso READ_CONTACTS para acceder a la agenda.
     *
     * @param context El contexto para acceder al ContentResolver.
     * @return ArrayList de Contacto con todos los contactos encontrados.
     */
    public static ArrayList<Contacto> listarContactos(Context context) {
        ArrayList<Contacto> listaContactos = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // Ordenamos alfabéticamente por nombre
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                String nombre = cursor.getString(nameIndex);
                String numero = cursor.getString(numberIndex);
                // Creamos un nuevo objeto Contacto y lo añadimos a la lista
                Contacto contacto = new Contacto(nombre, numero);
                listaContactos.add(contacto);
            }
            cursor.close();
        }

        return listaContactos;
    }
}
