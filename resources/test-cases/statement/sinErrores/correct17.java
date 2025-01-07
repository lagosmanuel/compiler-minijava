//[SinErrores]
class Init{
    static void main() {
        int x = 5;

        if (x < 0) {
            x = 0;
        } else {
            x += 1;
        }

        while (x > 0) {
            x -= 1;
            if (x == 0) break;
        }

        String message = "Hello";
        switch (message) {
            case "Hello": {
                x = 1;
                break;
            }
            case "Bye": {
                x = 2;
                break;
            }
            default: {
                x = 3;
            }
        }

        for (int i = 0; i < 10; i+=1) {
            x += i;
            if (x == 5) break;
        }

        while (x < 10) {
            if (x == 5) {
                x += 1;
            } else if (x == 6) {
                x += 2;
            } else {
                if (x == 7) {
                    x += 3;
                } else {
                    break;
                }
            }
        }
    }
}
