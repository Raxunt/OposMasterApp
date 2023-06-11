package com.damdos.oposmasterapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.damdos.oposmasterapp.model.Cuerpo;
import com.damdos.oposmasterapp.model.Especialidad;
import com.damdos.oposmasterapp.model.Tema;
import com.damdos.oposmasterapp.model.Validaciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Switch aSwitch;
    boolean modoNoche;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button boton_entrar, boton_nuevo_usu;
    private EditText correo, password;
    private FirebaseAuth firebaseAuth;
    private ImageView img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        cargarDatos();
         */


        /**
         * Código para realizar el cambio de Preferencias determinando que modo de colores deseamos entre claro/oscuro
         */
        getSupportActionBar().hide();

        aSwitch = findViewById(R.id.cambio_estilo);
        sharedPreferences = getSharedPreferences( "MODE", Context.MODE_PRIVATE);
        modoNoche = sharedPreferences.getBoolean("night", false);

        if(modoNoche){
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modoNoche){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);

                }

                editor.apply();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        correo=findViewById(R.id.correo);
        password=findViewById(R.id.password);
        /**
         * Boton para acceder a la pantalla de nuevo usuario
         */
        boton_nuevo_usu = findViewById(R.id.boton_nuevaCuenta);
        boton_nuevo_usu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
                Toast.makeText(MainActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Botón para acceder a la agenda tras validar las credenciales correctas almacenadas en la base de datos.
         */
        boton_entrar = findViewById(R.id.boton_login);
        boton_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correoUsuario = correo.getText().toString().trim();
                String passwordUsuario = password.getText().toString().trim();
                if(correoUsuario.isEmpty() && passwordUsuario.isEmpty()){
                    Toast.makeText(MainActivity.this, "¡Datos introducidos incorrectos!", Toast.LENGTH_SHORT).show();
                }else{
                    if (!Validaciones.validarCorreo(correoUsuario)){
                        correo.setError("Correo no válido");
                    }else if (!Validaciones.validarPassword(passwordUsuario)){
                        password.setError("Contraseña no válida (valores permitidos [0-9A-Za-z_] con rango de {3,15})");
                    }else{
                        loginUsuario(correoUsuario, passwordUsuario);
                    }

                }
            }
        });
    }

    /**
     * Función que nos permite comparar las credenciales que se introducen y contrastarlas contra la base de datos. Si son correctas
     * se pasará al menú de agenda arrojándonos un toast con un mensaje de exito al loguear, por lo contrario nos arrojará
     * un toast con el texto de credenciales incorrectas volviendo a introducir los datos o creando una cuenta nueva como ultima opción.
     * @param correoUsuario de tipo String introducido por el usuario validandola en la base de datos.
     * @param passwordUsuario de tipo String introducido por el usuario validandola en la base de datos.
     */
    private void loginUsuario(String correoUsuario, String passwordUsuario) {
        firebaseAuth.signInWithEmailAndPassword(correoUsuario, passwordUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(MainActivity.this, Temario.class));
                    Toast.makeText(MainActivity.this, "Credenciales correctas", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error al buscar al usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Nos impide retroceder estando en el menú de inicio de sesión.
     */
    @Override
    public void onBackPressed() {
    }

public void cargarDatos(){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference cuerposRef = database.getReference("cuerpos");

    List<Tema> temas_informatica = new ArrayList<>();
    temas_informatica.add(new Tema( 1,"Representación y comunicación de la información."));
    temas_informatica.add(new Tema( 2, "Elementos funcionales de un ordenador digital."));
    temas_informatica.add(new Tema( 3,"Componentes, estructura y funcionamiento de la Unidad Central de Proceso."));
    temas_informatica.add(new Tema( 4,"Memoria interna. Tipos. Direccionamiento. Características y funciones."));
    temas_informatica.add(new Tema( 5,"Microprocesadores. Estructura. Tipos. Comunicación con el exterior."));
    temas_informatica.add(new Tema( 6,"Sistemas de almacenamiento externo. Tipos. Características y funcionamiento."));
    temas_informatica.add(new Tema( 7,"Dispositivos periféricos de entrada/salida. Características y funcionamiento."));
    temas_informatica.add(new Tema( 8,"Hardware comercial de un ordenador. Placa base. Tarjetas controladoras de dispositivos y de entrada/salida."));
    temas_informatica.add(new Tema( 9,"Lógica de circuitos. Circuitos combinacionales y secuenciales."));
    temas_informatica.add(new Tema( 10,"Representación interna de los datos."));
    temas_informatica.add(new Tema( 11,"Organización lógica de los datos. Estructuras estáticas."));
    temas_informatica.add(new Tema( 12,"Organización lógica de los datos. Estructuras dinámicas."));
    temas_informatica.add(new Tema( 13,"Ficheros. Tipos. Características. Organizaciones."));
    temas_informatica.add(new Tema( 14,"Utilización de ficheros según su organización."));
    temas_informatica.add(new Tema( 15,"Sistemas operativos. Componentes. Estructura. Funciones. Tipos."));
    temas_informatica.add(new Tema( 16,"Sistemas operativos: Gestión de procesos."));
    temas_informatica.add(new Tema( 17,"Sistemas operativos: Gestión de memoria."));
    temas_informatica.add(new Tema( 18,"Sistemas operativos: Gestión de entradas/salidas."));
    temas_informatica.add(new Tema( 19,"Sistemas operativos: Gestión de archivos y dispositivos."));
    temas_informatica.add(new Tema( 20,"Explotación y Administración de sistemas operativos monousuario y multiusuario."));
    temas_informatica.add(new Tema( 21,"Sistemas informáticos. Estructura física y funcional."));
    temas_informatica.add(new Tema( 22,"Planificación y explotación de sistemas informáticos. Configuración. Condiciones de instalación. Medidas de seguridad. Procedimientos de uso."));
    temas_informatica.add(new Tema( 23,"Diseño de algoritmos. Técnicas descriptivas."));
    temas_informatica.add(new Tema( 24,"Lenguajes de programación. Tipos. Características."));
    temas_informatica.add(new Tema( 25,"Programación estructurada. Estructuras básicas. Funciones y Procedimientos."));
    temas_informatica.add(new Tema( 26,"Programación modular. Diseño de funciones. Recursividad. Librerías."));
    temas_informatica.add(new Tema( 27,"Programación orientada a objetos. Objetos. Clases. Herencia. Polimorfismo. Lenguajes."));
    temas_informatica.add(new Tema( 28,"Programación en tiempo real. Interrupciones. Sincronización y comunicación entre tareas. Lenguajes."));
    temas_informatica.add(new Tema( 29,"Utilidades para el desarrollo y prueba de programas. Compiladores. Interpretes. Depuradores."));
    temas_informatica.add(new Tema( 30,"Prueba y documentación de programas. Técnicas."));
    temas_informatica.add(new Tema( 31,"Lenguaje C: Características generales. Elementos del lenguaje. Estructura de un programa. Funciones de librería y usuario. Entorno de compilación. Herramientas para la elaboración y depuración de programas en lenguaje C."));
    temas_informatica.add(new Tema( 32,"Lenguaje C: Manipulación de estructuras de datos dinámicas y estáticas. Entrada y salida de datos. Gestión de punteros. Punteros a funciones."));
    temas_informatica.add(new Tema( 33,"Programación en lenguaje ensamblador. Instrucciones básicas. Formatos. Direccionamientos."));
    temas_informatica.add(new Tema( 34,"Sistemas gestores de base de datos. Funciones. Componentes. Arquitecturas de referencia y operacionales. Tipos de sistemas."));
    temas_informatica.add(new Tema( 35,"La definición de datos. Niveles de descripción. Lenguajes. Diccionario de datos."));
    temas_informatica.add(new Tema( 36,"La manipulación de datos. Operaciones. Lenguajes. Optimización de consultas."));
    temas_informatica.add(new Tema( 37,"Modelo de datos jerárquico y en red. Estructuras. Operaciones."));
    temas_informatica.add(new Tema( 38,"Modelo de datos relacional. Estructuras. Operaciones. Álgebra relacional."));
    temas_informatica.add(new Tema( 39,"Lenguajes para la definición y manipulación de datos en sistemas de base de datos relacionales. Tipos. Características. Lenguaje SQL."));
    temas_informatica.add(new Tema( 40,"Diseño de bases de datos relacionales."));
    temas_informatica.add(new Tema( 41,"Utilidades de los sistemas gestores de base de datos para el desarrollo de aplicaciones. Tipos. Características."));
    temas_informatica.add(new Tema( 42,"Sistemas de base de datos distribuidos."));
    temas_informatica.add(new Tema( 43,"Administración de sistemas de base de datos."));
    temas_informatica.add(new Tema( 44,"Técnicas y procedimientos para la seguridad de los datos."));
    temas_informatica.add(new Tema( 45,"Sistemas de información. Tipos. Características. Sistemas de información en la empresa."));
    temas_informatica.add(new Tema( 46,"Aplicaciones informáticas de propósito general y para la gestión empresarial. Tipos. Funciones. Características."));
    temas_informatica.add(new Tema( 47,"Instalación y explotación de aplicaciones informáticas. Compartición de datos."));
    temas_informatica.add(new Tema( 48,"Ingeniería del software. Ciclo de desarrollo del software. Tipos de ciclos de desarrollo. Metodologías de desarrollo. Características distintivas de las principales metodologías de desarrollo utilizadas en la Unión Europea."));
    temas_informatica.add(new Tema( 49,"Análisis de sistemas: Modelización de tratamientos. Modelo de flujo de datos y control. Técnicas descriptivas. Documentación."));
    temas_informatica.add(new Tema( 50,"Análisis de sistemas: Modelización conceptual de datos. Técnicas descriptivas. Documentación."));
    temas_informatica.add(new Tema( 51,"Análisis de sistemas: Especificación funcional del sistema. Búsqueda y descripción de requisitos funcionales. Especificación de soluciones técnicas. Análisis de viabilidad técnica y económica."));
    temas_informatica.add(new Tema( 52,"Diseño lógico de funciones. Definición de funciones. Descomposición modular. Técnicas descriptivas. Documentación."));
    temas_informatica.add(new Tema( 53,"Diseño lógico de datos. Transformación del modelo conceptual a modelos lógicos. Análisis relacional de datos. Documentación."));
    temas_informatica.add(new Tema( 54,"Diseño de interfaces de usuario. Criterios de diseño. Descripción de interfaces. Documentación. Herramientas para la construcción de interfaces."));
    temas_informatica.add(new Tema( 55,"Diseño físico de datos y funciones. Criterios de diseño. Documentación."));
    temas_informatica.add(new Tema( 56,"Análisis y diseño orientado a objetos."));
    temas_informatica.add(new Tema( 57,"Calidad del software. Factores y métricas. Estrategias de prueba."));
    temas_informatica.add(new Tema( 58,"Ayudas automatizadas para el desarrollo de software (herramientas CASE). Tipos. Estructura. Prestaciones."));
    temas_informatica.add(new Tema( 59,"Gestión y control de proyectos informáticos. Estimación de recursos. Planificación temporal y organizativa. Seguimiento."));
    temas_informatica.add(new Tema( 60,"Sistemas basados en el conocimiento. Representación del conocimiento. Componentes y arquitectura."));
    temas_informatica.add(new Tema( 61,"Redes y servicios de comunicaciones."));
    temas_informatica.add(new Tema( 62,"Arquitecturas de sistemas de comunicaciones. Arquitecturas basadas en niveles. Estándares."));
    temas_informatica.add(new Tema( 63,"Funciones y servicios del nivel físico. Tipos y medios de transmisión. Adaptación al medio de transmisión. Limitaciones a la transmisión. Estándares."));
    temas_informatica.add(new Tema( 64,"Funciones y servicios del nivel de enlace. Técnicas. Protocolos."));
    temas_informatica.add(new Tema( 65,"Funciones y servicios del nivel de red y del nivel de transporte. Técnicas. Protocolos."));
    temas_informatica.add(new Tema( 66,"Funciones y servicios en niveles sesión, presentación y aplicación. Protocolos. Estándares."));
    temas_informatica.add(new Tema( 67,"Redes de área local. Componentes. Topologías. Estándares. Protocolos."));
    temas_informatica.add(new Tema( 68,"Software de sistemas en red. Componentes. Funciones. Estructura."));
    temas_informatica.add(new Tema( 69,"Integración de sistemas. Medios de interconexión. Estándares. Protocolos de acceso a redes de área extensa."));
    temas_informatica.add(new Tema( 70,"Diseño de sistemas en red local. Parámetros de diseño. Instalación y configuración de sistemas en red local."));
    temas_informatica.add(new Tema( 71,"Explotación y administración de sistemas en red local. Facilidades de gestión."));
    temas_informatica.add(new Tema( 72,"La seguridad en sistemas en red. Servicios de seguridad. Técnicas y sistemas de protección. Estándares."));
    temas_informatica.add(new Tema( 73,"Evaluación y mejora de prestaciones en un sistema en red. Técnicas y procedimientos de medidas."));
    temas_informatica.add(new Tema( 74,"Sistemas multimedia."));


    List<Tema> temas_musica = new ArrayList<>();
    temas_musica.add(new Tema( 1,"La audición: percepción, psicología, memoria y análisis. Anatomía y fisiología del oído."));
    temas_musica.add(new Tema( 2, "La voz humana y su fisiología. Clasificación de las voces. La voz en la adolescencia: características y problemática."));
    temas_musica.add(new Tema( 3,"El aparato fonador. Voz hablada y cantada. Respiración – Emisión – Impostación."));
    temas_musica.add(new Tema( 4,"La canción: aspectos analíticos y aspectos interpretativos. Tipos de canciones. La agrupación vocal. Repertorio vocal aplicado a la secundaria."));
    temas_musica.add(new Tema( 5,"Juegos e improvisaciones vocales: individuales y en grupo, libres y dirigidas, con y sin melodía."));
    temas_musica.add(new Tema( 6,"Acústica. Fundamentos físicos y su repercusión musical."));
    temas_musica.add(new Tema( 7,"Organología. Clasificación de los instrumentos."));
    temas_musica.add(new Tema( 8,"Los instrumentos a través del tiempo en la música occidental. Diferentes agrupaciones instrumentales."));
    temas_musica.add(new Tema( 9,"Los instrumentos como medio de expresión en general. Improvisación, juegos, danzas, canciones. Los instrumentos en el aula: características y relación con los instrumentos profesionales."));
    temas_musica.add(new Tema( 10,"Instrumentos folclóricos y étnicos."));
    temas_musica.add(new Tema( 11,"Aplicaciones de la informática y la electrónica en la música: a la interpretación, a la composición, a la audición, a la didáctica del lenguaje musical."));
    temas_musica.add(new Tema( 12,"Música y movimiento. Parámetros del movimiento. El gesto en la Música."));
    temas_musica.add(new Tema( 13,"Música y danza. Danzas folclóricas, históricas y de salón."));
    temas_musica.add(new Tema( 14,"Danzas de los distintos pueblos de España."));
    temas_musica.add(new Tema( 15,"Sonido – Silencio. Parámetros del sonido. El ruido."));
    temas_musica.add(new Tema( 16,"La Música, definiciones. La música como ciencia, como arte y como lenguaje."));
    temas_musica.add(new Tema( 17,"El ritmo. Pulso y métrica."));
    temas_musica.add(new Tema( 18,"La melodía. Tema, motivo y diseño melódico. La articulación melódica."));
    temas_musica.add(new Tema( 19,"Organización sonora. Tonalidad, modalidad, escalas, modos…"));
    temas_musica.add(new Tema( 20,"La textura musical. Tipos y evolución a través de la historia."));
    temas_musica.add(new Tema( 21,"El Contrapunto. Diversas concepciones a través del tiempo."));
    temas_musica.add(new Tema( 22,"La armonía. Diversas concepciones a través del tiempo."));
    temas_musica.add(new Tema( 23,"La notación musical: Evolución histórica de la notación. De los neumas a las notaciones actuales."));
    temas_musica.add(new Tema( 24,"La expresión musical. Agógica, dinámica y otras indicaciones."));
    temas_musica.add(new Tema( 25,"Procedimientos compositivos fundamentales: Repetición – Imitación – Variación – Desarrollo."));
    temas_musica.add(new Tema( 26,"La Forma musical."));
    temas_musica.add(new Tema( 27,"La improvisación como forma de expresión libre y como procedimiento compositivo."));
    temas_musica.add(new Tema( 28,"Orígenes de la Música occidental: Grecia. Roma. La Música cristiana primitiva."));
    temas_musica.add(new Tema( 29,"El canto gregoriano. La monodía religiosa."));
    temas_musica.add(new Tema( 30,"Música profana en la Edad Media."));
    temas_musica.add(new Tema( 31,"Polifonía medieval."));
    temas_musica.add(new Tema( 32,"Música medieval en España."));
    temas_musica.add(new Tema( 33,"La Música en el Renacimiento. Estilos. Teoría musical. Organología."));
    temas_musica.add(new Tema( 34,"Polifonía renacentista."));
    temas_musica.add(new Tema( 35,"Música instrumental en el Renacimiento."));
    temas_musica.add(new Tema( 36,"La Música del Renacimiento en España."));
    temas_musica.add(new Tema( 37,"La Música en el Barroco. Épocas. Estilos. Teoría musical. Organología."));
    temas_musica.add(new Tema( 38,"Música vocal en el Barroco."));
    temas_musica.add(new Tema( 39,"Música instrumental en el Barroco."));
    temas_musica.add(new Tema( 40,"La música en el siglo XVII en España."));
    temas_musica.add(new Tema( 41,"Estilos preclásicos y Clasicismo. Características generales. Organología."));
    temas_musica.add(new Tema( 42,"Formas instrumentales preclásicas y clásicas."));
    temas_musica.add(new Tema( 43,"Formas vocales preclásicas y clásicas."));
    temas_musica.add(new Tema( 44,"La música en el siglo XVIII en España."));
    temas_musica.add(new Tema( 45,"El ballet. Origen y evolución."));
    temas_musica.add(new Tema( 46,"La Música en el Romanticismo. Etapas. Estética musical. Organología."));
    temas_musica.add(new Tema( 47,"La Música instrumental en el Romanticismo. Música de cámara, pianística y orquestal."));
    temas_musica.add(new Tema( 48,"La Música vocal en el Romanticismo. La ópera y el lied."));
    temas_musica.add(new Tema( 49,"La Música en el siglo XIX en España."));
    temas_musica.add(new Tema( 50,"Diversidad de estilos musicales al final del siglo XIX y principios del XX. (I): Expresionismo y Nacionalismos."));
    temas_musica.add(new Tema( 51,"Diversidad de estilos musicales a finales del siglo XIX y principios del XX. (II): el Impresionismo."));
    temas_musica.add(new Tema( 52,"La Música en el siglo XX hasta la segunda guerra mundial. (I): la segunda escuela de Viena."));
    temas_musica.add(new Tema( 53,"La Música en el siglo XX hasta la segunda guerra mundial. (II): las vanguardias históricas."));
    temas_musica.add(new Tema( 54,"La Música en el siglo XX hasta la segunda guerra mundial. (III): el Neoclasicismo."));
    temas_musica.add(new Tema( 55,"La Música en el siglo XX hasta la segunda guerra mundial. (IV): la Música en España."));
    temas_musica.add(new Tema( 56,"Música después de la segunda guerra mundial (I): Música concreta, electrónica y electroacústica."));
    temas_musica.add(new Tema( 57,"Música después de la segunda guerra mundial (I): Música concreta, electrónica y electroacústica."));
    temas_musica.add(new Tema( 58,"Música después de la segunda guerra mundial (I): Música concreta, electrónica y electroacústica."));
    temas_musica.add(new Tema( 59,"El folclore musical en España."));
    temas_musica.add(new Tema( 60,"El Flamenco. Origen y evolución."));
    temas_musica.add(new Tema( 61,"Música africana y americana."));
    temas_musica.add(new Tema( 62,"El Jazz. Origen y evolución."));
    temas_musica.add(new Tema( 63,"La Música popular. El rock y el pop. Análisis musical y sociológico."));
    temas_musica.add(new Tema( 64,"El sonido grabado. Evolución de las técnicas de grabación y reproducción del sonido."));
    temas_musica.add(new Tema( 65,"El sonido grabado. Evolución de las técnicas de grabación y reproducción del sonido."));
    temas_musica.add(new Tema( 66,"Los medios de difusión y la Música a través del tiempo."));
    temas_musica.add(new Tema( 67,"Música e imagen: la música en el cine y en el teatro. Otras creaciones audiovisuales."));
    temas_musica.add(new Tema( 68,"Consumo de la Música en la sociedad actual: productos musicales al alcance de todos. Contaminación sonora."));
    temas_musica.add(new Tema( 69,"Teoría de la comunicación aplicada al lenguaje musical: compositor, partitura, intérprete, oyente."));
    temas_musica.add(new Tema( 70,"Métodos y sistemas didácticos actuales de educación musical: Orff-Schulwerk, Dalcroze, Martenot, Kodaly, Willens, Ward…"));


    List<Tema> temas_sistemas = new ArrayList<>();
    temas_sistemas.add(new Tema( 1,"Representación y comunicación de la información."));
    temas_sistemas.add(new Tema( 2, "Elementos funcionales de un ordenador digital. Arquitectura."));
    temas_sistemas.add(new Tema( 3,"Componentes, estructura y funcionamiento de la Unidad Central de Proceso."));
    temas_sistemas.add(new Tema( 4,"Memoria interna. Tipos. Direccionamiento. Características y funciones."));
    temas_sistemas.add(new Tema( 5,"Microprocesadores. Estructura. Tipos. Comunicación con el exterior."));
    temas_sistemas.add(new Tema( 6,"Microprocesadores. Estructura. Tipos. Comunicación con el exterior. "));
    temas_sistemas.add(new Tema( 7,"Dispositivos periféricos de entrada/salida. Características y funcionamiento."));
    temas_sistemas.add(new Tema( 8,"Componentes hardware comerciales de un ordenador. Placa base. Tarjetas controladoras de dispositivo y de entrada/salida."));
    temas_sistemas.add(new Tema( 9,"Lógica de circuitos. Circuitos combinacionales y secuenciales."));
    temas_sistemas.add(new Tema( 10,"Representación interna de los datos. "));
    temas_sistemas.add(new Tema( 11,"Organización lógica de los datos. Estructuras estáticas."));
    temas_sistemas.add(new Tema( 12,"Organización lógica de los datos. Estructuras dinámicas."));
    temas_sistemas.add(new Tema( 13,"Ficheros. Tipos. Características. Organizaciones."));
    temas_sistemas.add(new Tema( 14,"Utilización de ficheros según su organización."));
    temas_sistemas.add(new Tema( 15,"Sistemas operativos. Componentes. Estructura. Funciones. Tipos."));
    temas_sistemas.add(new Tema( 16,"Sistemas operativos: Gestión de procesos. "));
    temas_sistemas.add(new Tema( 17,"Sistemas operativos: Gestión de memoria."));
    temas_sistemas.add(new Tema( 18,"Sistemas operativos: Gestión de entradas/salidas."));
    temas_sistemas.add(new Tema( 19,"Sistemas operativos: Gestión de archivos y dispositivos."));
    temas_sistemas.add(new Tema( 20,"Explotación y administración de un Sistema Operativo Monousuario."));
    temas_sistemas.add(new Tema( 21,"Explotación y administración de un Sistema Operativo Multiusuario."));
    temas_sistemas.add(new Tema( 22,"Sistemas informáticos. Estructura física y funcional."));
    temas_sistemas.add(new Tema( 23,"Instalación de un sistema informático. Entorno. Elementos. Conexión. Configuración. Medidas de seguridad."));
    temas_sistemas.add(new Tema( 24,"Planificación y explotación de un Sistema Informático."));
    temas_sistemas.add(new Tema( 25,"Diseño de algoritmos. Técnicas descriptivas."));
    temas_sistemas.add(new Tema( 26,"Lenguajes de programación. Tipos y características."));
    temas_sistemas.add(new Tema( 27,"Programación estructurada. Estructuras básicas. Funciones y procedimientos."));
    temas_sistemas.add(new Tema( 28,"Programación modular. Diseño de funciones. Recursividad. Librerías."));
    temas_sistemas.add(new Tema( 29,"Programación orientada a objetos. Objetos. Clases. Herencia. Poliformismos."));
    temas_sistemas.add(new Tema( 30,"Programación en tiempo real. Interrupciones. Sincronización y comunicación entre tareas."));
    temas_sistemas.add(new Tema( 31,"Utilidades para el desarrollo y pruebas de programas. Compiladores. Intérpretes. Depuradores."));
    temas_sistemas.add(new Tema( 32,"Técnicas para la verificación, prueba y documentación de programas."));
    temas_sistemas.add(new Tema( 33,"Programación en lenguaje ensamblador. Instrucciones básicas. Formatos. Direccionamientos."));
    temas_sistemas.add(new Tema( 34,"Lenguaje C: Características generales. Elementos del lenguaje. Estructura de un programa. Funciones de librería y usuario. Entorno de compilación. Herramientas para la elaboración y depuración de programas en lenguaje C. "));
    temas_sistemas.add(new Tema( 35,"Lenguaje C: Manipulación de estructuras de datos dinámicas y estáticas. Entrada y salida de datos. Gestión de punteros. Punteros a funciones. Gráficos en C. "));
    temas_sistemas.add(new Tema( 36,"Sistemas gestores de bases de datos. Funciones. Componentes. Arquitectura de referencia y operacionales. Tipos de sistemas. "));
    temas_sistemas.add(new Tema( 37,"Modelo de datos relacional. Estructura. Operaciones. Álgebra relacional."));
    temas_sistemas.add(new Tema( 38,"Lenguajes para definición y manipulación de datos en sistemas de bases de datos relacionales. Tipos. Características. Lenguaje SQL. "));
    temas_sistemas.add(new Tema( 39,"Desarrollo de aplicaciones mediante bases de datos relacionales."));
    temas_sistemas.add(new Tema( 40,"Explotación automática de documentación administrativa."));
    temas_sistemas.add(new Tema( 41,"Aplicaciones informáticas de propósito general y para la gestión comercial. Tipos. Funciones. Características. "));
    temas_sistemas.add(new Tema( 42,"Instalación y explotación de aplicaciones informáticas."));
    temas_sistemas.add(new Tema( 43,"Utilización compartida de recursos, ficheros y datos entre aplicaciones informáticas."));
    temas_sistemas.add(new Tema( 44,"Análisis y diseño de aplicaciones informáticas."));
    temas_sistemas.add(new Tema( 45,"Análisis y diseño de servicios de presentación en un entorno gráfico."));
    temas_sistemas.add(new Tema( 46,"Diseño de interfaces gráficas de usuario."));
    temas_sistemas.add(new Tema( 47,"Diseño de interfaces en contexto de gestión."));
    temas_sistemas.add(new Tema( 48,"Lenguajes de alto nivel en entorno gráfico."));
    temas_sistemas.add(new Tema( 49,"Sistemas multimedia."));
    temas_sistemas.add(new Tema( 50,"Calidad y documentación en entornos gráficos."));
    temas_sistemas.add(new Tema( 51,"Ayudas automatizadas para el desarrollo de software (herramientas CASE). Tipos. Estructura. Prestaciones."));
    temas_sistemas.add(new Tema( 52,"Sistemas en red. Tipos. Componentes y topologías."));
    temas_sistemas.add(new Tema( 53,"Transmisión de datos. Medios. Tipos. Técnicas. Perturbaciones."));
    temas_sistemas.add(new Tema( 54,"Arquitectura de sistemas de comunicación. Niveles. Funciones. Servicios."));
    temas_sistemas.add(new Tema( 55,"Conexión de ordenadores en red. Elementos hardware necesarios. Tipos y características."));
    temas_sistemas.add(new Tema( 56,"Software de sistemas en red. Componentes. Funciones y estructura."));
    temas_sistemas.add(new Tema( 57,"Redes de área local. Hardware. Software. Recursos compartidos."));
    temas_sistemas.add(new Tema( 58,"Redes de área extensa. Interconexión redes locales."));
    temas_sistemas.add(new Tema( 59,"Análisis e implantación de un sistema en red."));
    temas_sistemas.add(new Tema( 60,"Instalación y configuración de sistemas en red local."));
    temas_sistemas.add(new Tema( 61,"Integración de sistemas. Medios de interconexión estándares."));
    temas_sistemas.add(new Tema( 62,"Evaluación y mejora del rendimiento de sistemas en red."));
    temas_sistemas.add(new Tema( 63,"Seguridad de los sistemas en red."));
    temas_sistemas.add(new Tema( 64,"Explotación y administración de sistemas en red."));
    temas_sistemas.add(new Tema( 65,"Análisis comparativo entre un sistema operativo multiusuario y un sistema en red."));

    List<Tema> temas_elec = new ArrayList<>();
    temas_elec.add(new Tema( 1,"Configuración y cálculo de instalaciones electroacústicas. Tipología y características. Fenómenos acústicos. Elementos que componen la instalación, tipología y características. Normativa y reglamentación."));
    temas_elec.add(new Tema( 2, "Técnicas de montaje, diagnóstico y localización de averías en instalaciones electroacústicas. Puestas a tierra, procedimientos y efectos de su inexistencia. Ajustes y puesta punto. Preparación de elementos. Medida de parámetros característicos de las instalaciones electroacústicas e instrumentos de medida específicos. Normativa y reglamentación."));
    temas_elec.add(new Tema( 3,"Configuración y cálculo de instalaciones de antenas de TV y vía satélite. Tipología y características. Fenómenos radioeléctricos. Elementos que componen la instalación, tipología y características. Normativa y reglamentación."));
    temas_elec.add(new Tema( 4,"Técnicas de montaje, diagnóstico y localización de averías en instalaciones de TV y vía satélite. Puestas a tierra, procedimientos y efectos de su inexistencia. Ajustes y puesta punto. Preparación de elementos. Medida de parámetros característicos de las instalaciones de antenas terrestres y vía satélite e instrumentos de medida específicos. Normativa y reglamentación."));
    temas_elec.add(new Tema( 5,"Sistemas de telefonía: conceptos básicos y ámbito de aplicación. La Red Telefónica Conmutada: estructura y características. Centrales telefónicas: tipología, características y jerarquía. Sistemas de conmutación: conceptos básicos, tipología y características. Sistemas de transmisión: medios de soporte utilizados, tipología y características. Elementos de un sistema telefónico privado, centralitas y terminales: tipología y características. Telefonía móvil y celular. Unidades y parámetros característicos de las instalaciones de telefonía e intercomunicación. Normativa y reglamentación."));
    temas_elec.add(new Tema( 6,"Configuración y cálculo de instalaciones de telefonía e intercomunicación. Tipología y características. Elementos que componen la instalación, tipología y características. Normativa y reglamentación."));
    temas_elec.add(new Tema( 7,"Técnicas de montaje, diagnóstico y localización de averías en instalaciones de telefonía e intercomunicación. Ajustes y puesta punto. Preparación de elementos. Puestas a tierra, procedimientos y efectos de su inexistencia. Medida de parámetros característicos de las instalaciones de telefonía e intercomunicación, instrumentos de medida específicos. Normativa y reglamentación."));
    temas_elec.add(new Tema( 8,"Configuración y cálculo de instalaciones de seguridad. Tipología y características. Elementos que componen la instalación, tipología y características. Normativa y reglamentación."));
    temas_elec.add(new Tema( 9,"Técnicas de montaje, diagnóstico y localización de averías en instalaciones de seguridad. Ajustes y puesta a punto. Preparación de elementos. Medida de parámetros característicos de las instalaciones de seguridad e instrumentos de medida específicos. Normativa y reglamentación."));
    temas_elec.add(new Tema( 10,"Configuración y cálculo de instalaciones de energía solar fotovoltaica. Tipología y características. Elementos que componen la instalación, tipología y características. Normativa y reglamentación."));
    temas_elec.add(new Tema( 11,"Técnicas de montaje, diagnóstico y localización de averías en instalaciones de energía solar fotovoltaica. Preparación de elementos. Medida de parámetros característicos de las instalaciones de energía solar fotovoltaica e instrumentos de medida específicos. Normativa y reglamentación."));
    temas_elec.add(new Tema( 12,"Técnicas básicas de mecanizado en instalaciones electrotécnicas. Metrología y trazado. Procedimientos y medios de mecanizado, constitución de los materiales empleados y propiedades. Protocolos de seguridad en el uso de máquinas y herramientas para el mecanizado."));
    temas_elec.add(new Tema( 13,"Diseño y construcción de cuadros eléctricos. Tipología y características de los cuadros eléctricos. Envolventes y materiales auxiliares. Planos de montaje y conexionado. Mecanizado de envolventes, montaje y conexionado de elementos."));
    temas_elec.add(new Tema( 14,"Transformadores: tipología, constitución, funcionamiento y características. Relaciones eléctricas fundamentales. Pérdidas en los transformadores. Comportamiento en vacío y en carga. Acoplamiento de transformadores. Elementos de protección y maniobra de transformadores."));
    temas_elec.add(new Tema( 15,"Bobinado y conexionado de transformadores monofásicos y polifásicos. Tipos y características de los transformadores. Cálculos."));
    temas_elec.add(new Tema( 16,"Mantenimiento de transformadores. Averías, detección y reparación. Procedimientos y medios. Ensayos de transformadores."));
    temas_elec.add(new Tema( 17,"Máquinas eléctricas rotativas en servicio. Placa de características. Protecciones mecánicas. Refrigeración. Acoplamientos entre motor y máquina accionada. Sujeción del motor en el entorno de funcionamiento: formas y aplicación. Alimentación. Sistemas de frenado. Parámetros que posibilitan la regulación de velocidad. Reglamentación y normativa."));
    temas_elec.add(new Tema( 18,"Mando de motores eléctricos. Sistemas de dispositivos de mando. Elementos de control. Elementos electrotécnicos de protección. Elementos de medida. Esquemas y automatismos eléctricos. Arranque y maniobra de máquinas eléctricas."));
    temas_elec.add(new Tema( 19,"Bobinado y conexionado de máquinas eléctricas rotativas de corriente continua. Tipos y características de las máquinas eléctricas rotativas de corriente continua. Tipos de bobinados. Cálculos."));
    temas_elec.add(new Tema( 20,"Mantenimiento de máquinas eléctricas rotativas de corriente continua. Rebobinado y reconexionado, equilibrado y verificación eléctrica del inducido, sustitución y ajuste de escobillas. Averías, detección y reparación. Procedimientos y medios. Ensayos de máquinas eléctricas rotativas de corriente continua."));
    temas_elec.add(new Tema( 21,"Bobinado y conexionado de máquinas eléctricas rotativas de corriente alterna. Tipos y características de las máquinas eléctricas rotativas de corriente alterna. Tipos de bobinados y rotores. Máquinas monofásicas y polifásicas. Cálculos."));
    temas_elec.add(new Tema( 22,"Mantenimiento de máquinas eléctricas rotativas de corriente alterna. Rebobinado y reconexionado para una nueva tensión de servicio, cambio de velocidad de régimen, etc. Averías, detección y reparación. Procedimientos y medios. Ensayos de máquinas eléctricas rotativas de corriente alterna."));
    temas_elec.add(new Tema( 23,"Instalaciones de distribución de energía eléctrica. Tipología y características. Líneas de media tensión y centros de transformación. Componentes y equipos. Simbología y representación de esquemas. Protocolos de medidas e instrumentación. Diagnóstico y localización de averías"));
    temas_elec.add(new Tema( 24,"Instalaciones de distribución eléctrica de BT en ambiente industrial. Reglamentación y normativa electrotécnica. Simbología y representación de esquemas. Protocolos de medidas e instrumentación. Diagnóstico y localización de averías."));
    temas_elec.add(new Tema( 25,"Configuración y cálculo de instalaciones de electrificación en el interior de viviendas. Tipología. Simbología. Normativa y reglamentación electrotécnica aplicables. Receptores, tipología y características."));
    temas_elec.add(new Tema( 26,"Configuración y cálculo de instalaciones de electrificación en locales de pública concurrencia. Tipología. Simbología. Normativa y reglamentación electrotécnica aplicables. Receptores, tipología y características."));
    temas_elec.add(new Tema( 27,"Configuración y cálculo de instalaciones en locales con riesgo de incendio o explosión. Tipología. Simbología. Normativa y reglamentación electrotécnica aplicables. Receptores, tipología y características."));
    temas_elec.add(new Tema( 28,"Configuración y cálculo de instalaciones en locales de características especiales. Tipología. Simbología. Normativa reglamentación electrotécnica aplicables. Receptores, tipología y características."));
    temas_elec.add(new Tema( 29,"Configuración y cálculo de instalaciones de alumbrado. Tipología. Simbología. Normativa y reglamentación electrotécnica aplicables. Receptores, tipología y características."));
    temas_elec.add(new Tema( 30,"Configuración y cálculo de instalaciones de alimentación de socorro. Tipología. Simbología. Normativa y reglamentación electrotécnica aplicables. Receptores, tipología y características."));
    temas_elec.add(new Tema( 31,"Técnicas de montaje de instalaciones de electrificación en viviendas y edificios. Construcción de una instalación eléctrica de baja tensión: interpretación de la documentación, selección de elementos y herramientas, montaje de los elementos, realización de pruebas y verificación de las especificaciones de la instalación. Conducciones rígidas y flexibles. Normas de seguridad aplicables."));
    temas_elec.add(new Tema( 32,"Diagnóstico y localización de averías en instalaciones de electrificación en viviendas y edificios. Medidas eléctricas en las instalaciones de BT, tensión, intensidad, resistencia y continuidad, potencia y tomas de tierra. Instrumentos de medida, procedimientos de conexión y medida. Tipología y características de las averías. Técnicas y procedimientos empleados en la diagnosis y reparación. Normas de seguridad personal y de los equipos."));
    temas_elec.add(new Tema( 33,"Protección de las instalaciones eléctricas y prevención de accidentes. Normativa de seguridad eléctrica. Protección contra sobreintensidades y sobretensiones, dispositivos. Protección contra contactos directos e indirectos, dispositivos."));
    temas_elec.add(new Tema( 34,"Principios básicos de la automatización. Sistemas cableados y sistemas programados: tipología y características. Tipos de energía para el mando, tecnologías y medios utilizados."));
    temas_elec.add(new Tema( 35,"Implementación de funciones digitales combinacionales con tecnologías eléctricas y electrónicas. Puertas lógicas, relés y contactores."));
    temas_elec.add(new Tema( 36,"Implementación de funciones digitales secuenciales con tecnologías eléctricas y electrónicas. Básculas, contadores, secuenciadores y otros."));
    temas_elec.add(new Tema( 37,"Técnicas básicas y medios utilizados en los sistemas de comunicación para instalaciones automatizadas. Sistemas de «bus» y de corrientes portadoras, tipología, características y normativa."));
    temas_elec.add(new Tema( 38,"Configuración de instalaciones automatizadas en viviendas y edificios para la gestión de la energía por corrientes portadoras y «bus» de dos hilos. Tipos estándares del mercado y características. Simbología y normativa aplicable."));
    temas_elec.add(new Tema( 39,"Configuración de instalaciones automatizadas en viviendas y edificios para la gestión de la seguridad por corrientes portadoras y «bus» de dos hilos. Tipos estándares del mercado y características. Simbología y normativa aplicable."));
    temas_elec.add(new Tema( 40,"Configuración de instalaciones automatizadas en viviendas y edificios para la gestión de la confortabilidad por corrientes portadoras y «bus» de dos hilos. Tipos estándares del mercado y características. Simbología y normativa aplicable."));
    temas_elec.add(new Tema( 41,"Configuración de instalaciones automatizadas en viviendas y edificios para la gestión de las telecomunicaciones por corrientes portadoras y «bus» de dos hilos. Tipos estándares del mercado y características. Simbología y normativa aplicable."));
    temas_elec.add(new Tema( 42,"Montaje, diagnóstico y localización de averías de instalaciones automatizadas en viviendas y edificios. Procedimientos y medios. Precauciones, seguridad personal y de las instalaciones."));
    temas_elec.add(new Tema( 43,"Puesta en servicio de las instalaciones automatizadas en viviendas y edificios. Programación de los equipos utilizados en las instalaciones automatizadas: centralitas, autómatas y programas específicos para ordenadores. Protocolos de puesta en marcha de las instalaciones."));
    temas_elec.add(new Tema( 44,"Sistemas automáticos basados en autómatas programables. El autómata programable en el sistema automatizado. Módulos del autómata programable: tipología y características. Detección y captación de señales. Preaccionadores y accionadores. Diálogo y comunicación entre autómatas programables. Elección de los elementos de automatización del sistema. Ciclo de programa."));
    temas_elec.add(new Tema( 45,"Programación de autómatas programables. Etapas en la elaboración de programas mediante el uso de lenguajes gráficos. Etapas, condiciones de transición, reglas de evolución del GRAFCET, ecuaciones lógicas, elecciones condicionales, secuencias simultáneas, saltos condicionales a otras etapas y acciones asociadas a etapas."));
    temas_elec.add(new Tema( 46,"Programación de autómatas programables. Etapas en la elaboración de programas mediante el uso de lista de instrucciones, tipos de instrucciones."));
    temas_elec.add(new Tema( 47,"Sistemas informáticos monousuario: características y campos de aplicación. Funcionamiento y prestaciones generales de los ordenadores. Unidad central de proceso: arquitecturas microprocesadas CISC y RISC, coprocesadores, memorias semiconductoras, memorias específicas -caché-, buses, controladores específicos. Sistemas informáticos multiusuario: características y campos de aplicación. Entorno básico de los sistemas: servidor del sistema, terminales; compartición de recursos, niveles de acceso."));
    temas_elec.add(new Tema( 48,"Instalación, puesta en marcha y configuración de un entorno informático monousuario. Condiciones eléctricas y medioambientales de una sala de informática. Arquitectura física de un sistema informático, estructura, topología y características."));
    temas_elec.add(new Tema( 49,"Sistemas operativos: tipología, características. Funciones de los sistemas operativos. Sistemas operativos más usuales. Entornos gráficos: características y tendencias. Diferencias entre sistema operativo y entorno gráfico."));
    temas_elec.add(new Tema( 50,"Sistema operativo: estructura y versiones. Instalación y configuración de un sistema operativo. Configuración de la memoria, de los dispositivos de entrada, las unidades de almacenamiento. Secuencia de arranque de un ordenador. Órdenes para la gestión de los recursos del sistema informático. Órdenes para la gestión de dispositivos de almacenamiento masivo. Órdenes para la gestión de ficheros. Órdenes para la gestión de los directorios y subdirectorios."));
    temas_elec.add(new Tema( 51,"Elaboración de documentos con programas informáticos. Manejo de aplicaciones de uso general: características, tipología y prestaciones. Instalación, configuración y utilización de procesadores de texto, gestores de base de datos, hojas de cálculo y diseñadores gráficos."));
    temas_elec.add(new Tema( 52,"Edición de esquemas por ordenador para las instalaciones eléctricas y sistemas automáticos. Programas: tipología, características y prestaciones. Parámetros de configuración de los programas. Captura, creación y edición de los elementos de diseño. Trazado e interconexión de los elementos de los esquemas. Verificación de las conexiones eléctricas de los esquemas. Simbología, normativa sobre representación gráfica de circuitos electrotécnicos. Procedimientos normalizados de representación gráfica de cuadros e instalaciones."));
    temas_elec.add(new Tema( 53,"Elaboración de documentación técnica de las instalaciones electrotécnicas y sistemas automáticos mediante el uso de medios informáticos. Partes que componen la documentación: esquemas eléctricos, planos de situación, memoria justificativa, lista de materiales, listado de los programas e control, pruebas de calidad, fiabilidad y otros. Procedimientos para el mantenimiento preventivo y correctivo. Soportes de almacenamiento de la documentación papel e informático."));
    temas_elec.add(new Tema( 54,"Teleinformática: conceptos básicos y elementos que integran los sistemas telemáticos. Códigos de representación de la información. Sistemas de conmutación utilizados en teleinformática."));
    temas_elec.add(new Tema( 55,"Transmisión de datos: conceptos básicos. Técnicas de transmisión. Modulación: función, tipología y características. Equipos de transmisión: «modems», multiplexores y concentradores. Terminales: tipología y características."));
    temas_elec.add(new Tema( 56,"Configuración e instalación de sistemas telemáticos. Selección de topología, equipos y medios para las redes locales. Puesta en servicio de redes locales de ordenadores. Conexión a redes de área extensa: equipos, medios y procedimientos. Diagnóstico y localización de averías en sistemas telemáticos. Medida de los parámetros básicos de comunicación: instrumentos y procedimientos."));
    temas_elec.add(new Tema( 57,"Configuración, montaje y mantenimiento de sistemas de control secuencial neumático. Fundamentos de la neumática. Principios, leyes básicas y propiedades de los gases. Instalaciones neumáticas. Elementos emisores de señales, de maniobra, de procesado y tratamiento de señales y de actuación neumáticos. Mantenimiento de las instalaciones neumáticas."));
    temas_elec.add(new Tema( 58,"Configuración, montaje y mantenimiento de sistemas de control secuencial hidráulico. Fundamentos de la hidráulica. Principios, leyes básicas y propiedades de los líquidos. Instalaciones hidráulicas. Elementos emisores de señales, de maniobra, de procesado y tratamiento de señales y de actuación hidráulicos. Mantenimiento de las instalaciones hidráulicas."));
    temas_elec.add(new Tema( 59,"Manipuladores y robots. Tipología y características. Campos de aplicación. Elementos y características. Sensores, actuadores y sistemas de control para robots y manipuladores."));
    temas_elec.add(new Tema( 60,"Diagnosis de averías y puesta en marcha de sistemas automáticos secuenciales. Medidas en los sistemas automáticos, instrumentos y procedimientos. Mantenimiento de los sistemas automáticos secuenciales."));
    temas_elec.add(new Tema( 61,"Diagnostico y localización de averías en circuitos básicos de electrónica de potencia. Dispositivos electrónicos de potencia: diodos, transistores y tiristores. Simbología normalizada de componentes electrónicos. Rectificadores monofásicos y trifásicos. Rectificación controlada."));
    temas_elec.add(new Tema( 62,"Control y regulación electrónica de máquinas eléctricas. Tipología y características. Estructura general de los sistemas de regulación de máquinas eléctricas. Dispositivos que componen la cadena de regulación. Tipología y características."));
    temas_elec.add(new Tema( 63,"Configuración de sistemas de regulación de motores de corriente continua. Técnicas y medios utilizados en la regulación de velocidad de motores de corriente continua. Equipos y dispositivos utilizados, características y tipología."));
    temas_elec.add(new Tema( 64,"Diagnóstico y localización de averías en los sistemas de regulación de velocidad de los motores de corriente continua. Medida en los sistemas de regulación de velocidad de motores de CC, instrumentos y procedimientos utilizados."));
    temas_elec.add(new Tema( 65,"Configuración de sistemas de regulación de motores de corriente alterna. Técnicas y medios utilizados en la regulación de velocidad de motores de corriente alterna. Equipos y dispositivos utilizados, características y tipología."));
    temas_elec.add(new Tema( 66,"Diagnóstico y localización de averías en los sistemas de regulación de velocidad de los motores de corriente alterna. Medida en los sistemas de regulación de velocidad de motores de corriente alterna, instrumentos y procedimientos utilizados."));
    temas_elec.add(new Tema( 67,"Fases en el desarrollo de proyectos. Especificaciones de proyecto. Elaboración de anteproyectos. Relación con clientes. Elaboración de presupuestos. Selección de la documentación de entrada. Proceso de ideación de soluciones. Utilización de bases de datos de ingeniería. Calidad en proyectos: técnicas y procedimientos."));
    temas_elec.add(new Tema( 68,"Técnicas para el desarrollo de proyectos. La organización por proyectos. Los grupos de proyectos. Organización matricial. Dirección técnica."));
    temas_elec.add(new Tema( 69,"Finalización y entrega de proyectos. Informes y documentación. Comunicado finalización formal del proyecto. Documentación: memoria justificativa, pliegos de condiciones, planos y esquemas, lista de materiales, presupuesto, anexos específicos. Documentación administrativa de las instalaciones electrotécnicas."));
    temas_elec.add(new Tema( 70,"Documentación para la gestión de un taller de mantenimiento: inventarios, hojas de material, boletines de averías, etc. Organización de almacenes. Codificación de materiales. Técnicas para la gestión de «stock». Elaboración de albaranes y facturas. Herramientas informáticas para la gestión de un taller. Libros de reclamaciones. Reglamentación y normativa vigente."));

    List<Especialidad> especialidades = new ArrayList<>();
    Especialidad especialidad_informatica = new Especialidad("Informática", temas_informatica);
    Especialidad especialidad_musica = new Especialidad("Música", temas_musica);
    especialidades.add(especialidad_musica);
    especialidades.add(especialidad_informatica);

    List<Especialidad> especialidades_fp = new ArrayList<>();
    Especialidad especialidad_sistemas = new Especialidad("Sistemas y aplicaciones informáticas", temas_sistemas);
    Especialidad especialidad_elec = new Especialidad("Instalaciones electrotécnicas", temas_elec);
    especialidades_fp.add(especialidad_sistemas);
    especialidades_fp.add(especialidad_elec);

    Cuerpo cuerpo_secun = new Cuerpo("Profesores de enseñanza secundaria", especialidades);
    Cuerpo cuerpo_fp = new Cuerpo("Profesores técnicos de formación profesional", especialidades_fp);

    cuerposRef.push().setValue(cuerpo_secun);
    cuerposRef.push().setValue(cuerpo_fp);
}

}
