import java.util.concurrent.TimeUnit;

public class Revisor implements Runnable{
    private int N_REVISORES;
    private Buffer bufferInicial;
    private Buffer bufferValidado;
    private Integer cantidadRevisados;
    private static int totalRevisados = 0;
    private final int demora;

    public Revisor(Buffer bufferInicial, Buffer bufferValidado, int demora, int N_REVISORES){
        this.bufferInicial = bufferInicial;
        this.bufferValidado = bufferValidado;
        this.cantidadRevisados = 0;
        this.demora = demora;
        this.N_REVISORES = N_REVISORES;
    }

    public void run(){
        while(true){
            revisar();
            if (bufferValidado.getConsumidos() == Consumidor.getMaximasConsumisiones())
                break;
        }
        //System.out.println("Revisor: revisados = " + cantidadRevisados + " Total revisados = "+ getTotalRevisados());
        System.out.println("Revisor: revisados = " + cantidadRevisados);
    }

    public void revisar(){
        if (this.bufferInicial.estaVacio())
            return;
        try {
            Dato dato = this.bufferInicial.obtenerDato();
            if (dato == null) {
                System.out.println("revisar dato null" + " \n");
                return;
            }
            if (!dato.revisadoPor(this)) {
                TimeUnit.SECONDS.sleep(this.demora);
                dato.addReviewer(this);
                cantidadRevisados++;
                aumentartotalRevisados();
                this.copiar(dato);
            }
        }catch (NullPointerException e) {

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copiar(Dato dato){
        Dato copia;
        if(dato.getReviewersCount() == this.N_REVISORES){
            try {
                copia = dato.copiar();
                this.bufferValidado.agregarDato(copia);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void aumentartotalRevisados() {
        totalRevisados++;
    }

    public static synchronized int getTotalRevisados(){
        return totalRevisados;
    }

    public void setCantidad(int num){
        N_REVISORES = num;
    }


}
