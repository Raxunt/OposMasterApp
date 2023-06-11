package com.damdos.oposmasterapp.model;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validaciones {

    public static boolean validarCorreo(String correo) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(correo).matches();
    }


    public static boolean validarPassword(String password) {
        // Pattern patron = Pattern.compile("[
        // 0-9A-Za-zñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ¡!¿?@#$%()=+-€/.,]{3,15}");
        Pattern patron = Pattern.compile("[0-9A-Za-z_]{3,15}");
        Matcher comprobacion = patron.matcher(password);
        return comprobacion.matches();
    }
    public static boolean validarNombre(String nombre) {
        // Pattern patron = Pattern.compile("[
        // 0-9A-Za-zñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ¡!¿?@#$%()=+-€/.,]{3,15}");
        Pattern patron = Pattern.compile("[0-9A-Za-zñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ_ -]{2,15}");
        Matcher comprobacion = patron.matcher(nombre);
        return comprobacion.matches();
    }

}
