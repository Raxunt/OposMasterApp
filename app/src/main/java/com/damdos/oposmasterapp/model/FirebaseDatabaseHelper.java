package com.damdos.oposmasterapp.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirebaseDatabaseHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Tema> listaTemas = new ArrayList<>();
    FirebaseAuth firebaseAuth;


    public interface DataStatus{
        void DataIsLoaded(List<Tema> temaList, List<String> claves);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
        void NumeroTemasObtenido(int numeroTemas);
    }

    /**
     * Método para asignar el árbol a la hora de insertar los datos en la BD.
     */
    public FirebaseDatabaseHelper() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        databaseReference = firebaseDatabase.getInstance().getReference().child("usuarios").child(uid).child("temas");
    }

    /**
     *  Recupera la lista de contactos y los muestra en el RecyclerView.
     * @param dataStatus interfaz para añadir, modificar y eliminar datos.
     */
    public void listaTemas(final DataStatus dataStatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTemas.clear();
                List<String> claves = new ArrayList<>();
                for(DataSnapshot nodoClave : snapshot.getChildren()){
                   claves.add( nodoClave.getKey());
                   Tema tema = nodoClave.getValue(Tema.class);
                   listaTemas.add(tema);
                }
                dataStatus.DataIsLoaded(listaTemas, claves);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Método que nos almacena en la BD temas nuevos.
     * @param temas Recoge temas nuevos con todos sus atributos.
     * @param dataStatus interfaz para añadir, modificar y eliminar datos.
     */

    public void agregarTemas(List<Tema> temas, final DataStatus dataStatus){
        for (Tema tema : temas) {
            String clave = databaseReference.push().getKey();
            databaseReference.child(clave).setValue(tema);
        }
        dataStatus.DataIsInserted();
    }

    public void agregarTema(Tema tema, final DataStatus dataStatus){
        String clave = databaseReference.push().getKey();
        databaseReference.child(clave).setValue(tema).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });
    }


    /**
     * Actualiza un tema ya creado.
     * @param clave recoge el identificador del usuario existente por medio de un string.
     * @param tema recoge la clase usuario.
     * @param dataStatus interfaz para añadir, modificar y eliminar datos.
     */
    public void modificarTema(String clave, Tema tema, final DataStatus dataStatus){
        databaseReference.child(clave).setValue(tema).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });

    }

    /**
     *  Borra a un tema de la base de datos.
     * @param clave recoge el identificador del usuario existente por medio de un string.
     * @param dataStatus interfaz para añadir, modificar y eliminar datos.
     */
    public void borrarTema(String clave, final DataStatus dataStatus){
        databaseReference.child(clave).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsDeleted();
            }
        });
    }

    public void obtenerNumeroTemas(final DataStatus dataStatus) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numeroTemas = (int) snapshot.getChildrenCount();
                dataStatus.NumeroTemasObtenido(numeroTemas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
