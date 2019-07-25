package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <tt>true</tt> si el vértice tiene padre,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayPadre() {
            // Aquí va su código.
            return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <tt>true</tt> si el vértice tiene izquierdo,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            // Aquí va su código.
            return izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <tt>true</tt> si el vértice tiene derecho,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayDerecho() {
            // Aquí va su código.
            return derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            // Aquí va su código.
            if(!hayPadre())
                throw new NoSuchElementException();

            return padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            // Aquí va su código.
            if(!hayIzquierdo())
                throw new NoSuchElementException();

            return izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            // Aquí va su código.
            if(!hayDerecho())
                throw new NoSuchElementException();

            return derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            if(this == null)
                return -1;

            if(hayIzquierdo() && hayDerecho())
                return 1 + (Math.max(izquierdo.altura(), derecho.altura()));

            else if(!hayIzquierdo() && hayDerecho())
                return 1 + derecho.altura();

            else if(hayIzquierdo() && !hayDerecho())
                return 1 + izquierdo.altura();

            else
                return 0;
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            // Aquí va su código.
            return !hayPadre() ? 0 : 1 + padre.profundidad();
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            // Aquí va su código.
            return elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            // Aquí va su código.
            if(vertice == null)
                return false;
            if(!this.get().equals(vertice.get()))
                return false;
            if(!this.hayIzquierdo() && vertice.hayIzquierdo() || this.hayIzquierdo() && !vertice.hayIzquierdo())
                return false;
            if(!this.hayDerecho() && vertice.hayDerecho() || this.hayDerecho() && !vertice.hayDerecho())
                return false;
            
            if(this.hayIzquierdo())
                if(this.hayDerecho())
                    return this.izquierdo.equals(vertice.izquierdo) && this.derecho.equals(vertice.derecho);

                else return this.izquierdo.equals(vertice.izquierdo);

            else if(hayDerecho())
                return this.derecho.equals(vertice.derecho);

            return true;
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            // Aquí va su código.
            return get().toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        // Aquí va su código.
        for(T e : coleccion)
            agrega(e);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        // Aquí va su código.
        return esVacia() ? -1 : raiz().altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return busca(elemento) != null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <tt>null</tt>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        return busca(raiz, elemento);
    }

    /* Busca el vértice de un elemento en el árbol de manera recursiva.
     * Si no lo encuentra regres <tt>null</tt>.
     * @param Vertice vertice vértice desde el cual se iniciará la búsqueda.
     * @param elemento elemento a buscar
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    protected VerticeArbolBinario<T> busca(Vertice vertice, T elemento) {
        if(elemento == null || vertice == null)
            return null;

        if(vertice.get().equals(elemento))
            return vertice;

        VerticeArbolBinario<T> izquierdo = busca(vertice.izquierdo, elemento);
        VerticeArbolBinario<T> derecho = busca(vertice.derecho, elemento);

        if(izquierdo != null)
            return izquierdo;

        if(derecho != null)
            return derecho;

        return null;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        // Aquí va su código.
        if(esVacia())
            throw new NoSuchElementException();

        return raiz;
    }

    /*
     * Método auxiliar que indice si un vértice es hijo izquierdo.
     * @param v vértice a analizar.
     * @return boolean <code>true</code> si <code>v</code> es
     * hijo izquierdo, <code>false</code> en otro caso.
     */
    protected boolean esIzquierdo(Vertice v) {
        return v.padre.izquierdo == v;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return raiz == null;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
        raiz = null;
        elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        // Aquí va su código.
            if(esVacia() && arbol.esVacia())
                return true;

            if(esVacia() && !arbol.esVacia())
                return false;

            return raiz.equals(arbol.raiz);
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        // Aquí va su código.
        if(esVacia())
            return "";

        boolean [] contador = new boolean [altura() + 1];

        for(int i = 0; i < altura(); i++)
            contador[i] = false;

        return toString(this.raiz, 0, contador);
    }

    /*
     * Método auxiliar para agregar un espacio, o cualquier otro símbolo que represente una rama a nuestra cadena
     * @param int nivel en el que está el vértice
     * @param boolean [] binario arreglo para determinar si se debe agregar rama o un espacio
     * @return String representación de espacios en el árbol.
     */
    private String dibujaEspacios(int nivel, boolean [] binario){
        String s = "";
        for(int i = 0; i < nivel; i++)
            if(binario[i])
                s += "│  ";
            else
            s += "   ";
    
        return s;
    }

    /*
     * Método auxiliar que funciona de forma recursiva y se encarga de formar una cadena con los elementos del árbol
     * @param Vertice raíz del arból con la que trabajará el método
     * @param int nivel del árbol
     * @param boolean [] arreglo binario
     * @return String representación del árbol en <code>String</code>
     */
    private String toString(Vertice raiz, int nivel, boolean [] binario){
        String s = raiz + "\n";
        binario[nivel] = true;

        if(raiz.izquierdo != null && raiz.derecho != null){
            s += dibujaEspacios(nivel, binario);
            s += "├─›";
            s += toString(raiz.izquierdo, nivel + 1, binario);
            s += dibujaEspacios(nivel, binario);
            s += "└─»";
            binario[nivel] = false;
            s += toString(raiz.derecho, nivel + 1, binario);
        }else if(raiz.izquierdo != null){
            s += dibujaEspacios(nivel, binario);
            s += "└─›";
            binario[nivel] = false;
            s += toString(raiz.izquierdo, nivel + 1, binario);
        }else if(raiz.derecho != null){
            s += dibujaEspacios(nivel, binario);
            s += "└─»";
            binario[nivel] = false;
            s += toString(raiz.derecho, nivel + 1, binario);
        }
        
        return s;
    }
    

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
