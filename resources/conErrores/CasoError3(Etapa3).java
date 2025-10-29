//[Error:f|8]

class A {
    final void f() {}
}

class B extends A {
    void f() {} // No se puede redefinir con distinta signatura
}
