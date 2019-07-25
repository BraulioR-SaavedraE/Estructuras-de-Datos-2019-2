package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        // Aquí va su código.
        int residuo = llave.length % 4;
        int limite = llave.length / 4;
        int corredor = 0;
        int entero = 0;

        for(int i = 0; i < limite * 4; i = i){
            entero ^= combina(llave[i], llave[i + 1], llave[i + 2], llave[i + 3]);
            i += 4;
            corredor = i;
         }

         int sobrante = 0;

         switch(residuo){
            case 0:
            ;
            break;

            case 1:
            sobrante |= combina(llave[corredor]);
            entero ^= sobrante;
            break;

            case 2:
            sobrante |= combina(llave[corredor], llave[corredor + 1]);
            entero ^= sobrante;
            break;

            case 3:
            sobrante |= combina(llave[corredor], llave[corredor + 1], llave[corredor + 2]);
            entero ^= sobrante;
            break;
         }

         return entero;
    }

    /*
     * Método auxiliar que combina bytes para formar un entero de 32 bits.
     * El método evita que se prendan bits que no deben prenderse al hacerse la mezcla;
     * se les hará and con 0xFF,
     * @param a múltiples bytes con los que se hará la mezcla
     * @return int -entero de 32 bits resultante de la combinación.
     */
    private static int combina(byte ... a){
        int combinado = 0;

        for(int i = 0, recorrido = 24; i < a.length && recorrido >= 0; i++, recorrido -= 8) 
            combinado |= (a[i] & 0XFF) << recorrido;
        
        return combinado;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        // Aquí va su código.
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;

        int i, tamaño = llave.length;

        for(i = 0; tamaño >= 12; i += 12, tamaño -= 12) {
            a += ((llave[i] & 0xFF)   + ((llave[i + 1] & 0xFF) << 8) + ((llave[i + 2] & 0xFF)  << 16) + ((llave[i + 3] & 0xFF) << 24));
            b += ((llave[i + 4] & 0xFF) + ((llave[i + 5] & 0xFF) << 8) + ((llave[i + 6] & 0xFF)  << 16) + ((llave[i + 7] & 0xFF) << 24));
            c += ((llave[i + 8] & 0xFF) + ((llave[i + 9] & 0xFF) << 8) + ((llave[i + 10] & 0xFF) << 16) + ((llave[i + 11] & 0xFF) << 24));
            
            a -= b; a -= c; a ^= (c >>> 13);
            b -= c; b -= a; b ^= (a <<  8);
            c -= a; c -= b; c ^= (b >>> 13);
            a -= b; a -= c; a ^= (c >>> 12);
            b -= c; b -= a; b ^= (a <<  16);
            c -= a; c -= b; c ^= (b >>> 5);
            a -= b; a -= c; a ^= (c >>> 3);
            b -= c; b -= a; b ^= (a <<  10);
            c -= a; c -= b; c ^= (b >>> 15);
        }

        c += llave.length;

        switch(tamaño) {
            case 11: c += ((llave[i + 10] & 0xFF) << 24);
            case 10: c += ((llave[i + 9] & 0xFF)  << 16);
            case  9: c += ((llave[i + 8] & 0xFF)  << 8);
                
            case  8: b += ((llave[i + 7] & 0xFF)  << 24);
            case  7: b += ((llave[i + 6] & 0xFF)  << 16);
            case  6: b += ((llave[i + 5] & 0xFF)  << 8);
            case  5: b +=  llave[i + 4] & 0xFF;
                        
            case  4: a += ((llave[i + 3] & 0xFF)  << 24);
            case  3: a += ((llave[i + 2] & 0xFF)  << 16);
            case  2: a += ((llave[i + 1] & 0xFF)  << 8);
            case  1: a += llave[i] & 0xFF;
        }

        a -= b; a -= c; a ^= (c >>> 13);
        b -= c; b -= a; b ^= (a <<  8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a <<  16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a <<  10);
        c -= a; c -= b; c ^= (b >>> 15);

        return c;  
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        // Aquí va su código.
        int hash = 5381;

        for(int i = 0; i < llave.length; i++) 
            hash += (hash << 5) + (llave[i] & 0xFF);

        return hash;
    }
}
