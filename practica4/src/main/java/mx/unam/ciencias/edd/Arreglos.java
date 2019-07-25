package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
        quickSort(arreglo, comparador, 0, arreglo.length - 1);
    }

    /* 
     * Método auxiliar que ordena un arreglo usando el algoritmo quicksort.
     * @param arreglo arreglo a ordenar.
     * @param comparador instancia de <code>Comparator<T>></code> con la
     * que se harán las comparaciones.
     * @param a extremo izquierdo del subarreglo,
     * @param b extremo derecho del subarreglo.
     */
    private static <T> void quickSort(T[] arreglo, Comparator<T> comparador, int a, int b){
        if(b <= a)
            return;

        int i = a + 1;
        int j = b;

        while (i < j){

            if(comparador.compare(arreglo[i], arreglo[a]) > 0 && comparador.compare(arreglo[j], arreglo[a]) <= 0){
                intercambia(arreglo, i, j);
                i += 1;
                j -= 1;
            }else if(comparador.compare(arreglo[i], arreglo[a]) <= 0){
                i += 1;
            }else
                j -= 1;
        }

        if(comparador.compare(arreglo[i], arreglo[a]) > 0)
            i -= 1;

        intercambia(arreglo, a, i);
        quickSort(arreglo, comparador, a, i - 1);
        quickSort(arreglo, comparador, i + 1, b);
    }


    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
        int menor;
        for(int i = 0; i < arreglo.length; i++){
            menor = i;
            for(int j = i + 1; j < arreglo.length; j++)
                if(comparador.compare(arreglo[j], arreglo[menor]) < 0)
                    menor = j;
                
            intercambia(arreglo, i, menor);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /* 
     * Método auxiliar que intercambia dos objetos dado un arreglo.
     * @param arreglo arreglo en el que se quieren realizar los intercambios.
     * @param int a índice del primer elemento a intercambiar.
     * @oaram int b índice del segundo elemento a intercambiar.
     * @return T[] arreglo con los elementos especificados intercambiados.
     */
    private static <T> void intercambia(T[] arreglo, int a, int b){
        T elementoA = arreglo[a];
        arreglo[a] = arreglo[b];
        arreglo[b] = elementoA;
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        return busquedaBinaria(arreglo, elemento, comparador, 0, arreglo.length - 1);
    }

    /*
     * Método auxiliar que hace búsqueda binaria en una arreglo usando recursión.
     * @param  arreglo arreglo sobre el cual se va a buscar un elemento.
     * @param elemento elemento a buscar en el arreglo <code>arreglo</code>.
     * @param comparador comparador para comparar elementos.
     * @param a extremo izquierdo del subarreglo.
     * @param b extremo derecho del subarreglo.
     * @return int índice del elemento; <code>-1</code> si no lo encuentra.
     */
    private static <T> int busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador, int a, int b){
        int medio = (b + a) / 2;

        if(comparador.compare(arreglo[medio], elemento) == 0)
            return medio;

        if(a > b)
            return -1;

        if(comparador.compare(arreglo[medio], elemento) < 0)
            return busquedaBinaria(arreglo, elemento, comparador, medio + 1, b);
        
        return busquedaBinaria(arreglo, elemento, comparador, a, medio - 1);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
