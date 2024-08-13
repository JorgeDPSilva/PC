public class G1E2 extends Thread{
    final int I;
    final int n;

    G1E2(int n, int I){
        this.n = n;
        this.I = I;
    }

    public void run(){
        for(int i = 0; i<I; i++){
            System.out.println("A Thread "+n+" escreve "+i);
        }
    }
}
