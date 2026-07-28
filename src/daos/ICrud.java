package daos;
import java.util.List;

/**
 * Interfaz genérica para estandarizar los DAOs.
 * @param <T> La 'T' representa la clase de la entidad (ej: Asignacion, Aula).
 * @param <ID> El 'ID' representa el tipo de dato del identificador (ej: String, Integer).
 */

public interface ICrud<T, ID> {
    
    void agregar(T entidad);
    
    List<T> obtenerRegistros();
    
    T obtenerPorId(ID id);
    
    void modificar(T entidad);
    
    void eliminar(ID id);
}