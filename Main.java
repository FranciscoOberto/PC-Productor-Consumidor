public class Main {
    public  static final int bufferSize = 100;
    public static final int tiempoDeCreacion = 0;
    public static final int tiempoDeRevision = 0;
    public static final int tiempoDeConsumision = 0;
    public static final int N_CREADORES = 4;
    public static final int N_REVISORES = 2;
    public static final int N_CONSUMIDORES = 2;


    public static void main(String[] s){
        Buffer bufferInicial = new Buffer(bufferSize);
        Buffer bufferValidado = new Buffer(bufferSize);

        for(int i=0; i < N_CREADORES; i++){
            Creador creador = new Creador(bufferInicial, tiempoDeCreacion);
            Thread thread = new Thread(creador);
            thread.start();
        }

        for(int i=0; i < N_REVISORES; i++){
            Revisor revisor = new Revisor(bufferInicial, bufferValidado, tiempoDeRevision, N_REVISORES);
            Thread thread = new Thread(revisor);
            thread.start();
        }

        for(int i=0; i < N_CONSUMIDORES; i++){
            Consumidor consumidor = new Consumidor(bufferValidado, bufferInicial, tiempoDeConsumision);
            Thread thread = new Thread(consumidor);
            thread.start();
        }
    }
}
