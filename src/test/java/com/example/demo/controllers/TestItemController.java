package com.example.demo.controllers;

import com.example.demo.Data;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Test class for testing ItemController
 * Mocks ItemRepo
 * Uses Data to produce data objects of Items
 */
public class TestItemController {
    private ItemController itemController;
    private Data data;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        data = new Data();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    /**
     * Tests getItems from ItemController
     * Gets items from the data class
     * Checks the response from the orderController against the list of items
     */
    public void get_items(){
        List<Item> items = data.items();

        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItems();

        assertNotNull(itemResponseEntity);
        assertEquals(200, itemResponseEntity.getStatusCodeValue());
        assertArrayEquals(items.toArray(), itemResponseEntity.getBody().toArray());
    }

    @Test
    /**
     * Tests getItemById from ItemController
     * Gets items from the data class
     * Checks the response from the orderController against the item in items
     */
    public void get_item_by_id(){
        List<Item> items = data.items();

        when(itemRepository.findById(items.get(1).getId())).thenReturn(java.util.Optional.ofNullable(items.get(1)));

        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(items.get(1).getId());

        assertNotNull(itemResponseEntity);
        assertEquals(200, itemResponseEntity.getStatusCodeValue());
        assertEquals(items.get(1).getId(), itemResponseEntity.getBody().getId());
    }

    @Test
    /**
     * Tests getItemByName from ItemController
     * Gets items from the data class
     * Checks the response from the orderController against the item in items
     */
    public void get_items_by_name(){
        List<Item> items = data.items();

        when(itemRepository.findByName(items.get(1).getName())).thenReturn(Collections.singletonList((items.get(1))));

        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName(items.get(1).getName());

        assertNotNull(itemResponseEntity);
        assertEquals(200, itemResponseEntity.getStatusCodeValue());
        assertEquals(items.get(1).getName(), itemResponseEntity.getBody().get(0).getName());
    }
}
