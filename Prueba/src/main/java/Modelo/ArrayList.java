package Modelo;

/**
 *
 * @author Usuario
 */
public class ArrayList<E> implements Iterable<E>{
    private E[] elementos;
    private int capacidad;
    private int size;
    
    @SuppressWarnings("unchecked")
    public ArrayList(int capacidadInicial){
        this.capacidad = capacidadInicial;
        this.elementos = (E[]) new Object[capacidadInicial];
        this.size = 0;
    }

    public void addFirst(E elemento){
        if (size == capacidad) {
            expandirCapacidad();
        }
        for (int i = size -1 ; i >= 0; i--) {
            elementos[i+1] = elementos[i];
        }
        elementos[0] = elemento;
        size++;
    }

    public void addLast(E elemento) {
        if (size == capacidad) {
            expandirCapacidad();
        }
        elementos[size++] = elemento;
    }

    public void imprimir(){
        for (int i = 0 ; i < size; i++) {
            System.out.print(elementos[i]+ " ");
        }
        System.out.println();
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        return elementos[index];
    }

    public void set(int index, E elemento) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        elementos[index] = elemento;
    }

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void expandirCapacidad(){
        capacidad = capacidad * 2 ;
        E[] nuevoArreglo = (E[]) new Object[capacidad];
        for (int i = 0; i < size; i++) {
            nuevoArreglo[i] = elementos[i];
        }
        elementos = nuevoArreglo;
    }
    
    @Override
    public java.util.Iterator<E> iterator() {
        return new java.util.Iterator<E>() {
            private int indice = 0;

            @Override
            public boolean hasNext() {
                return indice < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return elementos[indice++];
            }
        };
    }
}