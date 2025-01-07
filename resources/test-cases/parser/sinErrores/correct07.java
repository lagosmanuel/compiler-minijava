///[SinErrores]
abstract class Prueba1 extends Prueba2 {
    private int x = 10;
    public Lista<String> lista = new Lista<>();
    private Map<String, Integer> mapa = new HashMap<A, B>();

    static void prueba1(int a) {
        int x = 10;
        float y = 20.5;
        boolean flag = true;

        String texto = "Hola Mundo";
        Integer numero = 100;

        List<String> lista = new ArrayList<>();
        Map<String, Integer> mapa = new HashMap<A, B>();

        int a, b, c = 10;
        String str1, str2 = "Hello World";

        for (int i = 0; i < 10; i+=1) {
           x += i;
           y ++ 1;
           z /+ 3;
        }
    }

    private int prueba3() {
        for (Persona persona:personas) {
            x += persona.edad;
        }
    }

    public abstract void prueba2();

    private static void prueba4(Ciudad c, int a, int b) {
        c.nombre = "Madrid";
        c.getNombre();
        {
            var d = c.nombre;
            var e = c.getNombre();
        }
    }

    public void prueba5() {
        var c = this.nombre.a.b(1+=1, 'z', 123.456e10).c;
        var d = this.getEdad().a.b.c(1);
        var e = this.getCiudad().nombre.a.b().c;
        var f = (this.getCiudad()).getNombre().a().b.c(null);
    }

    abstract void prueba6(float a, float b, float c, Matriz<T> d);
    public abstract void prueba7();
}
