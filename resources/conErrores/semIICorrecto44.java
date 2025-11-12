//[SinErrores]
class C {
    int a2;
}
class B {
    C a3;
}
class A {
    B m2() { return new B(); }
}
class Main extends A {
    void main() {
        this.m2().a3.a2 = 4;
    }
}
