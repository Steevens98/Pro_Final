/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Usuario
 */
public class NodoDobleCircular<T> {
    public T dato;

    public NodoDobleCircular<T> siguiente;
    public NodoDobleCircular<T> anterior;
    
    public NodoDobleCircular(T dato) {
        this.dato = dato;
        this.siguiente = this;
        this.anterior = this;
    }
}
