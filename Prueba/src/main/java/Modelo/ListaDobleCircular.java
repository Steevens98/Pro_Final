package Modelo;

import java.util.Comparator;

/**
 *
 * @author Usuario
 */
public class ListaDobleCircular<T> {


    public NodoDobleCircular<T> cabeza;
    public ListaDobleCircular() {
        this.cabeza = null;
    }
    
    // Agregar al final
    public void agregar(T valor) {
        NodoDobleCircular<T> nuevo = new NodoDobleCircular<>(valor);
        
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoDobleCircular<T> ultimo = cabeza.anterior;
            nuevo.siguiente = cabeza;
            nuevo.anterior = ultimo;
            cabeza.anterior = nuevo;
            ultimo.siguiente = nuevo;
        }
    }
    
    // Eliminar un nodo por valor
    public boolean eliminar(T valor) {
        if (cabeza == null)
            return false;
        NodoDobleCircular<T> actual = cabeza;
        
        do {
            if (actual.dato == valor) {
                if (actual == cabeza &&
                actual.siguiente == cabeza) {
                // Un solo nodo
                cabeza = null;

                } else {
                    
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    
                    if (actual == cabeza){
                        cabeza = actual.siguiente;
                    }
                }
            return true;
            }
            actual = actual.siguiente;
        } while (actual != cabeza);
        return false;
    }
    
    // Mostrar hacia adelante
    public void mostrarAdelante() {

        if (cabeza == null) {
            System.out.println("Lista vacía");
            return;
        }

        NodoDobleCircular<T> actual = cabeza;

        do {
            System.out.print(actual.dato
                    + " <-> ");
            actual = actual.siguiente;
        } while (actual != cabeza);
        System.out.println("(vuelve al inicio)");
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    // Mostrar hacia atrás
    public void mostrarAtras() {
        if (cabeza == null) {
            System.out.println("Lista vacía");
            return;
        }
        NodoDobleCircular<T> actual
                = cabeza.anterior;

        do {
            System.out.print(actual.dato
                    + " <-> ");
            actual = actual.anterior;
        } while (actual != cabeza.anterior);
        System.out.println("(vuelve al final)");
    }

    // Tamaño
    public int tamaño() {
        if (cabeza == null) {
            return 0;
        }
        int contador = 0;
        NodoDobleCircular<T> actual = cabeza;
        do {
            contador++;
            actual = actual.siguiente;
        } while (actual != cabeza);
        return contador;
    }

    // Devuelve el nodo siguiente del nodo actual
    public NodoDobleCircular<T> avanzar(NodoDobleCircular<T> actual) {
        if (actual != null) {
            return actual.siguiente;
        }
        return null;
    }

    // Devuelve el nodo anterior del nodo actual
    public NodoDobleCircular<T> retroceder(NodoDobleCircular<T> actual) {
        if (actual != null) {
            return actual.anterior;
        }
        return null;
    }
    
    public void ordenarPor(Comparator<T> comparador) {
        if (cabeza == null || cabeza.siguiente == cabeza) {
            return; // lista vacía o un nodo
        }
        boolean cambio;
        do {
            cambio = false;
            NodoDobleCircular<T> actual = cabeza;
            boolean primeraVuelta = true;
            while (actual.siguiente != cabeza || primeraVuelta) {
                primeraVuelta = false;
                NodoDobleCircular<T> siguiente = actual.siguiente;
                if (comparador.compare(actual.dato, siguiente.dato) > 0) {
                    T temp = actual.dato;
                    actual.dato = siguiente.dato;
                    siguiente.dato = temp;
                    cambio = true;
                }
                actual = actual.siguiente;
            }
        } while (cambio);
    }
    
    public boolean contiene(T elemento) {
        if (estaVacia()) {
            return false;
        }

        NodoDobleCircular<T> actual = cabeza;
        do {
            if (elemento instanceof Contacto && actual.dato instanceof Contacto) {
                Contacto c1 = (Contacto) actual.dato;
                Contacto c2 = (Contacto) elemento;
                if (c1.getId().equals(c2.getId())) {
                    return true;
                }
            } else {
                if (actual.dato.equals(elemento)) {
                    return true;
                }
            }
            actual = actual.siguiente;
        } while (actual != cabeza);

        return false;
    }
    
}


