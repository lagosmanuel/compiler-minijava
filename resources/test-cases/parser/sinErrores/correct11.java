///[SinErrores]
class Prueba1 {
    private Prueba1() {
        int x = 10;
    }
    int prueba1(int a, int b, int c) {
        int x = a + b - c * d / e % f + g - h * i / j;
        return x;
    }
    Lista<Integer> prueba2(int a, int b, int j) {
        Lista<Integer> l = new Lista<Integer>();
        l.add(a + b - c * d / e % f + g - h * i / j);
        return l;
    }
    void prueba3(int a, int b, int j) {
        int x = a + b - c * d / e % f + g - h * i / j;
        prueba(1, 'c', true, 1.0, 1.0, new Lista<Integer>());
        int y = a + b - c * d / e % f + g - h * i / j;
    }
    void prueba4(int a, int b, int j) {
        int x = a + b - c * d / e % f + g - h * i / j;
        if (a > 0) {
            print("a es positivo");
        } else if (a < 0) {
            print("a es negativo");
        } else {
            return (
                a + b - c * d / e % f + g - h * i / j
                || a == 5 || b < e || a > null && b < e
            );
        }
        int y = a + b - c * d / e % f + g - h * i / j;
    }
}
