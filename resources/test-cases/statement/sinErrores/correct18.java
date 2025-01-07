//[SinErrores]
class Init{
    int m1() {
        if (true) {
            return 0;
        } else {
            int i = 0;
            switch (i) {
                case 0: {
                    return 1;
                }
                case 1: {
                    return 2;
                }
                default: {
                    if (i == 2) {
                        return 3;
                    } else {
                        while (i < 10) {

                        }
                        return 4;
                    }
                }
            }
        }
    }

    static void main() { }
}
