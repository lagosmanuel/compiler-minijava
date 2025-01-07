///[SinErrores]
class Prueba1{
    int x;
    static void prueba1(int a) {
        this.b().a = 5;
    }
    public Prueba1(int y){
    }

    int ack(int m, int n) {
        if (m == 0) {
            return n + 1;
        } else if (n == 0) {
            return ack(m - 1, 1);
        } else {
            return ack(m - 1, ack(m, n - 1));
        }
    }

    private Prueba1() {
        x = 0;
    }

    int factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    int factorialTail(int n, int acc) {
        if (n == 0) {
            return acc;
        } else {
            return factorialTail(n - 1, n * acc);
        }
    }
}
