///[SinErrores]
// Referencia encadenada con llamadas intermedias

class A {
  A f(int v) { return this; }
  B g() { return new B(); }
}
class B {
  int h() { return 1; }
}
class C {
  void m() {
    var x = new A();
    var r = x.f(1).g().h();
  }
}
