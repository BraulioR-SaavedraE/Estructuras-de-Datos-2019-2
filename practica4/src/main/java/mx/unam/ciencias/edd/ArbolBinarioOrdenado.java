package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
            pila = new Pila<Vertice>();

            Vertice auxiliar = raiz;
            
            while(auxiliar != null) {
                pila.mete(auxiliar);
                auxiliar = auxiliar.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
            Vertice next = pila.saca();
            T siguiente = next.get();

            if(next.hayDerecho()) {
                next = next.derecho;
                pila.mete(next);

                while(next.hayIzquierdo()) {
                    pila.mete(next.izquierdo);
                    next = next.izquierdo;
                }
            }
            return siguiente;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException();

        elementos++;
        Vertice agregado = nuevoVertice(elemento);
        ultimoAgregado = agregado;

        if(raiz == null) {
            raiz = agregado;
            return;
        }
        
        agrega(raiz, agregado);

    }

    /* 
     * Método auxiliar recursivo que agrega un elemento en orden.
     * @param u vértice sobre el cual se inicia el recorrido de
     * comparaciones.
     * @param v vértice que contiene el elemento a agregar.
     */
    private void agrega(Vertice u, Vertice v) { 
        if(v.get().compareTo(u.get()) <= 0) {
            if(!u.hayIzquierdo()) {
                v.padre = u;
                u.izquierdo = v;
                ultimoAgregado = v;
                return;
            }
            else
                agrega(u.izquierdo, v);
        }else {
            if(!u.hayDerecho()) {
                v.padre = u;
                u.derecho = v;
                ultimoAgregado = v;
                return;
            }
            else
                agrega(u.derecho, v);
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice eliminado = vertice(busca(elemento));

        if(eliminado == null)
            return;

        if(!eliminado.hayIzquierdo() || !eliminado.hayDerecho()) {  // si el vértice tiene a lo más un hijo.
            eliminaVertice(eliminado);
            return;
        }

        Vertice intercambio = intercambiaEliminable(eliminado);   // si el vértice tiene dos hijos.
        eliminaVertice(intercambio);
    }

    /* Método auxiliar que elimina un vértice sin hijo izquierdo ni derecho.
     * @param v vértice a eliminar
     */
    private void eliminaHoja(Vertice v) {
        if (!v.hayPadre()) {    // en caso de que v sea la raíz.
            raiz = null;
            elementos--;
            return;
        }

        if(super.esIzquierdo(v))
            v.padre.izquierdo = null;
        else
            v.padre.derecho = null;

        elementos--;
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
        Vertice maximo = maximoEnSubArbol(vertice.izquierdo);
        T eliminado = vertice.elemento;
        vertice.elemento = maximo.elemento;
        maximo.elemento = eliminado;

        return maximoEnSubArbol(vertice.izquierdo);
    }

    /* Método auxiliar que busca el primer vértice derecho sin hijo derecho en
     * el subárbol izquierdo de <code>v</code>.
     * @param v vértice sobre el cual se empezará el recorrido.
     * @return Vertice vértice derecho sin hijo derecho.
     */
    private Vertice maximoEnSubArbol(Vertice v) {
        if(v.derecho == null)
            return v;

        return maximoEnSubArbol(v.derecho);
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
        if(vertice == null)
            return;

        if(!vertice.hayIzquierdo() && !vertice.hayDerecho()) {
            eliminaHoja(vertice);
            return;
        }
        
        if(vertice.hayIzquierdo()) {
            if(vertice.hayPadre()) {
                if(esIzquierdo(vertice)) {
                    vertice.izquierdo.padre = vertice.padre;
                    vertice.padre.izquierdo = vertice.izquierdo;
                }else {
                    vertice.padre.derecho = vertice.izquierdo;
                    vertice.izquierdo.padre = vertice.padre;
                }
            }else {
                raiz = vertice.izquierdo;
                vertice.izquierdo.padre = null;
            } 
        }else {
            if(vertice.hayPadre()) {
                if(esIzquierdo(vertice)) {
                    vertice.padre.izquierdo = vertice.derecho;
                    vertice.derecho.padre = vertice.padre;
                } else {
                    vertice.padre.derecho = vertice.derecho;
                    vertice.derecho.padre = vertice.padre;
                }
            }else {
                raiz = vertice.derecho;
                vertice.derecho.padre = null;
            }
        }
        elementos--;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        return busca(raiz, elemento);
    }

    @Override public VerticeArbolBinario<T> busca(Vertice v, T elemento) {
        if(v == null || elemento == null)
            return null;

        if(v.elemento.equals(elemento))
            return v;
        else if(elemento.compareTo(v.elemento) < 0)
            return busca(v.izquierdo, elemento);
        else 
            return busca(v.derecho, elemento);
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        Vertice q = vertice(vertice);

        if(vertice == null || !q.hayIzquierdo())
            return;

        Vertice p = q.izquierdo;
        Vertice s = p.derecho;
        Vertice padre = q.padre;

        p.padre = padre;
        if(padre != null) 
            if(esIzquierdo(q))
                padre.izquierdo = p;
            else 
                padre.derecho = p;
        else 
            raiz = p;
        
        q.padre = p;
        p.derecho = q;

        if(s != null)
            s.padre = q;
           
         q.izquierdo = s; 
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        Vertice p = vertice(vertice);

        if(vertice == null || !p.hayDerecho())
            return;

        Vertice q = p.derecho;
        Vertice s = q.izquierdo;
        Vertice padre = p.padre;

        q.padre = padre;
        if(padre != null)
            if(esIzquierdo(p))
                padre.izquierdo = q;
            else
                padre.derecho = q;
        else
            raiz = q;
        

        if(s != null)
            s.padre = p;

        p.derecho = s;
        p.padre = q;
        q.izquierdo = p;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPreOrder(accion, raiz);
    }

    /*
     * Método auxiliar que hace un recorrido DFS pre-order recursivamente.
     * @param accion acción a realizar.
     * @param v vértice sobre el que se aplicará el método.
     */
    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {

        if(v == null)
            return;

        accion.actua(v);
        dfsPreOrder(accion, v.izquierdo);
        dfsPreOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsInOrder(accion, raiz);
    }

    /*
     * Método auxiliar que hace un recorrido DFS in-order recursivamente.
     * @param accion acción a realizar.
     * @param v vértice sobre el que se aplicará el método.
     */
    private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
        if(v == null)
            return;

        dfsInOrder(accion, v.izquierdo);
        accion.actua(v);
        dfsInOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPostOrder(accion, raiz);
    }

    /*
     * Método auxiliar que hace un recorrido DFS post-order recursivamente.
     * @param accion acción a realizar.
     * @param v vértice sobre el que se aplicará el método.
     */
    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
        if(v == null)
            return;

        dfsPostOrder(accion, v.izquierdo);
        dfsPostOrder(accion, v.derecho);
        accion.actua(v);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
