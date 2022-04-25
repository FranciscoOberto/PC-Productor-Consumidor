import java.util.concurrent.TimeUnit;

public class Creador implements Runnable{
    private Buffer bufferInicial;
    private Buffer bufferValidado;
    private int cantidadCreados;
    private final long demora;
    private static int totalCreados = 0;

    /**
     * Constructor con parámetros
     * Inicializa las variables de instancia
     * @param bufferInicial Buffer donde enviar los datos creados.
     * @param demora Cuanto tiempo demora en crear un dato
     */
    public Creador(Buffer bufferInicial, Buffer bufferValidado, long demora){
        this.bufferInicial = bufferInicial;
        this.bufferValidado = bufferValidado;
        this.cantidadCreados = 0;
        this.demora = demora;
    }

    public void run(){
        while(true){
            crear();
            if (bufferValidado.getConsumidos() == Consumidor.getMaximasConsumisiones())
                break;
        }
        System.out.println("Creador: creados = " + cantidadCreados + " Total creados = " + getTotalCreados());
    }

    /**
     * Crea un nuevo dato (demorando el tiempo correspondiente)
     * y se lo entrega al buffer. 
     */
    public void crear(){
        try {
            Dato nuevoDato= new Dato();
            TimeUnit.SECONDS.sleep(this.demora);
            this.cantidadCreados++;
            aumentartotalCreados();
            //System.out.println("Creados: " + cantidadCreados + ' ' + Thread.currentThread().getName());
            this.bufferInicial.agregarDato(nuevoDato);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized void aumentartotalCreados() {
        totalCreados++;
    }

    public static synchronized int getTotalCreados(){
        return totalCreados;
    }

    public int getCantidadCreados() {
        return cantidadCreados;
    }
}