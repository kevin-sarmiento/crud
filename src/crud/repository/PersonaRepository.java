package crud.repository;

import crud.model.Persona;
import crud.exceptions.RepositoryException;

/**
 * Repositorio específico para Persona.
 * Usa FileRepository como base.
 */
public class PersonaRepository extends FileRepository<Persona> {
    public PersonaRepository() throws RepositoryException {
        super("personas.dat");
    }
    // si quieres agregar métodos específicos de Persona los pones aquí
}
