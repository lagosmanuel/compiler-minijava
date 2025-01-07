///[SinErrores]
class Arbol<T> {
    T valor;
    Arbol<T> izq;
    Arbol<T> der;

    Arbol(T valor) {
        this.valor = valor;
    }

    int altura() {
        if (this == null) {
            return 0;
        } else {
            return 1 + Math.max(izq.altura(), der.altura());
        }
    }

    private boolean compararAltura(Arbol<T> otro) {
        return this.altura() == otro.altura();
    }

    static void main(String args) {
        Arbol<Integer> arbol = new Arbol<>(1);
        arbol.izq = new Arbol<>(2);
        arbol.der = new Arbol<>(3);
        arbol.izq.izq = new Arbol<>(4);
        arbol.izq.der = new Arbol<>(5);
        arbol.der.izq = new Arbol<>(6);
        arbol.der.der = new Arbol<>(7);
    }

    int compareTo(Arbol<T> otro) {
        return this.altura() - otro.altura();
    }

    T getValor() {
        return valor;
    }

    Arbol<T> getIzq() {
        return izq;
    }
}
