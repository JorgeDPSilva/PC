import java.util.concurrent.locks.*;
import java.util.*;


class BoundedBuffer {
    private final int[] buffer;
    private final int capacity;
    private int front;
    private int rear;
    private int count;

    public BoundedBuffer() {
        this(10);
    }

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new int[capacity];
        this.front = 0;
        this.rear = 0;
        this.count = 0;
    }

    public synchronized void put(int value) {
        while (count == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buffer[rear] = value;
        rear = (rear + 1) % capacity;
        count++;
        notifyAll();
    }

    public synchronized int get() {
        while (count == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int value = buffer[front];
        front = (front + 1) % capacity;
        count--;
        notifyAll();
        return value;
    }
}
interface MatchMaker {
    BoundedBuffer waitForConsumer() throws InterruptedException;
    BoundedBuffer waitForProducer() throws InterruptedException;
}

class ex2 implements MatchMaker{
    Lock l = new ReentrantLock();
    ArrayList<Pair> producers = new ArrayList<Pair>();
    ArrayList<Pair> consumers = new ArrayList<Pair>();

    private class Pair {
       Condition cond = l.newCondition();
       BoundedBuffer b = null;
    }

    

    public BoundedBuffer waitForConsumer() throws InterruptedException {
        l.lock();
        try{
            if(consumers.size() > 0){
                Pair p = consumers.remove(0);
                p.b = new BoundedBuffer();
                p.cond.signal();
                return p.b;
            }else{
                Pair p = new Pair();
                consumers.add(p);
                while(p.b == null){
                    p.cond.await();
                }
                return p.b;
            }
        } finally{
            l.unlock();
        }
    }

    public BoundedBuffer waitForProducer() throws InterruptedException {
        l.lock();
        try{
            if(producers.size() > 0){
                Pair p = producers.remove(0);
                p.b = new BoundedBuffer();
                p.cond.signal();
                return p.b;
            }else{
                Pair p = new Pair();
                producers.add(p);
                while(p.b == null){
                    p.cond.await();
                }
                return p.b;
            }
        } finally{
            l.unlock();
        }
    }


}
    
interface Controller {
    int request_resource(int i) throws InterruptedException;
    void release_resource(int i);
}


class ex3 implements Controller{
    Lock l = new ReentrantLock();
    private int[] count_threads_resource = {0, 0};
    private final Condition[] resourceAvailable = {l.newCondition(), l.newCondition()};
    private int numero_max_threads = T;
    public int request_resource(int i) throws InterruptedException{
            l.lock();
            try{
                while(count_threads_resource[i] > numero_max_threads || count_threads_resource[1-i] > 0){
                    resourceAvailable[i].await();
                }
                count_threads_resource[i]++;
                return i;
            } finally{
                l.unlock();
            } 
            
    }

    public void release_resource(int i) {
        l.lock();
        try{
            count_threads_resource[i]--;
            resourceAvailable[i].signalAll();
        } finally{
            l.unlock();
        }
    }
}
