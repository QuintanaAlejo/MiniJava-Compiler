//Sin errores
class A {}
class Test {
    void m(A x, A y) {}
    void test() {
        m(null, null);  // ✅ válido
    }
}
