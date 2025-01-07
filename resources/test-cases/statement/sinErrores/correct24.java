//[SinErrores]
class Padre {
    private int x = 10;
}

class Hijo extends Padre {
    public int x = 20;

}

class Test {
    public static void main() {
        Hijo h = new Hijo();
        h.x = 0;
        Padre p = h;
        //p.x = 0; // esto sería un error
    }
}
