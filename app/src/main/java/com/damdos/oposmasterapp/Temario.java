package com.damdos.oposmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.damdos.oposmasterapp.model.FirebaseDatabaseHelper;
import com.damdos.oposmasterapp.model.RecyclerViewConfiguracion;
import com.damdos.oposmasterapp.model.Tema;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Temario extends AppCompatActivity {
    private Button boton_nuevo_contacto;
    private RecyclerView lista_contactos;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);
        this.setTitle("Temario");
        firebaseAuth = FirebaseAuth.getInstance();
        lista_contactos = findViewById(R.id.listaAgenda);


        /**
         * Carga los datos de los temas de la base de datos.
         */
        firebaseDatabaseHelper.listaTemas(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Tema> temaList, List<String> claves) {
                new RecyclerViewConfiguracion().setConfig(lista_contactos, Temario.this, temaList, claves);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }

            @Override
            public void NumeroTemasObtenido(int numeroTemas) {

            }
        });

        boton_nuevo_contacto = findViewById(R.id.boton_pruebas);
        boton_nuevo_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Temario.this, Prueba.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cronoMenu:
            case R.id.crono_menu:
                startActivity(new Intent(this, PomodoroActivity.class));
                return true;
            case R.id.calendario_menu:
            case R.id.calendarioMenu:
                startActivity(new Intent(this, Calendario.class));
                return true;
            case R.id.prueba:
            case R.id.pruebasMenu:
                startActivity(new Intent(this, Prueba.class));
                return true;
            case R.id.cerrar_sesion:
                firebaseAuth.signOut();
                invalidateOptionsMenu();
                RecyclerViewConfiguracion.cerrarSersion();

                startActivity(new Intent(this, MainActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}