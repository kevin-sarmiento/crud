package crud.model;

import java.io.Serializable;

/**
 * Interfaz simple para entidades que tienen un ID.
 */
public interface Identificable extends Serializable {
    int getId();
    void setId(int id);
}
