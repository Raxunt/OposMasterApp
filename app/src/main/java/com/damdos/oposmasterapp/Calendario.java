package com.damdos.oposmasterapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.damdos.oposmasterapp.model.Nota;
import com.damdos.oposmasterapp.model.RecyclerViewConfiguracion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class Calendario extends AppCompatActivity {
    private List<Nota> listaNotas = new ArrayList<>();
    private HashMap<Date, String> notasPorFecha = new HashMap<>();
    Button botonGuardar;
    Button botonMostrarNotas;
    EditText editTextNota;
    TextView textViewFechaSeleccionada;
    private Date fechaSeleccionada;
    private DatabaseReference notasRef;
    private Calendar calendar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        this.setTitle("Calendario");
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        botonGuardar = findViewById(R.id.boton_guardar_nota);
        botonMostrarNotas = findViewById(R.id.boton_mostrar_notas);
        editTextNota = findViewById(R.id.texto_anotacion_cal);
        textViewFechaSeleccionada = findViewById(R.id.texto_fecha_seleccionada);
        calendar = Calendar.getInstance();

        String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);

        notasRef = userRef.child("notas");
        cargarNotasDesdeFirebase();

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fechaSeleccionada != null) {
                    String contenidoNota = editTextNota.getText().toString().trim();
                    if (!contenidoNota.isEmpty()) {
                        Nota nota = new Nota(contenidoNota, fechaSeleccionada);
                        String notaId = notasRef.push().getKey();

                        notasRef.child(notaId).setValue(nota)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        editTextNota.setText("");

                                        LayoutInflater inflater = getLayoutInflater();
                                        View layout = inflater.inflate(R.layout.toast_custom, findViewById(R.id.toast_layout));

                                        TextView textoToast = layout.findViewById(R.id.texto_toast);
                                        textoToast.setText("Nota guardada exitosamente a día: " + textViewFechaSeleccionada.getText().toString());

                                        Toast toast = new Toast(getApplicationContext());
                                        toast.setDuration(Toast.LENGTH_SHORT);
                                        toast.setView(layout);
                                        toast.show();

                                        cargarNotasDesdeFirebase();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Calendario.this, "Error al guardar la nota", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Calendario.this, "Por favor, ingresa el contenido de la nota", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Calendario.this, "Por favor, selecciona una fecha", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonMostrarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarNotas();
            }
        });

        Button botonSeleccionarFecha = findViewById(R.id.boton_fecha);
        botonSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });
    }

    private void cargarNotasDesdeFirebase() {
        notasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaNotas.clear();
                notasPorFecha.clear();

                for (DataSnapshot notaSnapshot : dataSnapshot.getChildren()) {
                    Nota nota = notaSnapshot.getValue(Nota.class);
                    listaNotas.add(nota);
                    notasPorFecha.put(nota.getFecha(), nota.getContenido());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Calendario.this, "Error al cargar las notas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mostrarDatePicker() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fechaSeleccionada = calendar.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaSeleccionadaTexto = sdf.format(fechaSeleccionada);

                Toast.makeText(Calendario.this, "Fecha seleccionada: " + fechaSeleccionadaTexto, Toast.LENGTH_SHORT).show();

                textViewFechaSeleccionada.setText(fechaSeleccionadaTexto);
            }
        };

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener, year, month, day);

        datePickerDialog.show();
    }

    public void mostrarNotas() {
        StringBuilder notasTexto = new StringBuilder();

        for (Nota nota : listaNotas) {
            String contenidoNota = nota.getContenido();
            Date fechaNota = nota.getFecha();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaNotaTexto = sdf.format(fechaNota);

            notasTexto.append("Fecha: ").append(fechaNotaTexto).append("\n");
            notasTexto.append("Contenido: ").append(contenidoNota).append("\n\n");
        }

        if (notasTexto.length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notas");
            builder.setMessage(notasTexto.toString());
            builder.setPositiveButton("OK", null);
            builder.show();
        } else {
            Toast.makeText(this, "No hay notas para mostrar", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}








