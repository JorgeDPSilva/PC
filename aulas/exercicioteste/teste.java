import java.util.*;


interface Jogo {
    Partida participa();
}

interface Partida {
    String adivinha(int n);
}


class jogoImpl implements Jogo {
    private int numero_jogadores = 0;
    private PartidaImpl partida = new PartidaImpl();

    public  Partida participa() {
        numero_jogadores++;
        PartidaImpl p = partida;
        if(numero_jogadores == 4){
            notifyAll();
            partida = new PartidaImpl();
            numero_jogadores = 0;
            new Thread(() -> {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ignored) {}
                p.timeout();
            }).start();
        } else{
            while(p == partida){
                    wait();
            }
        }
        return p;
    }
}

class PartidaImpl implements Partida {
    private  int LIMITE_TENTATIVAS = 100;
    private  int NUMERO_SECRETO = (int) (Math.random() * 100) + 1;
    private int numeroTentativas = 0;
    boolean timeout = false;
    boolean ganhou = false;

    synchronized void timeout(){
        timeout = true;
    }

    public synchronized String adivinha(int n) {
        numeroTentativas++;
        if (ganhou) {
            return "PERDEU";
        }
        if (timeout) {
            return "TEMPO ESGOTADO";
        }
       
        if (numeroTentativas > LIMITE_TENTATIVAS) {
            return "PERDEU POR TENTATIVAS";
        }
        if(n==NUMERO_SECRETO){
            ganhou = true;
            return "GANHOU";
        }
        if(NUMERO_SECRETO<n){
            return "MENOR";
        }
        return "MAIOR";
    }
}
