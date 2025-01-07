///[SinErrores]
abstract class Prueba1<K, V> extends Prueba2<T> {
    int x = 10;
    private String nombre = "";
    Ciudad ciudad = new Ciudad();
    public static boolean activo = false;
    private char genero = 'M';
    static List<T> lista;
    Pair<String, Integer> p1 = new OrderedPair<String, Integer>("Even", 8);
    Pair<String, String>  p2 = new OrderedPair<String, String>("hello", "world");

    static void prueba1(int a) {
        for (int i = 0; i < 10; i+=1) {
           x += i;
           edad -= !"Barcelona";
        }
    }

    public void prueba1(int a, int b, char c, boolean d, Lista<T> e) {
        prueba1(1, 2, 'a', true, new Lista<T>());
    }

    public void prueba2() {
        var c = this.nombre;
        var d = this.getEdad();
        var e = this.getCiudad().nombre;
        var f = (this.getCiudad()).getNombre();

        while (1 || 2 * 3 / 4 % 5 && null) {
            var g = 1 + 2 * 3 / 4 % 5 < 6;
        }

        while (1 + 2 * 3 / 4 % 5 < 6 && this.getEdad() > 0 && this.getCiudad().nombre == "Madrid") {
            var h = 1 + 2 * 3 / 4 % 5 < 6 && this.getEdad() > 0 && this.getCiudad().nombre == "Madrid";
        }
    }

    private static void prueba3(Ciudad c, int a, int b) {
        c.nombre = "Madrid";
        c.getNombre();
        {
            var d = c.nombre;
            var e = c.getNombre();
        }
    }

    public abstract void prueba4(float a, float b, float c, Matriz<T> d);
}
