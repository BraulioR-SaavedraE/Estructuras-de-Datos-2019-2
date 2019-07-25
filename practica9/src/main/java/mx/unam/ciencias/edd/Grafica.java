package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            vecinos = new Lista<Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            // Aquí va su código.
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            // Aquí va su código.
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
            if((distancia - vertice.distancia) < 0)
                return -1;
            else if(distancia - vertice.distancia > 0)
                return 1;
            
            return 0;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            // Aquí va su código.
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            // Aquí va su código.
            return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
        vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return vertices.getLongitud();
    }
         
    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null || contiene(elemento))
            throw new IllegalArgumentException();

        vertices.agrega(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
        Vertice primero = busca(a); 
        Vertice segundo = busca(b);

        if(primero == null || segundo == null)
            throw new NoSuchElementException();

        if(primero.equals(segundo) || sonVecinos(a, b))
            throw new IllegalArgumentException();

        primero.vecinos.agrega(new Vecino(segundo, 1));
        segundo.vecinos.agrega(new Vecino(primero, 1));
        aristas++;
    }

    /*
     * Método auxiliar que busca un elemento en la gráfica y devuelve una referencia.
     * al vértice que lo contiene.
     * @param elemento a buscar.
     * @return Vertice vértice que contiene al elemento; null en caso de no encontrarse.
     */
    private Vertice busca(T elemento){
        if(elemento == null)
            return null;

        for(Vertice vertex : vertices)
            if(vertex.elemento.equals(elemento))
                return vertex;
            
        return null;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
        Vertice primero = busca(a); 
        Vertice segundo = busca(b);

        if(primero == null || segundo == null)
            throw new NoSuchElementException();

        if(primero.equals(segundo) || sonVecinos(a, b) || peso < 0)
            throw new IllegalArgumentException();

        primero.vecinos.agrega(new Vecino(segundo, peso));
        segundo.vecinos.agrega(new Vecino(primero, peso));
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
        Vertice uno = busca(a);
        Vertice dos = busca(b);

        if(uno == null || dos == null)
            throw new NoSuchElementException();

        if(!sonVecinos(a, b))
            throw new IllegalArgumentException();

        eliminaVecino(uno.vecinos, b);
        eliminaVecino(dos.vecinos, a);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        for(Vertice vertex : vertices)
            if(vertex.elemento.equals(elemento))
                return true;

        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice eliminado = busca(elemento);

        if(eliminado == null)
            throw new NoSuchElementException();

        for(Vecino vertex : eliminado.vecinos){
            eliminaVecino(vertex.vecino.vecinos, elemento);
            aristas--;
        }

        vertices.elimina(eliminado);
    }

    /*
     * Método auxiliar que elimina a un vecino dado un elemento.
     * @param vecinos -lista de vecinos de la cual será eliminada el elemento.
     * @param elemento -elemento a eliminar.
     */
    private void eliminaVecino(Lista<Vecino> vecinos, T elemento){
        for(Vecino vertex :  vecinos)
            if(vertex.vecino.elemento.equals(elemento)){
                vecinos.elimina(vertex);
                return;
            }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
        Vertice primero = busca(a);
        Vertice segundo = busca(b);

        if(primero == null || segundo == null)
            throw new NoSuchElementException();

        return busca(primero.vecinos, b) != null;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(!sonVecinos(a, b))
            throw new IllegalArgumentException();

        Vertice primero = busca(a);
        Vecino auxiliar = busca(primero.vecinos, b);

        return auxiliar.peso;
    }

    /*
     * Método auxiliar que busca un vecino dado un elemento.
     * @param vecinos lista en la que se buscará el vecino.
     * @elemento elemento a buscar.
     * @return Vecino vecino cuyo vértice vecino contiene al elemento buscado.
     * <code>null</code> si el elemento no es encontrado.
     */
    private Vecino busca(Lista<Vecino> vecinos, T elemento){
        for(Vecino vecinox : vecinos)
            if(vecinox.vecino.elemento.equals(elemento))
                return vecinox;
        
        return null;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.
        Vertice uno = busca(a);
        Vertice dos = busca(b);

        if(uno == null || dos == null)
            throw new NoSuchElementException();

        if(!sonVecinos(a, b))
            throw new IllegalArgumentException();

        Vecino first = busca(dos.vecinos, a);
        Vecino second = busca(uno.vecinos, b);
        first.peso = peso;
        second.peso = peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
        Vertice buscado = busca(elemento);
        if(buscado == null)
            throw new NoSuchElementException();

        return buscado;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        // Aquí va su código.
        if(vertice == null || (vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class)){
            throw new IllegalArgumentException();
        }

        if(vertice.getClass() == Vertice.class) {
            ((Vertice)vertice).color = color;
            return;
        }

        if(vertice.getClass() == Vecino.class)
            ((Vecino)vertice).vecino.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
        Lista<Vertice> lista = new Lista<Vertice>();
        bfs(vertices.getPrimero().get(), v -> lista.agrega((Vertice)v));

        return vertices.getLongitud() == lista.getLongitud();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        for(Vertice vertex : vertices)
            accion.actua(vertex);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        recorridoGeneral(elemento, accion, new Cola<Vertice>());
    }

    /*
     * Método auxiliar que hace lo que los métodos bfs y dfs deberían implementar,
     * pero usando instancias de MeteSaca
     * @param elemento -elemento contenido en el vértice sobre el cual queremos 
     * iniciar el recorrido
     * @param accion -acción que se le quiere aplicar a los vértices
     * @param meteSaca -instancia de la clase abstracta MeteSaca
     * @throws NoSuchElementException si el elemento no está en la gráfica
     */
    private void recorridoGeneral(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> mete){
        Vertice buscado = busca(elemento);

        if(buscado == null)
            throw new NoSuchElementException();

        paraCadaVertice(v -> setColor(v, Color.ROJO));
        setColor(buscado, Color.NEGRO);
        mete.mete(buscado);
        Vertice auxiliar;

        while(!mete.esVacia()) {
            auxiliar = mete.saca();
            accion.actua(auxiliar);
            for(Vecino vertex : auxiliar.vecinos)
                if(vertex.getColor() == Color.ROJO) {
                    setColor(vertex, Color.NEGRO);
                    mete.mete(vertex.vecino);
                }
        }

        paraCadaVertice(v -> setColor(v, Color.NINGUNO));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        recorridoGeneral(elemento, accion, new Pila<Vertice>());
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        String s = "{";

        for(Vertice vertex : vertices)
            s += vertex.get().toString() + ", ";

        s += "}, {";

        for(Vertice vertex : vertices)
            for(Vecino vecino : vertex.vecinos) 
                if(!s.contains("(" + vecino.get().toString() + ", " + vertex.get().toString() + ")"))
                    s += "(" + vertex.get().toString() + ", " + vecino.get().toString() + "), " ;
        
        s += "}";

        return s;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
        if(getAristas() != grafica.getAristas() || vertices.getLongitud() != grafica.vertices.getLongitud())
            return false;

        for(Vertice vertex : vertices)
            if(!grafica.contiene(vertex.get()))
                return false;
            else
                for(Vecino vecino : vertex.vecinos)
                    if(!grafica.sonVecinos(vertex.get(), vecino.get()))
                        return false;
         
         return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        Vertice origenVer = busca(origen);
        Vertice destinoVer = busca(destino);

        if(origenVer == null || destinoVer == null)
            throw new NoSuchElementException();

        Lista<VerticeGrafica<T>> miLista = new Lista<VerticeGrafica<T>>();

        if(origen.equals(destino)){
            miLista.agrega(origenVer);
            return miLista;
        }

        paraCadaVertice(v -> ((Vertice)v).distancia = Double.POSITIVE_INFINITY);
        origenVer.distancia = 0;
        Cola<Vertice> colilla = new Cola<Vertice>();
        colilla.mete(origenVer);
        Vertice auxiliar;

        while(!colilla.esVacia()){
            auxiliar = colilla.saca();
            for(Vecino vertex : auxiliar.vecinos)
                if(vertex.vecino.distancia == Double.POSITIVE_INFINITY){
                    vertex.vecino.distancia = auxiliar.distancia + 1;
                    colilla.mete(vertex.vecino);
                }
        }

        if(destinoVer.distancia == Double.POSITIVE_INFINITY)
            return miLista;

        if(destinoVer.distancia < Double.POSITIVE_INFINITY) {
        miLista.agrega(destinoVer);
        BuscadorCamino reconstruye;
        reconstruye = (x, y) -> {
            return x.distancia == y.vecino.distancia + 1;
        };
        miLista = reconstruye(miLista, reconstruye, origen);
        }
    
        return miLista.reversa();
    }

    /*
     * Método auxiliar que reconstruye la trayectoria de un vértice destino hasta
     * un vértice origen.
     * @param lista lista en la que se guardará la trayectoria.
     * @param busca instancia de BuscadorCamino para saber cómo reconstruir la
     * trayectoria.
     * @param origen vértice al que se quiere llegar desde <code>destino</code>.
     * @return Lista<VerticeGrafica<T>> lista con la trayectoria.
     */
    private Lista<VerticeGrafica<T>> reconstruye(Lista<VerticeGrafica<T>> lista, 
            BuscadorCamino busca, T origen) {

        Vertice destino = (Vertice)lista.get(0);

        while(!destino.get().equals(origen)) {
            for(Vecino vertex : destino.vecinos)
                if(busca.seSiguen(destino, vertex)) {
                    lista.agrega(vertex.vecino);
                    destino = vertex.vecino;
                    break;
                }
        }
        return lista;
    }       
               
    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        Vertice s = busca(origen);
        Vertice t = busca(destino);

        if(s == null || t == null)
            throw new NoSuchElementException();

        paraCadaVertice(r -> ((Vertice)r).distancia = Double.POSITIVE_INFINITY);
        s.distancia = 0;
        MonticuloDijkstra<Vertice> mon;
        Lista<VerticeGrafica<T>> miLista = new Lista<VerticeGrafica<T>>();

        if(getAristas() > ((getElementos() * (getElementos() -1)) / 2) -1)  // cuando la gráfica no es plana.
            mon = new MonticuloArreglo<Vertice>(vertices);
        else
            mon = new MonticuloMinimo<Vertice>(vertices);

        ponDistancia(mon);

        if(t.distancia == Double.POSITIVE_INFINITY)
            return miLista;
        
        if(t.distancia < Double.POSITIVE_INFINITY){
            miLista.agrega(t);
            BuscadorCamino reconstruye;
            reconstruye = (x, y) -> {
                return x.distancia == y.vecino.distancia + y.peso;
            };
            miLista = reconstruye(miLista, reconstruye, origen);
        }

        return miLista.reversa();
    }

    /* Método auxiliar que pone distancias a los vértices de una gráfica
     * a partir de un vértice origen.
     * @param monticulo instancia de MonticuloDijkstra donde se encuentran los
     * vértices a los cuales se les asignará una distancia respecto a un vértice
     * origen.
     */
    private void ponDistancia(MonticuloDijkstra<Vertice> monticulo) {
        Vertice elemento;
        while(!monticulo.esVacia()){
            elemento = monticulo.elimina();
            for(Vecino vecinox : elemento.vecinos){
                if(vecinox.vecino.distancia > elemento.distancia + vecinox.peso){
                    vecinox.vecino.distancia = elemento.distancia + vecinox.peso;
                    monticulo.reordena(vecinox.vecino);
                }
            }
        }
    }
}