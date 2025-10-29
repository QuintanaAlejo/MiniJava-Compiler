//Caso de error de etapa anterior donde se lanzaban
//errores con métodos abstractos, solicitando que sean implementados en la clase abstracta en la que son inicialmente declarados.

abstract class A {
    abstract void m1();
}

abstract class B extends A {
    // No implementa m1(), pero está bien porque B también es abstracta
}