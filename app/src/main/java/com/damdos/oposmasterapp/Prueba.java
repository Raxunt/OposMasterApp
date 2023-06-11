package com.damdos.oposmasterapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.damdos.oposmasterapp.model.FirebaseDatabaseHelper;
import com.damdos.oposmasterapp.model.RecyclerViewConfiguracion;
import com.damdos.oposmasterapp.model.Tema;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Prueba extends AppCompatActivity {
    private Chronometer cronometro;
    private Button botonEmpezar, botonPausar, botonReiniciar, botonTemsTotales, botonTemsEst, botonSalir;
    private SeekBar seekBarTemEstudiados, seekBarBolas;
    private TextView numTemas, temEstudiados, numBolas, porcentajeTem;
    private boolean enEjecucion;
    private long tiempoPausado;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        this.setTitle("Pruebas");
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cronometro = findViewById(R.id.chronometer);
        botonEmpezar = findViewById(R.id.boton_empezar);
        botonPausar = findViewById(R.id.boton_pausar);
        botonReiniciar = findViewById(R.id.boton_reiniciar);
        numTemas = findViewById(R.id.num_temas);
        seekBarTemEstudiados = findViewById(R.id.seekBar_temas_estudiados);
        seekBarBolas = findViewById(R.id.seekBar_bolas);
        temEstudiados = findViewById(R.id.tem_estudiados);
        numBolas = findViewById(R.id.num_bolas);
        porcentajeTem = findViewById(R.id.porcentaje);
        botonTemsTotales = findViewById(R.id.boton_tem_totl);
        botonTemsEst = findViewById(R.id.boton_tem_est);
        botonSalir = findViewById(R.id.boton_salir);

        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();

        firebaseDatabaseHelper.obtenerNumeroTemas(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Tema> temaList, List<String> claves) {

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
                numTemas.setText(String.valueOf(numeroTemas));
                seekBarTemEstudiados.setMax(numeroTemas);
            }

        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarBolas.setMin(1);
        }
        seekBarBolas.setMax(10);
        numBolas.setText(String.valueOf(1));
        seekBarBolas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numBolas.setText(String.valueOf(progress));
                double porcentajeAcierto = calcularPorcentajeAcierto(Integer.parseInt(String.valueOf(numTemas.getText())), progress, Integer.parseInt(String.valueOf(temEstudiados.getText())));
                String porcentajeFormateado = String.format("%.2f%%", porcentajeAcierto);
                porcentajeTem.setText(porcentajeFormateado);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTemEstudiados.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temEstudiados.setText(String.valueOf(progress));
                double porcentajeAcierto = calcularPorcentajeAcierto(Integer.parseInt(String.valueOf(numTemas.getText())), Integer.parseInt(String.valueOf(numBolas.getText())), progress);
                String porcentajeFormateado = String.format("%.2f%%", porcentajeAcierto);
                porcentajeTem.setText(porcentajeFormateado);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        botonEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCronometro();
            }
        });

        botonPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausarCronometro();
            }
        });

        botonReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarCronometro();
            }
        });
        botonTemsTotales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabaseHelper.listaTemas(new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Tema> temaList, List<String> claves) {
                        mostrarTemasAleatorios(temaList, Integer.parseInt(String.valueOf(numBolas.getText())));
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
            }


        });

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        botonTemsEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabaseHelper.listaTemas(new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Tema> temaList, List<String> claves) {
                        mostrarTemasDistintosPendiente(temaList, Integer.parseInt(String.valueOf(numBolas.getText())));
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
            }
        });






    }

    private void iniciarCronometro() {
        if (!enEjecucion) {
            cronometro.setBase(SystemClock.elapsedRealtime() - tiempoPausado);
            cronometro.start();
            enEjecucion = true;
        }
    }

    private void pausarCronometro() {
        if (enEjecucion) {
            cronometro.stop();
            tiempoPausado = SystemClock.elapsedRealtime() - cronometro.getBase();
            enEjecucion = false;
        }
    }

    private void reiniciarCronometro() {
        cronometro.setBase(SystemClock.elapsedRealtime());
        tiempoPausado = 0;
    }

    public double calcularPorcentajeAcierto(int temasTotales, int temasEstudiados, int numeroBolas) {
        double proporcionEstudiados = (double) temasEstudiados / temasTotales;
        double probabilidad = 1.0 - Math.exp(numeroBolas * Math.log(1 - proporcionEstudiados));
        double porcentajeAcierto = probabilidad * 100.0;

        return porcentajeAcierto;
    }


    private void mostrarTemasAleatorios(List<Tema> temaList, int cantidad) {
        List<Tema> temasAleatorios = obtenerTemasAleatorios(temaList, cantidad);

        TemasDialogFragment dialogFragment = new TemasDialogFragment(temasAleatorios);
        dialogFragment.show(getSupportFragmentManager(), "TemasDialogFragment");
    }

    private List<Tema> obtenerTemasAleatorios(List<Tema> temaList, int cantidad) {
        List<Tema> temasAleatorios = new ArrayList<>();

        List<Integer> indicesAleatorios = generarIndicesAleatorios(temaList.size(), cantidad);

        for (int indice : indicesAleatorios) {
            temasAleatorios.add(temaList.get(indice));
        }

        return temasAleatorios;
    }

    private List<Integer> generarIndicesAleatorios(int maximo, int cantidad) {
        List<Integer> indicesAleatorios = new ArrayList<>();

        Random random = new Random();
        while (indicesAleatorios.size() < cantidad) {
            int indice = random.nextInt(maximo);
            if (!indicesAleatorios.contains(indice)) {
                indicesAleatorios.add(indice);
            }
        }

        return indicesAleatorios;
    }

    private void mostrarTemasDistintosPendiente(List<Tema> temaList, int cantidad) {
        List<Tema> temasDistintosPendiente = filtrarTemasDistintosPendiente(temaList);

        if (temasDistintosPendiente.size() <= cantidad) {
            mostrarDialogoTemas(temasDistintosPendiente);
        } else {
            Random random = new Random();
            Set<Integer> indicesAleatorios = new HashSet<>();

            while (indicesAleatorios.size() < cantidad) {
                int indiceAleatorio = random.nextInt(temasDistintosPendiente.size());
                indicesAleatorios.add(indiceAleatorio);
            }

            List<Tema> temasAleatorios = new ArrayList<>();
            for (int indice : indicesAleatorios) {
                temasAleatorios.add(temasDistintosPendiente.get(indice));
            }

            mostrarDialogoTemas(temasAleatorios);
        }
    }

    private void mostrarDialogoTemas(List<Tema> temas) {
        TemasDialogFragment dialogFragment = new TemasDialogFragment(temas);
        dialogFragment.show(getSupportFragmentManager(), "TemasDialogFragment");
    }


    private List<Tema> filtrarTemasDistintosPendiente(List<Tema> temaList) {
        List<Tema> temasDistintosPendiente = new ArrayList<>();

        for (Tema tema : temaList) {
            if (!tema.getEvaluacion().equals("Pendiente")) {
                temasDistintosPendiente.add(tema);
            }
        }

        return temasDistintosPendiente;
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


}
