package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            // Aquí va su código.
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
            if(!hasNext())
                throw new NoSuchElementException();

            anterior = siguiente;
            siguiente = siguiente.siguiente;

            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
            if(!hasPrevious())
                throw new NoSuchElementException();

            siguiente = anterior;
            anterior = anterior.anterior;

            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return getLongitud() == 0;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException();

        Nodo agregado = new Nodo(elemento);

        if(esVacia())
            cabeza = rabo = agregado;
        else{
            rabo.siguiente = agregado;
            agregado.anterior = rabo;
            rabo = agregado;
        }

        longitud++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException();

        Nodo agregado = new Nodo(elemento);

        if(esVacia())
            cabeza = rabo = agregado;
        else{
            cabeza.anterior = agregado;
            agregado.siguiente = cabeza;
            cabeza = agregado;
        }

        longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException();

        if(i <= 0){
            agregaInicio(elemento);
            return;
        }else if(i >= getLongitud()){
            agregaFinal(elemento);
            return;
        }

        Nodo buscado = busca(i);
        Nodo agregado = new Nodo(elemento);
        
        agregado.anterior = buscado.anterior;
        agregado.siguiente = buscado;
        buscado.anterior.siguiente = agregado;
        buscado.anterior = agregado;
        longitud++;
    }

    /*
     * Método auxiliar que busca un nodo dado un índice.
     * @param indice índice del nodo que se requiere.
     * @return Nodo nodo con índice i.
     */
    private Nodo busca(int i){
        int contador = 0;
        Nodo auxiliar = cabeza;

        while(contador != i){
            auxiliar = auxiliar.siguiente;
            contador++;
        }

        return auxiliar;
    }

    /* 
     * Método auxiliar que busca un nodo dado un elemento
     * @param elemento elemento a buscar
     * @return Nodo primer nodo que contenga a <code>elemento</code>
     */
    private Nodo busca(T elemento){
        if(elemento == null)
            return null;

        Nodo auxiliar = cabeza;

        while(auxiliar != null && !auxiliar.elemento.equals(elemento))
            auxiliar = auxiliar.siguiente;

        return auxiliar;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Nodo eliminado = busca(elemento);

        if(eliminado == null)
            return;

        if(getLongitud() == 1)
            cabeza = rabo = null;
        else if(eliminado == cabeza){
            eliminado.siguiente.anterior = null;
            cabeza = eliminado.siguiente;
        }else if(eliminado == rabo){
            eliminado.anterior.siguiente = null;
            rabo = eliminado.anterior;
        }
        else{
            eliminado.anterior.siguiente = eliminado.siguiente;
            eliminado.siguiente.anterior = eliminado.anterior;
        }

        longitud--;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        if(esVacia())
            throw new NoSuchElementException();

        T eliminado = getPrimero();
        elimina(eliminado);

        return eliminado;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        if(esVacia())
            throw new NoSuchElementException();

        T eliminado = getUltimo();

        if(getLongitud() == 1)
            cabeza = rabo = null;
        else{
            rabo.anterior.siguiente = null;
            rabo = rabo.anterior;
        }

        longitud--;

        return eliminado;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        Nodo buscado = busca(elemento);

        return buscado != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
        Lista<T> reversa = new Lista<T>();
        IteradorLista<T> it = iteradorLista();
        it.end();

        while(it.hasPrevious())
            reversa.agrega(it.previous());
        
        return reversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
        Lista<T> copia = new Lista<T>();
        Iterator<T> it = iterator();

        while(it.hasNext())
            copia.agregaFinal(it.next());

        return copia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        cabeza = rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        if(esVacia())
            throw new NoSuchElementException();

        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        if(esVacia())
            throw new NoSuchElementException();

        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if(i < 0 || i >= getLongitud())
            throw new ExcepcionIndiceInvalido();

        return busca(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        if(!contiene(elemento))
            return -1;

        int contador = 0;
        Nodo auxiliar = cabeza;

        while(!auxiliar.elemento.equals(elemento)){
            auxiliar = auxiliar.siguiente;
            contador++;
        }

        return contador;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
        Iterator<T> it = iterator();
        String toString = "[";
        int contador = 0;

        if(esVacia())
            return toString + "]";

        while(it.hasNext())
            if(contador != getLongitud() -1){
                toString += it.next().toString() + ", ";
                contador++;
            }else
                toString += it.next().toString() + "]";   
        
        return toString;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        // Aquí va su código.
        if(getLongitud() != lista.getLongitud())
            return false;

        Iterator<T> it = iterator();
        Iterator<T> it2 = lista.iterator();

        while(it.hasNext())
            if(!it.next().equals(it2.next()))
                return false;

        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        // Aquí va su código.
        return mergeSort(this, comparador);
    }

    /*
     * Método auxiliar que regresa la copia de una lista, pero ordenada.
     * @param l lista que se quiere ordenar.
     * @param comparador comparador con el cual se harán las comparaciones.
     * @return Lista<T> <code>l</code> pero ordenada.
     */
    private Lista<T> mergeSort(Lista<T> l, Comparator<T> comparador){
        int n = l.getLongitud();
        int t1;
        int t2;
        int c1 = 0;
        int c2 = 0;
        Lista<T> l1 = new Lista<T>();
        Lista<T> l2 = new Lista<T>();
        Nodo auxiliar = l.cabeza;

        if (n == 1 || n == 0)
            return l.copia();

        if(n % 2 == 1){
            t1 = (n - 1) / 2;
            t2 = t1 + 1;
        }else
            t1 = t2 = n / 2;
        

        while (c1 != t1) {
            l1.agregaFinal(auxiliar.elemento);
            auxiliar = auxiliar.siguiente;
            c1++;
        }

        while (c2 != t2) {
            l2.agregaFinal(auxiliar.elemento);
            auxiliar = auxiliar.siguiente;
            c2++;
        }

        l1 = mergeSort(l1, comparador);
        l2 = mergeSort(l2, comparador);

        return mezcla(l1, l2, comparador);
    }

    /*
     * Método auxiliar que mezcla dos listas ordenadas.
     * @param l1 primera lista a mezclar.
     * @param l2 segunda lista a mezclar.
     * @param comparador comparador con el que se harán las comparaciones.
     * @return Lista<T> lista ya ordenada.
     */
    private Lista<T> mezcla(Lista<T> l1, Lista<T> l2, Comparator<T> comparador){
        Lista<T> mezclada = new Lista<T>();
        Nodo i = l1.cabeza;
        Nodo j = l2.cabeza;
        while (i != null && j != null) 
            if (comparador.compare(i.elemento, j.elemento) <= 0){
                mezclada.agregaFinal(i.elemento);
                i = i.siguiente;
            }else{
                mezclada.agregaFinal(j.elemento);
                j = j.siguiente;
            }

        if (i == null) 
            while (j != null){
                mezclada.agregaFinal(j.elemento);
                j = j.siguiente;
            }
        else{
            while (i != null) {
                mezclada.agregaFinal(i.elemento);
                i = i.siguiente;
            }
        }

        return mezclada;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        if(elemento == null)
            return false;

        if(!esVacia()) 
            if(comparador.compare(elemento, getPrimero()) < 0)
                return false;

        for(T e : this) 
            if(comparador.compare(elemento, e) == 0)
                return true;

        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
