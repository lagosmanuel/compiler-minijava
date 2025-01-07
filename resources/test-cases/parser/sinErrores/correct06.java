///[SinErrores]
class Prueba1<T, L> extends Prueba2 {
    List<T> lista;
    static void prueba1(int a) {
        this.b().a = 5;
        L prueba = new L();
    }
    T prueba2() {
        return lista.get(0);
    }
    public Prueba1(int y) {
        lista = new ArrayList<>();
    }
    public Prueba1() {
        lista = new ArrayList<T, L>();
    }

    // a function that solves a non-linear equation with the Newton-Raphson method
    float newtonRaphson(float x0, float epsilon) {
        float x = x0;
        float fx = f(x);
        float dfx = df(x);
        while (Math.abs(fx) > epsilon) {
            x = x - fx / dfx;
            fx = f(x);
            dfx = df(x);
        }
        return x;
    }

    // a function that aproximates a function with a Taylor polynomial
    float taylor(float x, int n) {
        float sum = 0;
        for (int i = 0; i < n; i+=1) {
            sum += Math.pow(x, i) / factorial(i);
        }
        return sum;
    }

    void prueba3() {
        for (T t: lista) {
            t = (a = b);
            for (int i = 0; i < 10; i+=1) {
                t = (a = b);
                while (true) {
                    if (true) {
                        t = (a = b);
                    }
                }
            }
        }
    }
}
