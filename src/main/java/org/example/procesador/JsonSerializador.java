package org.example.procesador;

import org.example.JsonAtributo;
import org.example.procesador.exception.JsonSerializadorException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class JsonSerializador {

    public static String convertirJson(Object object){

        if(Objects.isNull(object)){
            throw new JsonSerializadorException("El objeto a serializar no puede ser nulo");
        }

        Field[] atributos = object.getClass().getDeclaredFields();

        return Arrays.stream(atributos).filter(f -> f.isAnnotationPresent(JsonAtributo.class))
                .map(f -> {
                    f.setAccessible(true);
                    String nombre = f.getAnnotation(JsonAtributo.class).nombre().equals("")
                            ? f.getName() : f.getAnnotation(JsonAtributo.class).nombre();
                    try {
                        Object valor = f.get(object);
                        if(f.getAnnotation(JsonAtributo.class).capitalizar() && valor instanceof String){
                            String nuevoValor = (String) valor;
                            nuevoValor = nuevoValor.substring(0,1).toUpperCase() +
                                    nuevoValor.substring(1).toLowerCase();
                            f.set(object, nuevoValor);
                        }
                        return "\"" + nombre + "\":\"" + f.get(object) + "\"";
                    } catch (IllegalAccessException e) {
                        throw new JsonSerializadorException("Error al serializar a JSON: " + e.getMessage());
                    }
                })
                .reduce("{", (a,b) -> {
                    if("{".equals(a)){
                        return a+b;
                    }
                    return a + ", " + b;
                }).concat("}");
    }
}
