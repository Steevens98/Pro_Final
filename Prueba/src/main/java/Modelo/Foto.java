/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Usuario
 */
public class Foto {
    private String ruta;
    private String descripcion;
    private LocalDate fechaCreacion;
    
    public Foto(String ruta, LocalDate fechaCreacion) {
        this.ruta = Objects.requireNonNull(ruta, "La ruta no puede ser nula");
        this.fechaCreacion = fechaCreacion;
    }

    public Foto(String ruta) {
        this(ruta, LocalDate.now());
    }

    // Getters y Setters (encapsulamiento)
    public String getRuta() {
        return ruta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    // Para comparar fotos por ruta (usado en GestorFotos)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Foto otraFoto = (Foto) obj;
        return ruta.equals(otraFoto.ruta);
    }

    @Override
    public int hashCode() {
        return ruta.hashCode();
    }

    @Override
    public String toString() {
        return "Foto{" +
               "ruta='" + ruta + '\'' +
               ", fecha=" + fechaCreacion +
               '}';
    }
}