import java.util.*;
import java.util.concurrent.locks.*;



class Warehouse {
    private Map<String, Product> map =  new HashMap<String, Product>();
    private Lock lock = new ReentrantLock();
    

    private class Product { 
        int quantity = 0;
        Condition cond = lock.newCondition(); 
    }

    private Product get(String item) {
        Product p = map.get(item);
        if (p != null) return p;
        p = new Product();
        map.put(item, p);
        return p;
    }

    public void supply(String item, int quantity) {
        lock.lock();
        try {
            Product p = get(item);
            p.quantity += quantity;
            p.cond.signalAll();
        } finally {
            lock.unlock();
        }
    }


    private Product areItemsAvailable(Product[] a) {
        for (Product p : a) {
           if(p.quantity == 0)
            return p;
        }
        return null;
    }



    public void consume(Set<String> items) throws InterruptedException{
        Product[] a = new Product[items.size()];
        lock.lock();
        try{
            int i = 0;
            for (String s : items) 
                a[i++] = get(s);
            
            Product p;
            while((p=areItemsAvailable(a)) != null)
                    p.cond.await();
            
                    for(Product c: a){
                        c.quantity--;
                    }
            } finally {
                lock.unlock();
            }
    } 
} 

     