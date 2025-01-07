///[SinErrores]
class Prueba1 extends Prueba2 {
    public void prueba1(int a, int b, char c, boolean d, Lista<T> e) {
        prueba1(1, 2, 'a', true, new Lista<T>());
    }

    private void complexSwith() {
        switch (value) {
            case 1: {
                print("Case 1");
                break;
            }
            case 2: {
                print("Case 2");
                switch (option) {
                    case 'a': {
                        print("Nested case 'a'");
                        break;
                    }
                    case 'b': {
                        print("Nested case 'b'");
                        break;
                    }
                    default: {
                        print("Nested default case");
                        break;
                    }
                }
                break;
            }
            case 3: {
                print ("Case 3");
                if (option == 'x') {
                    print ("Option is 'x'");
                } else {
                    print ("Option is not 'x'");
                }
                break;
            }
            default: {
                print ("Default case");
                break;
            }
        }
    }
}
