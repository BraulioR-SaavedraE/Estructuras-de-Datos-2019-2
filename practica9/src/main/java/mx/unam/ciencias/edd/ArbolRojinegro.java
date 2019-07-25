package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            // Aquí va su código.
            if (color == Color.ROJO)
                return "R{" + super.elemento.toString() + "}";

            return "N{" + super.elemento.toString() + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
                return (color == vertice.color && super.equals(vertice));
                // Verificar que no se pueda dar el caso los descendientes
                // tengan diferente color y el método regrese true de todas
                // maneras.
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        if(vertice.getClass() != VerticeRojinegro.class)
            throw new ClassCastException();

        return ((VerticeRojinegro)vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeRojinegro agregado = (VerticeRojinegro)ultimoAgregado;
        agregado.color = Color.ROJO;
        rebalanceaAgrega(agregado);
    }

    /*
     * Método auxiliar recursivo que rebalancea un árbol Rojinegro después de 
     * agregar un vértice.
     * @param vertice vértice a partir del cual sea hará el rebalanceo.
     */
    private void rebalanceaAgrega(VerticeRojinegro vertice) {
        if(!vertice.hayPadre()) {   // Caso 1: no hay padre.
            vertice.color = Color.NEGRO;
            return;
        }

        VerticeRojinegro p = (VerticeRojinegro)vertice.padre;

        if(!esRojo(p))     // Caso 2: el padre es negro.
            return;

        VerticeRojinegro a = (VerticeRojinegro)p.padre;
        VerticeRojinegro t = hermano(p);

        if(esRojo(t)) {    // Caso 3: el tío es rojo.
            t.color = Color.NEGRO;
            p.color = Color.NEGRO;
            a.color = Color.ROJO;
            rebalanceaAgrega(a);
            return;
        }

         if(estanCruzados(p, vertice)) {    // Caso 4: el vértice y su padre están cruzados.
            if(esIzquierdo(p))
                super.giraIzquierda(p);
            else
                super.giraDerecha(p);

            VerticeRojinegro auxiliar = vertice;
            p = auxiliar;
            vertice = p;
        }

        p.color = Color.NEGRO;
        a.color = Color.ROJO;

        if(esIzquierdo(vertice))   // Caso 5: continuación del caso 4 o si los vértices no están cruzados.
            super.giraDerecha(a);
        else 
            super.giraIzquierda(a);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeRojinegro eliminado = (VerticeRojinegro)(busca(elemento));
        VerticeRojinegro fantasma = null;

        if(eliminado == null)
            return;

        if(eliminado.hayIzquierdo() && eliminado.hayDerecho()) 
            eliminado = (VerticeRojinegro)intercambiaEliminable(eliminado);
        
        if(!eliminado.hayIzquierdo() && !eliminado.hayDerecho()) {
            fantasma = new VerticeRojinegro(null);
            fantasma.color = Color.NEGRO;
            fantasma.padre = eliminado;
            eliminado.izquierdo = fantasma;
        }

        VerticeRojinegro h;

        if(eliminado.hayIzquierdo()) 
            h = (VerticeRojinegro)eliminado.izquierdo;
        else
            h = (VerticeRojinegro)eliminado.derecho;

        eliminaVertice(eliminado);

        tresOpciones: { // Posibilidades con respecto a los colores de v y h.
            if(esRojo(h)) {
                h.color = Color.NEGRO;
                break tresOpciones;
            }
            if(esRojo(eliminado))
            ;
            if(!esRojo(eliminado) && !esRojo(h))
            rebalanceaElimina(h);
        };

        mataFantasma(fantasma);
        }

    /* Método auxiliar recursivo que rebalancea un árbol rojinegro después de
     * haber eliminado un vértice.
     * @param v vértice a partir del cual se hará el rebalanceo.
     */
    private void rebalanceaElimina(VerticeRojinegro v) {
        if(!v.hayPadre())  // Caso 1: v no tiene padre.
            return;

        VerticeRojinegro p = (VerticeRojinegro)v.padre;
        VerticeRojinegro h = hermano(v);

        if(esRojo(h)) { // Caso 2: h es rojo y p negro.
            p.color = Color.ROJO;
            h.color = Color.NEGRO;

            if(esIzquierdo(v)) 
                super.giraIzquierda(p);
            else
               super.giraDerecha(p);

           p = (VerticeRojinegro)v.padre;
           h = hermano(v);
        }

        VerticeRojinegro i = (VerticeRojinegro)h.izquierdo;
        VerticeRojinegro d = (VerticeRojinegro)h.derecho;
        
        if(!esRojo(p) && !esRojo(h) && !esRojo(i) && !esRojo(d)) {  // Caso 3: p, h, i y d son negros.
            h.color = Color.ROJO;
            rebalanceaElimina(p);
            return;
        }

        if(esRojo(p) && !esRojo(h) && !esRojo(i) && !esRojo(d)) {   // Caso 4: h, i y d son negros y p rojo.
            h.color = Color.ROJO;
            p.color = Color.NEGRO;
            return;
        }

        if((esIzquierdo(v) && esRojo(i) && !esRojo(d)) || (!esIzquierdo(v) && !esRojo(i) && esRojo(d))) {   // Caso 5:
            // v es izquierdo, i rojo y d negro, o v derecho, i negro y d rojo.
            h.color = Color.ROJO;

            if(esRojo(i))
                i.color = Color.NEGRO;
            else
                d.color = Color.NEGRO;
            
            if(esIzquierdo(v))
                super.giraDerecha(h);
            else
                super.giraIzquierda(h);

            h = hermano(v);
            i = (VerticeRojinegro)h.izquierdo;
            d = (VerticeRojinegro)h.derecho;    
        }

        h.color = getColor(p); // Caso 6: v es izquierdo y d rojo o v es derecho y i rojo.
        p.color = Color.NEGRO;

        if(esIzquierdo(v))
            d.color = Color.NEGRO;
        else
            i.color = Color.NEGRO;

        if(esIzquierdo(v))
            super.giraIzquierda(p);
        else
            super.giraDerecha(p);
    }

    /* Método auxiliar que determina si un vértice y su hijo están cruzados, es decir,
     * tienen diferente dirección.
     * @param padre vértice padre.
     * @param hijo vértice hijo.
     * @return <code>true</code> si los vértices están cruzados, <code>false</code> en otro caso.
     */ 
    private boolean estanCruzados(VerticeRojinegro padre, VerticeRojinegro hijo) {
        return (esIzquierdo(padre) && !esIzquierdo(hijo) || !esIzquierdo(padre) && esIzquierdo(hijo));
    }

    /*
     * Método auxiliar que determina si un vértice es rojo.
     * @param vertice vértice del cual se quiere saber si es rojo.
     * @return <code<true</code> si el vértice es rojo, <code>false</code>
     * en otro caso.
     */
    private boolean esRojo(VerticeRojinegro vertice) {
        return (vertice != null && vertice.color == Color.ROJO);
    }

    /*
     * Método auxiliar que nos da el hermano de un vértice
     * @param vertice vértice del cual se quiere obtener el hermano.
     * @return VerticeRojinegro hermano del vértice.
     */
    private VerticeRojinegro hermano(VerticeRojinegro vertice) {
        if(esIzquierdo(vertice))
            return (VerticeRojinegro)vertice.padre.derecho;

        return (VerticeRojinegro)vertice.padre.izquierdo;
    }

    /*
     * Método auxiliar que elimina un vértice fantasma.
     * @param fantasma vértice fantasma que se quiere eliminar.
     */
    private void mataFantasma(VerticeRojinegro fantasma){
        if(fantasma != null)
            if(!fantasma.hayPadre())
                raiz = fantasma = null;
            else if(esIzquierdo(fantasma))
                    fantasma.padre.izquierdo = null;
                else
                    fantasma.padre.derecho = null;       
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
