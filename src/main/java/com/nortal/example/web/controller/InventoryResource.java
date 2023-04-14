//package com.nortal.example.web.controller;
//
//import com.nortal.example.domain.Inventory;
//import com.nortal.example.service.InventoryService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URISyntaxException;
//import java.util.List;
//
///**
// * REST controller for managing {@link com.nortal.example.domain.Inventory}.
// */
//@RestController
//@RequestMapping("/api")
//public class InventoryResource {
//
//    private final Logger log = LoggerFactory.getLogger(InventoryResource.class);
//
//    private static final String ENTITY_NAME = "inventory";
//
//    @Value("${jhipster.clientApp.name}")
//    private String applicationName;
//
//    private final InventoryService inventoryService;
//
//    private final InventoryRepository inventoryRepository;
//
//    public InventoryResource(InventoryService inventoryService, InventoryRepository inventoryRepository) {
//        this.inventoryService = inventoryService;
//        this.inventoryRepository = inventoryRepository;
//    }
//
//    /**
//     * {@code POST  /inventories} : Create a new inventory.
//     *
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventory, or with status {@code 400 (Bad Request)} if the inventory has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    public ResponseEntity<Inventory> createInventory() {
//        return null;
//    }
//
//    /**
//     * {@code PUT  /inventories/:id} : Updates an existing inventory.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventory,
//     * or with status {@code 400 (Bad Request)} if the inventory is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the inventory couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/inventories/{id}")
//    public ResponseEntity<Inventory> updateInventory() {
//        return null;
//    }
//
//    /**
//     * {@code PATCH  /inventories/:id} : Partial updates given fields of an existing inventory, field will ignore if it is null
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventory,
//     * or with status {@code 400 (Bad Request)} if the inventory is not valid,
//     * or with status {@code 404 (Not Found)} if the inventory is not found,
//     * or with status {@code 500 (Internal Server Error)} if the inventory couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/inventories/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<Inventory> partialUpdateInventory() {
//      return null;
//    }
//
//    /**
//     * {@code GET  /inventories} : get all the inventories.
//     *
//     * @param pageable the pagination information.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventory in body.
//     */
//    @GetMapping("/inventories")
//    public ResponseEntity<List<Inventory>> getAllInventories(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
//       return null;
//    }
//
//    /**
//     * {@code GET  /inventories/:id} : get the "id" inventory.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventory, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/inventories/{id}")
//    public ResponseEntity<Inventory> getInventory() {
//        return null;
//    }
//
//    /**
//     * {@code DELETE  /inventories/:id} : delete the "id" inventory.
//     *
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/inventories/{id}")
//    public ResponseEntity<Void> deleteInvnentory() {
//       return null;
//    }
//}
