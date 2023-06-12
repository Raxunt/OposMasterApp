package com.damdos.oposmasterapp;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.damdos.oposmasterapp.model.FirebaseDatabaseHelper;
import com.damdos.oposmasterapp.model.Tema;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ModificarTema extends AppCompatActivity {
     Button boton_modificar, boton_borrar, boton_cancelar, boton_sumar, boton_restar;
     EditText nota;
     TextView nombre, numTema, numRepasos;
     Spinner evaluacion;
    private FirebaseAuth firebaseAuth;
    private String clave;
    private String nom;
    private String numTem;
    private String numRep;
    private String eval;
    private String anotacion;
    private int repasos = 0;
    Tema tema = new Tema();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tema);
        this.setTitle("Modificar Tema");
        firebaseAuth = FirebaseAuth.getInstance();
        clave = getIntent().getStringExtra("clave");
        numTem = getIntent().getStringExtra("numero");
        nom = getIntent().getStringExtra("nombre");
        numRep = getIntent().getStringExtra("repasos");
        eval = getIntent().getStringExtra("evaluacion");
        anotacion = getIntent().getStringExtra("nota");

        numTema = findViewById(R.id.nuemero_tema);
        numTema.setText("Tema: " + numTem);
        nombre = findViewById(R.id.titulo_tema);
        nombre.setMovementMethod(new ScrollingMovementMethod());
        nombre.setText(nom);
        numRepasos = findViewById(R.id.contador_repasos);
        numRepasos.setText("Repasos: " + numRep);
        evaluacion = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.evaluacion_opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evaluacion.setAdapter(adapter);
        //TODO
        int position = adapter.getPosition(eval);
        evaluacion.setSelection(position);

        nota = findViewById(R.id.texto_anotacion_cal);
        nota.setText(anotacion);
        boton_modificar = findViewById(R.id.boton_guardar_tema);
        boton_borrar = findViewById(R.id.boton_borrar_tema);
        boton_cancelar = findViewById(R.id.boton_cancelar_tema);
        boton_sumar = findViewById(R.id.boton_mas);
        boton_restar = findViewById(R.id.boton_menos);
        repasos = Integer.parseInt(numRep);
        boton_sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repasos++;
                tema.setRepasos(repasos);
                numRepasos.setText("Repasos: " + String.valueOf(repasos));

            }
        });

        boton_restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repasos > 0) {
                    repasos--;
                    tema.setRepasos(repasos);
                    numRepasos.setText("Repasos: " + String.valueOf(repasos));
                }

            }
        });

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tema.setNumero(Integer.parseInt(numTem));
                tema.setNombre(nombre.getText().toString());
                tema.setRepasos(repasos);
                tema.setEvaluacion(evaluacion.getSelectedItem().toString());
                tema.setNota(nota.getText().toString());

                {
                    new FirebaseDatabaseHelper().modificarTema(clave, tema, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Tema> temaList, List<String> claves) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {


                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_custom, findViewById(R.id.toast_layout));

                            TextView textoToast = layout.findViewById(R.id.texto_toast);
                            textoToast.setText("Modificación exitosa del tema: " + Integer.parseInt(numTem));

                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();



                        }

                        @Override
                        public void DataIsDeleted() {

                        }

                        @Override
                        public void NumeroTemasObtenido(int numeroTemas) {

                        }

                    });
                }finish();
            }

        });

        boton_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tema.setNumero(Integer.parseInt(numTem));
                tema.setNombre(nombre.getText().toString());
                tema.setRepasos(0);
                tema.setEvaluacion("Pendiente");
                tema.setNota("");

                {
                    new FirebaseDatabaseHelper().modificarTema(clave, tema, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Tema> temaList, List<String> claves) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            Toast.makeText(ModificarTema.this, "Reseteo de datos exitoso", Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void DataIsDeleted() {

                        }

                        @Override
                        public void NumeroTemasObtenido(int numeroTemas) {

                        }
                    });
                }
                finish(); return;
            }
        });
        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); return;
            }
        });


    }


}