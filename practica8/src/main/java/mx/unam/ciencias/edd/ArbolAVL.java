package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
            return super.toString() + " " + altura() + "/" + getBalance(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            // Aquí va su código.
            return (altura() == vertice.altura() && super.equals(objeto));
        }
    }

    /* Convierte el vértice a VerticeAVL */
    private VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        rebalancea(verticeAVL(ultimoAgregado.padre));
    }

    /*
     * Método auxiliar que calcula el balancea de un vértice AVL, es decir,
     * Nos da el entero resultante de la diferencia de la altura de los
     * hijos del vértice.
     * @param vertice vértice del cual se quiere conocer el balance.
     * @return int balance del vértice.
     */
    private int getBalance(VerticeAVL vertice) {
        if(vertice == null)
            return 0;

        return getAltura(verticeAVL(vertice.izquierdo)) - getAltura(verticeAVL(vertice.derecho));
    }

    /* 
     * Método auxiliar que calcula la altura de un vértice AVL con ayuda del método altura()
     * definido en la clase VerticeAVL.
     * @param vertice vértice del cual se quiere conocer la altura.
     * @return int altura del vértice.
     */
    private int getAltura(VerticeAVL vertice) {
        if(vertice == null)
            return -1;

        if(!vertice.hayIzquierdo() && !vertice.hayDerecho())
            return 0;

        if(vertice.hayIzquierdo() && vertice.hayDerecho())
            return 1 + Math.max(verticeAVL(vertice.izquierdo).altura(), verticeAVL(vertice.derecho).altura());

        if(vertice.hayIzquierdo())
            return 1 + verticeAVL(vertice.izquierdo).altura();

        return 1 + verticeAVL(vertice.derecho).altura();
    }

    /*
     * Método auxiliar que rebalancea un árbol a partir de un vértice.
     * @param v vértice desde el cual se quiere iniciar el rebalanceo.
     */
    private void rebalancea(VerticeAVL v) {
        if(v == null)
            return;

        v.altura = getAltura(v);
        int balance = getBalance(v);

        if(balance == -2) {
            VerticeAVL q = verticeAVL(v.derecho);
            VerticeAVL x = verticeAVL(q.izquierdo);
            VerticeAVL y = verticeAVL(q.derecho);

            if(getBalance(q) == 1) {
                super.giraDerecha(q);
                
                q.altura = getAltura(q);
                if(x != null)
                    x.altura = getAltura(x);
                v.altura = getAltura(v);
                q = verticeAVL(v.derecho);
                x = verticeAVL(q.izquierdo);
                y = verticeAVL(q.derecho);
            }

            super.giraIzquierda(v);
            v.altura = getAltura(v);
            q.altura = getAltura(q);
        }else if(balance == 2) {
            VerticeAVL p = verticeAVL(v.izquierdo);
            VerticeAVL x = verticeAVL(p.izquierdo);
            VerticeAVL y = verticeAVL(p.derecho);

            if(getBalance(p) == -1) { 
                super.giraIzquierda(p);
               
                p.altura = getAltura(p);
                y.altura = getAltura(y);
                p = verticeAVL(v.izquierdo);
                x = verticeAVL(p.izquierdo);
                y = verticeAVL(p.derecho);
                
            }

            super.giraDerecha(v);
            v.altura = getAltura(v);
            p.altura = getAltura(p);
        }

        rebalancea(verticeAVL(v.padre));
    }    

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeAVL eliminado = verticeAVL(busca(elemento));

        if(eliminado == null)
            return;

        if(eliminado.hayIzquierdo() && eliminado.hayDerecho()) {    // si el vértice tiene dos hijos.
            // se hace este caso aparte dado que el método eliminaVertice() no puede resolverlo bien.
            VerticeAVL intercambio = verticeAVL(intercambiaEliminable(eliminado));
            eliminaVertice(intercambio);
            eliminado = intercambio;      
        }else
             eliminaVertice(eliminado);

        rebalancea(verticeAVL(eliminado.padre));
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
