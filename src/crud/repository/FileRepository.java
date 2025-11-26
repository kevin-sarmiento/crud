package crud.repository;

import crud.model.Identificable;
import crud.exceptions.RepositoryException;
import crud.exceptions.EntityNotFoundException;
import crud.exceptions.ValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación genérica de repositorio que persiste una lista de entidades en un archivo.
 *
 * @param <T> tipo que implementa Identificable y Serializable
 */
public class FileRepository<T extends Identificable> implements Repositorio<T> {

    private final File file;
    private List<T> items;
    private int nextId = 1;

    public FileRepository(String filename) throws RepositoryException {
        this.file = new File(filename);
        this.items = load();
        calculateNextId();
    }

    // -------------------- CRUD --------------------

    @Override
    public synchronized T crear(T entidad) throws RepositoryException {
        if (entidad == null) throw new ValidationException("Entidad nula");
        if (entidad.getId() != 0) {
            // evitar duplicados por id
            if (items.stream().anyMatch(e -> e.getId() == entidad.getId())) {
                throw new ValidationException("Ya existe una entidad con id " + entidad.getId());
            }
        } else {
            entidad.setId(nextId++);
        }
        items.add(entidad);
        save();
        return entidad;
    }

    @Override
    public synchronized List<T> listar() {
        return new ArrayList<>(items);
    }

    @Override
    public synchronized Optional<T> buscarPorId(int id) {
        return items.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public synchronized T actualizar(T entidad) throws RepositoryException {
        if (entidad == null || entidad.getId() <= 0) throw new ValidationException("Id inválido para actualizar");
        Optional<T> opt = buscarPorId(entidad.getId());
        if (opt.isEmpty()) throw new EntityNotFoundException("Entidad con id " + entidad.getId() + " no encontrada");
        T existente = opt.get();
        // reemplazamos: removemos y agregamos la nueva entidad (podrías actualizar campos individualmente)
        items.remove(existente);
        items.add(entidad);
        save();
        return entidad;
    }

    @Override
    public synchronized void eliminar(int id) throws RepositoryException {
        boolean removed = items.removeIf(e -> e.getId() == id);
        if (!removed) throw new EntityNotFoundException("Entidad con id " + id + " no encontrada");
        save();
    }

    // -------------------- Persistencia --------------------

    @SuppressWarnings("unchecked")
    private List<T> load() throws RepositoryException {
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<T>) obj;
            } else {
                throw new RepositoryException("Formato de archivo inválido");
            }
        } catch (EOFException eof) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Error al cargar datos: " + e.getMessage(), e);
        }
    }

    private void save() throws RepositoryException {
        // carpeta padre
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(items);
        } catch (IOException e) {
            throw new RepositoryException("Error al guardar datos: " + e.getMessage(), e);
        }
    }

    private void calculateNextId() {
        int max = items.stream().mapToInt(Identificable::getId).max().orElse(0);
        this.nextId = max + 1;
    }
}
