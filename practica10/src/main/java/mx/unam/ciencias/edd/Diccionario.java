package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            // Aquí va su código.
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            // Aquí va su código.
            Lista<Entrada> lista = new Lista<Entrada>();

            for(int i = 0; i < entradas.length; i++)
                if(entradas[i] != null)
                    for(Entrada e : entradas[i])
                        lista.agrega(e);
                
            iterador = lista.iterator();
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            // Aquí va su código.
            return iterador.hasNext();
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            // Aquí va su código. 
            return iterador.next();
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            // Aquí va su código.
            if(!super.hasNext())
                return null;

            return super.siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            // Aquí va su código.
            if(!super.hasNext())
                return null;

            return super.siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        // Aquí va su código.
        this.dispersor = dispersor;
        capacidad = (capacidad < MINIMA_CAPACIDAD) ? MINIMA_CAPACIDAD : capacidad;

        int c = 1;

        while(c < capacidad * 2)
            c *= 2;

        entradas = nuevoArreglo(c);
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        // Aquí va su código.
        if(llave == null || valor == null)
            throw new IllegalArgumentException();

        int dispersion = dispersor.dispersa(llave) & entradas.length - 1;

        if(entradas[dispersion] == null){
            entradas[dispersion] = new Lista<Entrada>();
            entradas[dispersion].agregaFinal(new Entrada(llave, valor));
            elementos++;
        }else if(contiene(llave)){
                for(Entrada e : entradas[dispersion])
                    if(e.llave.equals(llave))
                        e.valor = valor;
        }else{
            entradas[dispersion].agregaFinal(new Entrada(llave, valor));
            elementos++;
        }
        
        if(carga() >= MAXIMA_CARGA)
            expande();
    }

    /*
     * Método auxiliar que dobla el tamaño del arreglo del diccionario.
     */
    private void expande(){
        Iterador recorre = new Iterador();
        Entrada entra;
        int dispersion;
        Lista<Entrada> [] auxiliar = nuevoArreglo(entradas.length * 2);

        while(recorre.hasNext()){
            entra = recorre.siguiente();
            dispersion = (dispersor.dispersa(entra.llave)) & (auxiliar.length - 1);
            if(auxiliar[dispersion] == null){
                auxiliar[dispersion] = new Lista<Entrada>();
                auxiliar[dispersion].agrega(entra);
            }else {
                for(Entrada e : auxiliar[dispersion])
                    if(e.equals(entra.llave)){
                        e.valor = entra.valor;
                        break;
                    }
                auxiliar[dispersion].agregaFinal(entra);
            }
        }

        entradas = auxiliar;
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        // Aquí va su código.
        if(llave == null)
            throw new IllegalArgumentException();

        int dispersion = dispersor.dispersa(llave) & entradas.length -1;

        if(entradas[dispersion] != null)
            for(Entrada e : entradas[dispersion])
                if(e.llave.equals(llave))
                    return e.valor;
        
       throw new NoSuchElementException();
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
        // Aquí va su código.
        if(llave == null)
            return false;

        int dispersion = (dispersor.dispersa(llave)) & (entradas.length - 1);

        if(entradas[dispersion] == null)
            return false;

        for(Entrada e : entradas[dispersion])
            if(e.llave.equals(llave))
                return true;
        
        return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        // Aquí va su código.
        if(llave == null)
            throw new IllegalArgumentException();
        if(!contiene(llave))
            throw new NoSuchElementException();

        int dispersion = (entradas.length - 1) & (dispersor.dispersa(llave));

        for(Entrada e : entradas[dispersion])
            if(e.llave.equals(llave)){
                entradas[dispersion].elimina(e);
                if(entradas[dispersion].getLongitud() == 0)
                    entradas[dispersion] = null;
        
                elementos--;
                break;
            }
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        // Aquí va su código.
        int colision = 0;

        for(int i = 0; i < entradas.length; i++)
            if(entradas[i] != null && entradas[i].getLongitud() > 1)
                colision++;
            
        return colision;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        // Aquí va su código.
        int maxima = 0;

        for(int i = 0; i < entradas.length; i++)
            if(entradas[i] != null)
                if(entradas[i].getLongitud() > maxima)
                    maxima = entradas[i].getLongitud();
        
        return maxima - 1;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        // Aquí va su código.
        return (double) elementos / entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        // Aquí va su código.
        entradas = nuevoArreglo(entradas.length);
        elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        // Aquí va su código.
        Iterador entrada = new Iterador();
        String s = "{ ";

        if(getElementos() == 0)
            return "{}";

        Entrada seguida;

        while(entrada.hasNext()){
            seguida = entrada.siguiente();
            s += "'" + seguida.llave.toString() + "': ";
            s += "'" + seguida.valor.toString() + "', "; 
        }

        s += "}";

        return s;
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        // Aquí va su código.
        if(getElementos() != d.getElementos())
            return false;

        Iterator<K> llavecin = iteradorLlaves();
        K llavecilla;

        while(llavecin.hasNext()){
            llavecilla = llavecin.next();
            if(!d.contiene(llavecilla) || !(d.get(llavecilla).equals(get(llavecilla))))
                return false;
        }
        
        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
