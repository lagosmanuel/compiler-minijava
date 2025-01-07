///[Error:m1|3]
class Init{
    int m1() {
        if (true) {
            return 0;
        } else {
            for (int i = 0; i < 10; i+=1) {
                if (i == 5) {
                    return 0;
                } else {
                    switch (i) {
                        case 0: {
                            if (i == 0) {
                                return 0;
                            } else {

                            }
                        }
                        case 1: {
                            return 0;
                        }
                    }
                }
            }
        }
    }
    static void main() {
    }
}
