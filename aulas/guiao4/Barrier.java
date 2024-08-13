import java.util.*;

class Barrier {
private static class Etapa {} 
private int N;
private int count;
//private int phase;
private Etapa current = new Etapa();


    Barrier (int N) {
        this.N = N;
        this.count = 0;           
    }
    
    public synchronized void await() throws InterruptedException { 
        //int currentPhase = phase;
        Etapa e = current;
        count++;
       
        if(count == N){
            notifyAll();
            //phase++; 
            current = new Etapa();
            count = 0;
        }else {
            while(current == e ){ 
                wait();
            } 
        }
        
}

public static void main(String[] args) {
        final int numberOfThreads = 3;
        final int numberOfPhases = 5;

        Barrier barrier = new Barrier(numberOfThreads);

        for (int i = 0; i < numberOfPhases; i++) {
            for (int j = 0; j < numberOfThreads; j++) {
                new Thread(() -> {
                    try {
                        System.out.println("Thread started");
                        barrier.await();
                        System.out.println("Thread finished");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            try {
                // Aguarda um pouco antes de começar a próxima fase
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Starting new phase\n");
        }
    }
}

