public class G1E1 extends Thread{
    final int I;
    final int n;

    G1E1(int n, int I){
        this.n = n;
        this.I = I;
    }

    public void run(){
       for(int i = 0; i<I; i++){
           System.out.println("A Thread "+n+" escreve "+i);
       }
    }
}

class Main{
    public static void main(String[] args) throws InterruptedException{
        final int N = Integer.parseInt(args[0]);
        final int I = Integer.parseInt(args[1]);
        Thread[] a = new Thread[N];


        for(int i=0;i<N; ++i){
            a[i] = new G1E1(i,I);
        }

        for(int i=0;i<N; ++i){
            a[i].start();
        }

        for(int i=0;i<N; ++i){
            a[i].join();
        }
    }
}
