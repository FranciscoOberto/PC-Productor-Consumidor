import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Random;

public class Buffer {

    private final int LimiteDatos;
    private HashMap<Integer, Dato> datos;
    private int rechazados;
    private final ReentrantReadWriteLock lock;
    private Buffer buffer;

    /**
     * Constructor con parámetros
     * Inicializa las variables de instancia
     * @param LimiteDatos Cantidad máxima de datos.
     */
    public Buffer(int LimiteDatos, Buffer buffer) {
        this.LimiteDatos = LimiteDatos;
        this.datos = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.rechazados = 0;
        this.buffer = buffer;
    }

    /**
     * Le asigna un ID a un dato y lo agrega al Buffer.
     * No se puede agregar un dato si el total de datos es mayor
     * o igual a LimiteDatos.
     * @param dato El dato a agregar en el Buffer.
     */
    public void agregarDato(Dato dato) throws Exception {
        //Metemos todo dentro de un writeLock para que size no cambie hasta que se agrega el Dato.
        this.lock.writeLock().lock();
        if (datos.size() == LimiteDatos){
            this.rechazados++;
            this.lock.writeLock().unlock();
            return;
        }
        else if (datos.size() > LimiteDatos) {
            this.lock.writeLock().unlock();
            throw new Exception("Buffer rebalsado Exception" + this.datos.size());
        }
        datos.put(dato.getId(), dato);
        this.lock.writeLock().unlock();
    }


    /**
     * Obtiene un dato del Buffer.
     * Si no hay datos o si están en uso, devuelve null.
     */
    public Dato obtenerDato() {
        this.lock.readLock().lock();
        if (datos.isEmpty()) {
            this.lock.readLock().unlock();
            return null;
        }
        Random generator = new Random();
        Set keySet = datos.keySet();
        //Object[] values = this.datos.values().toArray();
        int key = generator.nextInt(keySet.size());
        this.lock.readLock().unlock();
        return datos.get(key);
    }

    /**
     * Elimina un dato del Buffer.
     * @param id El id del dato a eliminar del Buffer.
     */
    public void BorrarDato(int id){
        //this.lock.writeLock().lock();
        datos.remove(id);
        //this.lock.writeLock().unlock();
    }

    public boolean consumirDato(){
        this.lock.writeLock().lock();
        //Dato dato = this.obtenerDato();
        if (datos.isEmpty()) {
            this.lock.writeLock().unlock();
            return false;
        }
        Random generator = new Random();
        Set keySet = datos.keySet();
        int key = generator.nextInt(keySet.size());
        Dato dato = datos.get(key);
        if (dato == null) {
            this.lock.writeLock().unlock();
            return false;
        }
        this.datos.remove(dato.getId());
        this.buffer.BorrarDato(dato.getId());
        this.lock.writeLock().unlock();
        return true;
    }

}
