///[Error:1|12]
class Test {
    int m1() {
        int i = 0;
        switch (i) {
            case 1: {
                return 1;
            }
            case 2: {
                return 1;
            }
            case 1: {

            }
        }
    }
    public static void main() {
    }
}
