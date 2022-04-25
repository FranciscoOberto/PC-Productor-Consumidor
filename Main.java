import java.util.ArrayList;

public class Main {
    public  static final int bufferSize = 100;
    public static final double tiempoDeCreacion = 0;
    public static final double tiempoDeRevision = 0;
    public static final double tiempoDeConsumision = 0;
    public static final double tiempoLog = 0;
    public static final int N_CREADORES = 4;
    public static final int N_REVISORES = 2;
    public static final int N_CONSUMIDORES = 2;

    public static void main(String[] s) {
        ArrayList<Long> times = new ArrayList<>();
        for (int i=0;i<100;i++){
            times.add(run());
            System.out.println("Fin ejecucion " + i);
        }
        System.out.println(times);
        int sum = 0;
        for (long num: times) {
            sum += num;
        }
        long mean = sum/times.size();
        System.out.println(mean);
    }

    public static long run(){
        long startTime = System.currentTimeMillis();
        Buffer bufferInicial = new Buffer(bufferSize, null);
        Buffer bufferValidado = new Buffer(bufferSize, bufferInicial);
        Log log = new Log(bufferInicial, bufferValidado, tiempoLog);

        Consumidor.totalConsumidos = 0;

        ArrayList<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < N_CREADORES; i++) {
            Creador creador = new Creador(bufferInicial, bufferValidado, (long) tiempoDeCreacion);
            Thread thread = new Thread(creador);
            threads.add(thread);
        }

        for (int i = 0; i < N_REVISORES; i++) {
            Revisor revisor = new Revisor(bufferInicial, bufferValidado, (int) tiempoDeRevision, N_REVISORES);
            Thread thread = new Thread(revisor);
            threads.add(thread);
        }

        for (int i = 0; i < N_CONSUMIDORES; i++) {
            Consumidor consumidor = new Consumidor(bufferValidado, bufferInicial, (int) tiempoDeConsumision);
            Thread thread = new Thread(consumidor);
            threads.add(thread);
        }

        for (Thread value : threads) {
            value.start();
        }

        Thread logThread = new Thread(log);
        logThread.start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }

        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }
}
