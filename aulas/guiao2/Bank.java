package guiao2;

public class Bank {

    private static class Account {
        private int balance;
        
        Account(int balance) { 
            this.balance = balance; 
        }
        
        int balance() { 
            return balance; 
        }
        
        boolean deposit(int value) {
            balance += value;
            return true;
        }
        
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    //Bank slots and vector of accounts
    private final int slots;
    private Account[] av; 

    public Bank(int n) {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    //Account balance
    public synchronized int balance(int id) {
        if (id < 0 || id >= slots)
            return 0;
        return av[id].balance();
    }

    //Deposit
    public synchronized boolean deposit(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        return av[id].deposit(value);
    }

    //Withdraw; fails if no such account or insufficient balance
    public synchronized boolean withdraw(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        return av[id].withdraw(value);
    }

    public synchronized boolean transfer (int from , int to , int value){
        return withdraw(from,value) && deposit(to,value);
    }

    public  int totalBalance(){
        return totalBalnceRec(0);
    }

    public int totalBalnceRec(int i){
        
        if(i<slots){
            synchronized(av[i]){
                return totalBalnceRec(i+1);
            }
        }else {
            int total = 0;
            for(int j=0; j< slots; j++)
                total+=balance(j);
            return total;
        }
    }
}
