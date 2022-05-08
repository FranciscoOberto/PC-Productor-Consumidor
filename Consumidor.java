import java.util.concurrent.TimeUnit;

public class Consumidor implements Runnable {

    private static int totalConsumidos = 0;
    private final int demoraConsumidor;
    private final Buffer bufferValidado;
    private int cantidadConsumidos;
    private static int MAXIMAS_CONSUMISIONES;
    private static final Object controlConsumisiones = new Object();

    /** Constructor con parametros  */
    public Consumidor(Buffer bufferValidado, int demoraConsumidor, int MAXIMAS_CONSUMISIONES) {
        this.demoraConsumidor = demoraConsumidor;
        this.bufferValidado = bufferValidado;
        cantidadConsumidos =  0;
        this.MAXIMAS_CONSUMISIONES = MAXIMAS_CONSUMISIONES;
    }

    public static void aumentarConsumisiones() {
        synchronized (controlConsumisiones){
            totalConsumidos++;
        }
    }

    public static int getTotalConsumidos(){
        synchronized (controlConsumisiones){
            return totalConsumidos;
        }
    }

    public static int getMaximasConsumisiones(){
        return MAXIMAS_CONSUMISIONES;
    }

    public void consumir() throws InterruptedException{
        if (this.bufferValidado.estaVacio())
            return;
        try {
            TimeUnit.MILLISECONDS.sleep(this.demoraConsumidor);
            synchronized (controlConsumisiones) {
                if (totalConsumidos >= MAXIMAS_CONSUMISIONES)
                    throw new InterruptedException();
                boolean consumido = bufferValidado.consumirDato();
                if (consumido) {
                    cantidadConsumidos++;
                    aumentarConsumisiones();
                }
            }
        }catch (InterruptedException e){
            throw new InterruptedException();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        do {
            try {
                consumir();
            } catch (InterruptedException e) {

            }
        } while (getTotalConsumidos() < MAXIMAS_CONSUMISIONES);
        //System.out.println(" Total consumidos = "+ getTotalConsumidos());
        System.out.println("Consumidor: consumidos = " + cantidadConsumidos + " Total consumidos = "+ getTotalConsumidos());
    }

}
