package org.example;

import org.example.models.Producto;
import org.example.procesador.JsonSerializador;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Producto p = new Producto();
        p.setFecha(LocalDate.now());
        p.setNombre("mesa centro roble");
        p.setPrecio(1000L);

        System.out.println("JSON = " + JsonSerializador.convertirJson(p));
    }
}