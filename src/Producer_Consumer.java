import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ProducerConsumer<T> {
    private LinkedList<Object> list = new LinkedList<>();
    private final static int producerMax = 10;
    private Object object = new Object();
    private synchronized void Produce(){
        T t = null;
        while(true){
            while (GetList() >= producerMax) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add(t);
            System.out.println("生产者"+Thread.currentThread().getName()+"生产了"+GetList());
            this.notifyAll();
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    private synchronized T Consume(){
        T t =null;
        //while(true){
            while (GetList()==0){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("消费者"+Thread.currentThread().getName()+"把产品"+list.getFirst()+"消费了一个还剩"+GetList());
            list.removeFirst();
            this.notifyAll();

        return t;
        //}
    }
    private int GetList(){
        return list.size();
    }
    public static void main(String[] args){
        ProducerConsumer<String> producerConsumer = new ProducerConsumer<>();
        List<Thread> threads_C = new ArrayList<Thread>();
        List<Thread> threads_P = new ArrayList<Thread>();
        int Comsumer_MaxNum = 10;
        int Producer_MaxNum = 2;
        while(threads_P.size()<=Producer_MaxNum){
            threads_P.add(new Thread(producerConsumer::Produce,""+threads_P.size()));
        }
        threads_P.forEach(o->o.start());

        while(true){
            if (threads_C.size()<Comsumer_MaxNum){
                threads_C.add(new Thread(producerConsumer::Consume,""+threads_C.size()));
                threads_C.get(threads_C.size()-1).start();

            }


            for(int i=0;i<threads_C.size();i++){

                if (!threads_C.get(i).isAlive()){
                    //System.out.println("Thread中有"+threads_C.size()+"个线程;");
                    threads_C.remove(i);
                    //System.out.println("Thread移除了"+i+";");
                    //System.out.println("Thread中有"+threads_C.size()+"个线程;");
                }
            }
        }


    }
}
