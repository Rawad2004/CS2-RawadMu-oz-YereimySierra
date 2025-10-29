package app.domain.repository;

import app.domain.model.Staff;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.Username;

import java.util.List; // Import añadido
import java.util.Optional;

public interface StaffRepositoryPort {

    /**
     * Guarda un nuevo miembro del personal o actualiza uno existente.
     * @param staff El objeto Staff a guardar.
     * @return El objeto Staff guardado (usualmente con el ID asignado por la BD).
     */
    Staff save(Staff staff);

    /**
     * Busca un miembro del personal por su ID de base de datos.
     * @param id El ID único de la base de datos.
     * @return Un Optional que contiene al Staff si se encuentra, o vacío si no.
     */
    Optional<Staff> findById(Long id);

    /**
     * Busca un miembro del personal por su nombre de usuario.
     * @param username El nombre de usuario único.
     * @return Un Optional que contiene al Staff si se encuentra.
     */
    Optional<Staff> findByUsername(Username username);

    /**
     * Busca un miembro del personal por su número de cédula.
     * @param nationalId El número de cédula único.
     * @return Un Optional que contiene al Staff si se encuentra.
     */
    Optional<Staff> findByNationalId(NationalId nationalId);

    /**
     * Obtiene una lista de todos los miembros del personal.
     * @return Una lista con todos los objetos Staff.
     */
    List<Staff> findAll(); // <-- MÉTODO AÑADIDO

    /**
     * Elimina un miembro del personal por su ID de base de datos.
     * @param id El ID del Staff a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si ya existe un Staff con un nombre de usuario dado.
     * Esencial para validar la regla de negocio de unicidad.
     * @param username El nombre de usuario a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByUsername(Username username);

    /**
     * Verifica si ya existe un Staff con una cédula dada.
     * @param nationalId La cédula a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNationalId(NationalId nationalId);
}
