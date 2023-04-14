package com.nortal.example.service;

import com.nortal.example.domain.Inventory;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Inventory}.
 */
public interface InventoryService {
    /**
     * Save a inventory.
     *
     * @param inventory the entity to save.
     * @return the persisted entity.
     */
    Inventory save(Inventory inventory);

    /**
     * Updates a inventory.
     *
     * @param inventory the entity to update.
     * @return the persisted entity.
     */
    Inventory update(Inventory inventory);

    /**
     * Partially updates a inventory.
     *
     * @param inventory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Inventory> partialUpdate(Inventory inventory);

    /**
     * Get all the inventorys.
     *
     * @return the list of entities.
     */
    List<Inventory> findAll();

    /**
     * Get the "id" inventory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Inventory> findOne(Long id);

    /**
     * Delete the "id" inventory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
