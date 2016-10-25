class A{
    A(String s){
        System.out.println(s);
    }
}

class E{
    {a2 = new A("a2");}
    A a1 = new A("a1");
    A a2;
}

class F extends E{
    A a3 = new A("a31");
    {a3 = new A("a32");}
    A a4;
    F(){
        this(5);
    }
    F(int i){
        a4 = new A("a4");
    }

    public static void main(String[] args){
        F f = new F();
    }
}
