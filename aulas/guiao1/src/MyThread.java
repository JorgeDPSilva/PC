class MyThread extends Thread {
    public void run(){
        try{
            Thread.sleep(1000);
            System.out.println("Na Thread1");
        } catch (Exception ignored){}

    }
}

class MyRunnable implements Runnable {
    public void run(){
        try{
            Thread.sleep(1000);
            System.out.println("Na Thread2");
        } catch (Exception ignored){}

    }
}
/*class Main{
    public static void main(String[] args) throws InterruptedException{
        MyThread t = new MyThread();
        t.start();

        Thread t2 = new Thread(new MyRunnable());
        t2.start();

        System.out.println("No Main 1");

        t.join();
        t2.join();

        System.out.println("No Main fim");
    }
}*/