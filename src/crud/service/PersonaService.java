package crud.service;

import crud.model.Persona;
import crud.repository.PersonaRepository;
import crud.exceptions.RepositoryException;

import java.util.List;
import java.util.Optional;

public class PersonaService {

    private final PersonaRepository repo;

    public PersonaService() throws RepositoryException {
        this.repo = new PersonaRepository();
    }

    public Persona crear(String nombre, int edad) throws RepositoryException {
        if (nombre == null || nombre.isBlank()) throw new RepositoryException("Nombre inválido");
        if (edad < 0) throw new RepositoryException("Edad inválida");
        Persona p = new Persona(0, nombre.trim(), edad); // id 0 -> FileRepository asigna
        return repo.crear(p);
    }

    public List<Persona> listar() throws RepositoryException {
        return repo.listar();
    }

    public Optional<Persona> buscar(int id) throws RepositoryException {
        return repo.buscarPorId(id);
    }

    public Persona actualizar(int id, String nombre, int edad) throws RepositoryException {
        Persona p = new Persona(id, nombre, edad);
        return repo.actualizar(p);
    }

    public void eliminar(int id) throws RepositoryException {
        repo.eliminar(id);
    }
}
