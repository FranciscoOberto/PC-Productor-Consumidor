import java.util.concurrent.TimeUnit;

public class Revisor implements Runnable{
    private int N_REVISORES;
    private Buffer bufferInicial;
    private Buffer bufferValidado;
    private Integer cantidadRevisados;
    private static int totalRevisiones = 0;
    private final int demora;

    public Revisor(Buffer bufferInicial, Buffer bufferValidado, int demora, int N_REVISORES){
        this.bufferInicial = bufferInicial;
        this.bufferValidado = bufferValidado;
        this.cantidadRevisados = 0;
        this.demora = demora;
        this.N_REVISORES = N_REVISORES;
    }

    public void run(){
        while(Consumidor.getDatosconsumidos() < Consumidor.getMaximasConsumisiones()){
            revisar();
        }
    }

    public void revisar(){
        try {
            Dato dato = this.bufferInicial.obtenerDato();
            if (dato == null)
                return;
            if (!dato.revisadoPor(this)) {
                TimeUnit.SECONDS.sleep(this.demora);
                dato.addReviewer(this);
                cantidadRevisados++;
                System.out.println("Revisados: " + cantidadRevisados + ' ' + Thread.currentThread().getName());
            }
            this.copiar(dato);
        }catch (NullPointerException e) {

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copiar(Dato dato){
        Dato copia;
        if(dato.getReviewersCount() == this.N_REVISORES){
            try {
                copia = dato.clone();
                this.bufferValidado.agregarDato(copia);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void setCantidad(int num){
        N_REVISORES = num;
    }


}
