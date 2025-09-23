///[SinErrores]
// Prueba de una interfaz que hereda de otra

interface Calculadora {
    int dividir(int a, int b);
}

interface Avanzada extends Calculadora {
    int potencia(int base, int exponente);
}
