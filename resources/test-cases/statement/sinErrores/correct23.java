//[SinErrores]
class Test {
    public static void main() {
        Animal a = new Perro(); // Válido
        a.hacerSonido(); // Llama al método concreto en Perro
    }
}


abstract class Animal {
    abstract void hacerSonido();
}

class Perro extends Animal {
    void hacerSonido() { }
}
