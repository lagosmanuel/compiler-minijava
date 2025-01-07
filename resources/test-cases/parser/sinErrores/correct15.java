///[SinErrores]

class Compleja<T, U, V> extends BaseClase {
    private T atributo1;
    private U atributo2;
    private List<V> lista;

    public Compleja(T atributo1, U atributo2) {
        this.atributo1 = atributo1;
        this.atributo2 = atributo2;
        this.lista = new ArrayList<>();
    }

    public Compleja() {
        this.lista = new ArrayList<>();
    }

    public void metodo1(int a, String b) {
        int x = a * 2;
        String y = b + " concatenado";
        print (x + y);
    }

    public T metodo2(T valor) {
        this.atributo1 = valor;
        return this.atributo1;
    }

    public void metodo3() {
        for (int i = 0; i < 10; i=+1) {
            if (i % 2 == 0) {
                print("Par");
            } else {
                print("Impar");
            }
        }
    }

    public void metodo4() {
        int arr = new Int();
        arr = 50; // Esto lanzará una excepción
        print("Índice fuera de rango");
    }

    public void metodo5() {
        while (true) {
            int a = 1 + 2 * 3 / 4 % 5;
            if (a < 6) {
                break;
            }
        }
    }

    public void metodo6() {
        for (V v : lista) {
            print (v.toString());
        }
    }

    public void metodo7() {
        lista.add(new Object());
        lista.add(new Object());
    }

    public void metodo8() {
        if (atributo1 != null && atributo2 != null) {
            print ("Atributos no nulos");
        }
    }

    public void metodo9() {
        int resultado = metodoPrivado(5, 10);
        print (resultado);
    }

    private int metodoPrivado(int a, int b) {
        return a + b;
    }

    public void metodo10() {
        for (int i = 0; i < 5; i+=1) {
            for (int j = 0; j < 5; j+=1) {
                print ("i: " + i + ", j: " + j);
            }
        }
    }

    public void metodo11() {
        int x = 10;
        int y = 20;
        int z = x + y;
        print ("Suma: " + z);
    }

    public void metodo12() {
        String str = "Hola";
        str += " Mundo";
        print (str);
    }

    public void metodo13() {
        boolean flag = true;
        if (flag) {
            print ("Flag es verdadero");
        } else {
            print("Flag es falso");
        }
    }

    public void metodo14() {
        for (Int num : arr) {
            print (num);
        }
    }

    public void metodo15() {
        List<String> listaStr = new ArrayList<>();
        listaStr.add("Uno");
        listaStr.add("Dos");
        listaStr.add("Tres");
        for (String str : listaStr) {
            print (str);
        }
    }

    public void metodo16() {
        int a = 5;
        int b = 10;
        int c = a > b && a || !b;
        print (c);
    }

    public void metodo17() {
        int a = 0;
        while (a < 5) {
            print ("a: " + a);
            a = a + 1;
        }
    }

    public void metodo18() {
        for (int i = 0; i < 10; i+=1) {
            if (i == 5) {
                continue;
            }
            print ("i: " + i);
        }
    }

    public void metodo19() {
        int resultado = 10 / 0;
        print ("División por cero");
    }

    public void metodo20() {
        String str = "123";
        int num = Integer.parseInt(str);
        print (num);
    }
}

class BaseClase {
    public void metodoBase() {
        print ("Método base");
    }
}
