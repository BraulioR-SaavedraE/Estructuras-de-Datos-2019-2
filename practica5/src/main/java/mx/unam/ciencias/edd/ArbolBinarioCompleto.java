package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
            cola = new Cola<Vertice>();

            if(raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
            Vertice siguiente = cola.saca();

            if(siguiente.hayIzquierdo())
                cola.mete(siguiente.izquierdo);

            if(siguiente.hayDerecho())
                    cola.mete(siguiente.derecho);

            return siguiente.get();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException();

        Vertice agregado = new Vertice(elemento);

        if(super.esVacia()) {
            raiz = agregado;
            elementos++;
            return;
        }
        
        // esta manera de agregar es logarítmica en complejidad temporal.
        // Cabe recalcar que el método puede llegar a fallar si el número de elementos
        // excede la máxima capacidad de un entero long.
        Vertice auxiliar = super.raiz;
        long x = coordenadaX(getElementos());
        int y = coordenadaY();
        long x1 = x;
        int i = y;
        Pila<Integer> direcciones = new Pila<Integer>();
        direcciones = trayectoriaInversa(direcciones, x, y);
        auxiliar = trayectoria(direcciones, auxiliar, y);
        agregado.padre = auxiliar;

        if(direcciones.saca() == 0)
            auxiliar.izquierdo = agregado;
        else
            auxiliar.derecho = agregado;
        
        elementos++;
    }

    /*
     * Método que reconstruye la trayectoria de un vértice hasta la raíz.
     * @param trayectoria pila donde se guardará la trayectoria.
     * @param x coordenada x del vértice.
     * @param y coordenada y del vértice.
     * @return Pila<Integer> pila con la trayectoria.
     */
    private Pila<Integer> trayectoriaInversa(Pila<Integer> trayectoria, long x, int y) {
        int i = y;
        long x1 = x;
        while(i != 0) { // empezamos a reconstruir la trayectoria a partir de las coordenadas
            // del vértice a agregar, hasta la raíz.
            if(x1 % 2 == 1) {
                x1 = (x1 - 1) / 2;
                trayectoria.mete(1);
            }else{
                x1 /= 2;
                trayectoria.mete(0);
            }
            i--;
        }

        return trayectoria;
    }

    /* Método auxiliar que nos da el padre del vértice del cual construimos la
     * trayectoria.
     * @param trayectoria a seguir desde la raíz para llegar al padre de
     * cierto vértice.
     * @param u vértice desde el cual se inicia el recorrido (debería ser la raíz).
     * @param y coordenada y del vértice del que se quiere obtener su padre.
     * @return Vertice padre del vértice con coordenada y = <code>y</code>.
     */
    private Vertice trayectoria(Pila<Integer> trayectoria, Vertice u, int y) {
        int i = 1;
        while(i != y){
            if(trayectoria.saca() == 0)
                u = u.izquierdo;
            else
                u = u.derecho;
            i++;
        } 

        return u;
    }

    /*
     * Método auxiliar que calcula la coordenada x de un elemento en un árbol binario completo.
     * @param n número de elemento del vértice.
     * @return long coordenada x del siguiente elemento a agregar en un árbol binario
     * completo.
     */
    private long coordenadaX(int n) {
        if(n == maxElem())
            return (n + 1) - (long) Math.pow(2, altura() + 1);

        return (n + 1) - (long) Math.pow(2, altura());
    }

    /*
     * Método auxiliar que calcula el número máximo de vértices que puede
     * tener un árbol binario completo con cierta altura.
     * @return long máximo número de vértices que puede tener un árbol binario
     * completo con cierta altura.
     */
    private long maxElem() {
        return (long) (Math.pow(2, altura() + 1) - 1);
    }

    /*
     * Método auxiliar que calcula la coordenada y del siguiente vértice
     * a agregar.
     * @return int coordenada y del vértice.
     */
    private int coordenadaY() {
        if(getElementos() == maxElem())
            return altura() + 1;

        return altura();
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice eliminado = (Vertice)(super.busca(elemento));

        if(eliminado == null)
            return;

        if(super.getElementos() == 1){
            raiz = null;
            elementos--;
            return;
        }


        Vertice ultimo = ultimoVertice();
        T x = eliminado.get();
        eliminado.elemento = ultimo.get();
        ultimo.elemento = x;

        if(super.esIzquierdo(ultimo))
            ultimo.padre.izquierdo = null;
        else
            ultimo.padre.derecho = null;

        elementos--;
    }

    /*
     * Método auxiliar que nos da el último vértice agregado.
     * @return Vertice último vértice agregado
     */
    private Vertice ultimoVertice() {
        long x = coordenadaX(getElementos() - 1);
        int y = altura();
        Vertice auxiliar = raiz;
        Pila<Integer> trayectoria = new Pila<Integer>();
        trayectoria = trayectoriaInversa(trayectoria, x, y);
        auxiliar = trayectoria(trayectoria, auxiliar, y);

        int e = trayectoria.saca();

        if(e == 0)
            auxiliar = auxiliar.izquierdo;
        else
            auxiliar = auxiliar.derecho;
        return auxiliar;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
        return esVacia() ? -1 : (int) (Math.log(super.getElementos()) / Math.log(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        if(esVacia())
            return;

        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
        Vertice auxiliar;

        while(!cola.esVacia()) {
            auxiliar = cola.saca();
            accion.actua(auxiliar);
            if(auxiliar.hayIzquierdo())
                cola.mete(auxiliar.izquierdo);
            if(auxiliar.hayDerecho())
                cola.mete(auxiliar.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
