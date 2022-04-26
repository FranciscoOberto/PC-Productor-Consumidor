import java.util.ArrayList;

public class Main {
    public  static final int bufferSize = 100;
    public static final double tiempoDeCreacion = 7;
    public static final double tiempoDeRevision = 5;
    public static final double tiempoDeConsumision = 5;
    public static final double tiempoLog = 2000;
    public static final int N_CREADORES = 4;
    public static final int N_REVISORES = 2;
    public static final int N_CONSUMIDORES = 2;
    public static final int MAXIMAS_CONSUMISIONES = 1000;

    public static void main(String[] s) {

        long startTime = System.currentTimeMillis();
        Buffer bufferInicial = new Buffer(bufferSize, null);
        Buffer bufferValidado = new Buffer(bufferSize, bufferInicial);
        Log log = new Log(bufferInicial, bufferValidado, tiempoLog);


        ArrayList<Thread> threads = new ArrayList<>();

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
            Consumidor consumidor = new Consumidor(bufferValidado, (int) tiempoDeConsumision, MAXIMAS_CONSUMISIONES);
            Thread thread = new Thread(consumidor);
            threads.add(thread);
        }


        for (Thread value : threads) {
            value.start();
        }

        Thread logThread = new Thread(log);
        logThread.setDaemon(true);
        logThread.start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }

        logThread.interrupt();

        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }
}
