package crud.repository;

import java.util.List;
import java.util.Optional;

/**
 * Operaciones CRUD genéricas.
 */
public interface Repositorio<T> {
    T crear(T entidad) throws Exception;
    List<T> listar() throws Exception;
    Optional<T> buscarPorId(int id) throws Exception;
    T actualizar(T entidad) throws Exception;
    void eliminar(int id) throws Exception;
}
