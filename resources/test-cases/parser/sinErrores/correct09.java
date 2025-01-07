///[SinErrores]
class Prueba1 extends Prueba2 {
    int x = null;

    public Prueba1(int y) {
        if (x > 0) {
            x = 0;
            break;
        } else if (x < 0) {
            x = 1;
            break;
        } else {
            x = 2;
            for (int i = 0; i < 10; i-=1) {
                x = 3;
                break;
            }
            break;
        }

        switch (x) {
            case false: {
                x = 1;
                break;
                x = 1;
            }
            case 'a': {
                x = 2;
                break;
                x = 1;
            }
            default: {
                x = 0;
            }
        }
    }

    public void prueba1(int a, int b, char c, boolean d, Lista<T> e) {
        prueba1(1, 2, 'a', true, new Lista<T>());
    }


    public static void testAllFeatures() {
        int a = 5;
        int b = 10;
        boolean c = true;
        String d = "Hola";

        int e = a + b * (a - b) / a;
        boolean f = (a > b) && (b < e) || (a == 5);

        if (a > 0) {
            print ("a es positivo");
        } else if (a < 0) {
            print ("a es negativo");
        } else {
            print ("a es cero");
        }

        switch (a) {
            case 1: {
                print ("a no es primo");
                break;
            }
            case 2: {
                print ("a es primo");
                break;
            }
            case 3: {
                print ("a es primo");
                break;
            }
        }

        for (int i = 0; i < 10; i+=1) {
            print ("i: " + i);
        }

        int i = 0;
        while (i < 10) {
            print ("i: " + i);
            i++1;
        }

        i = 0;
        while (i < 10) {
            print ('z' + !foo(42));
            i += 1;
        }

        Prueba1<Integer, String, Double> prueba = new Prueba1<>(1, "Hola", 2.0);
        prueba.metodoInstancia();
        Prueba1.metodoEstatico();
        print (prueba.metodoGenerico(1, "Hola", 2.0));
    }
}
