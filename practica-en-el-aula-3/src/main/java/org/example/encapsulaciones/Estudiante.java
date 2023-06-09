package org.example.encapsulaciones;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by vacax on 02/06/16.
 */
@Entity
public class Estudiante implements Serializable {

    @Id
    private int matricula;
    private String nombre;
    private String apellido;
    private String carrera;
    private String telefono;


    public Estudiante(){  //Debo tener el constructor vacio...

    }


    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @PreUpdate
    @PrePersist
    private void cancelarMatricula() {
        //setNombre(getNombre().toUpperCase()); //cambiando a mayuscula
        if(getMatricula() == 20011137){
            throw new RuntimeException("No puede ser esa matricula..");
        }
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
