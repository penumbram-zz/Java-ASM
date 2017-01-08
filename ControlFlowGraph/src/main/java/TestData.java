public class TestData {

    public int coloradoStateMethod() {
        int b = 0;
        int c = 0;
        int a = 0;
        do {
            b = a + 1;
            c = c + b;
            a = b * 2;
        } while(a < 9);
        return c;
    }

    public void carnegieMellonMethod(int c, boolean e) {
        int a = 10;
        int b = 11;
        if (e) {
            a = 1;
            b = 2;
        } else {
            c = a;
            a = 4;
        }
    }

    public int homework2a() {
        int a = 0;
        int c = 5;
        int b = 3;
        b = b - 1;
        return b;
    }

    public int homework2b() {
        int a = 0;
        int b = 0;
        int c = 0;
        while(a > 9) {
            b = a + 1;
            c = c + b;
            a = b * 2;
        }
        return c;
    }


    public void test1(){
        int x = 3;
        int y = 5;

        if(x <= 2){
            if(y >= 4){
                System.out.println("x <=2 and y >= 4 Tolga");
            }else{
                System.out.println("x <= 2 and y < 4");
            }
        }else{
            if(y >= 4){
                System.out.println("x > 2 and y >= 4");
            }else{
                System.out.println("x < 2 and y < 4");
            }
        }
    }



    public void test2(){
        int x = 4;
        int y = 4;

        if(x < y){
            System.out.println("x < y");
        }else
        if(y < x){
            System.out.println("y < x");
        }else{
            System.out.println("x = y");
        }
    }



    public void test3(){
        int x = 5;
        int y = 5;

        while(x < y){
            System.out.println("x < y");
            ++x;
        }
    }

    public void test4(){
        int x = 4;
        int y = 5;

        while(x < y){
            System.out.println("x < y");
            ++x;
        }
    }

    public void test5(){
        int x = 3;
        int y = 5;

        while(x < y){
            System.out.println("x < y");
            ++x;
        }
    }





    public void IfElse(int x) {
        if (x < 3) {
            x = 6;
        } else {
            x = 7;
        }
    }

    public int IfElseIf(int y) {

        int x = 1;

        if (x == y) {
            y = 6;
        } else
        if (x == 8) {
            System.out.println("x == 7");
        } else {
            y = 1;
        }

        return x;
    }

    public int ManyIfElse(int y) {

        int x = 1;

        if (x == y) {
            y = 6;
        } else
        if (x == 8) {
            y = 7;
        } else
        if (x == 6) {
            y = 1;
        } else
        if (x == 5) {
            y = 4;
        }

        return x;
    }

    public void OnDemandFor(int x) {
        if (x > 0) {
            for (int i = 0; i < x; i++) {
                System.out.println(i + " is smaller than " + x);
            }
        } else {
            System.out.println("input was negative");
        }
    }

}