import java.util.concurrent.TimeUnit;

public class Consumidor implements Runnable {

    private static int totalConsumidos = 0;
    private int demoraConsumidor;
    private Buffer bufferValidado;
    private Buffer bufferInicial;
    private int cantidadConsumidos;
    private static final int MAXIMAS_CONSUMISIONES = 50;

    /** Constructor con parametros  */
    public Consumidor(Buffer bufferValidado, Buffer bufferInicial,int demoraConsumidor) {
        this.demoraConsumidor = demoraConsumidor;
        this.bufferInicial = bufferInicial;
        this.bufferValidado = bufferValidado;
        cantidadConsumidos =  0 ;
    }

    public synchronized void aumentarConsumisiones() throws InterruptedException{
        synchronized (this) {
            totalConsumidos++;
        }
    }

    public static synchronized int getTotalConsumidos(){
        return totalConsumidos;
    }

    public static int getMaximasConsumisiones(){
        return MAXIMAS_CONSUMISIONES;
    }

    public void consumir(){
        try {
            TimeUnit.SECONDS.sleep(this.demoraConsumidor);
            boolean consumido = bufferValidado.consumirDato();
            //if (dato == null)
            //    return;
            //int id = dato.getId();
            //bufferInicial.BorrarDato(id);
            //bufferValidado.BorrarDato(id);
            if (consumido) {
                cantidadConsumidos++;
                aumentarConsumisiones();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        while(totalConsumidos< MAXIMAS_CONSUMISIONES) {
            try {
                consumir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println("Consumidor: " + getTotalConsumidos());
        System.out.println("Consumidor: " + cantidadConsumidos);
    }

}
