class Parent  {
    static int x  = 2;
    static public void print ()  {
        System.out.println (x);
    }
}
class Child extends Parent  {
    static int x  = 3;
    public static void main (String [] args)  {
        Parent c  = new Child ();
        c.print ();
    }
}