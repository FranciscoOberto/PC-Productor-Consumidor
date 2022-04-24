import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Dato {

    public final HashSet<Revisor> reviews;
    private ReentrantReadWriteLock lock;
    private static int static_id = 0;
    private final int id;

    /**
     * Constructor sin parámetros
     *
     */
    public Dato(){
        reviews = new HashSet<Revisor>();
        this.lock = new ReentrantReadWriteLock();
        static_id ++;
        this.id = static_id;
    }

    public Dato(int id, HashSet<Revisor> reviews){
        this.reviews = reviews;
        this.lock = new ReentrantReadWriteLock();
        this.id = id;
    }

    /**
     * Agrega un revisor al HashSet usando el ID del thread.
     * Verifica que no lo haya revisado antes, si no imprime un error
     *
     * @param revisor El revisor a agregar
     */
    public void addReviewer (Revisor revisor) throws Exception{
        if(revisadoPor(revisor)){
            throw new Exception("El revisor " + revisor + " ya ha revisado este dato");
        }
        else{
            this.lock.writeLock().lock();
            this.reviews.add(revisor);
            this.lock.writeLock().unlock();

        }
    }

    /**
     * Devuelve la cantidad de revisores que han validado el dato
     *
     */
    public int getReviewersCount(){
        this.lock.readLock().lock();
        int size = this.reviews.size();
        this.lock.readLock().unlock();
        return size;
    }

    /**
     * Verifica si el dato está validad
     *
     */

    /**
     * Verifica que el validador no haya revisado el dato anteriormente
     *
     * @param revisor El revisor del cual se quiere validar
     */
    public boolean revisadoPor(Revisor revisor){
        this.lock.readLock().lock();
        boolean isReviewed =  this.reviews.contains(revisor);
        this.lock.readLock().unlock();
        return isReviewed;
    }
  

    /**
     * Realiza la copia de un dato
     *
     */
    public Dato clone(){
        this.lock.readLock().lock();
        HashSet<Revisor> reviews = (HashSet<Revisor>) this.reviews.clone();
        Dato dato = new Dato(this.id, reviews);
        this.lock.readLock().unlock();
        return dato;
    }

    public int getId() {
        return id;
    }
}
