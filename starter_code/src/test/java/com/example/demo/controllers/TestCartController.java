package com.example.demo.controllers;

import com.example.demo.Data;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Test class for testing CartController
 * Mocks UserRepo, CartRepo and ItemRepo
 * Uses Data to produce data objects of User and Items
 */
public class TestCartController {
    private CartController cartController;
    private Data data;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        data = new Data();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    /**
     * Tests addToCart from CartController
     * Gets user and items from the data class. Creates a new cart to equal the request
     * Creates a ModifyCartRequest to the first item of items with quantity 3
     * Checks the response from the cartController against the cart,
     * and that the number of elements and id is equal to the expected ones
     */
    public void add_to_cart(){
        User user = data.user();
        List<Item> items = data.items();
        Cart fullCart = new Cart();

        fullCart.addItem(items.get(0));
        fullCart.addItem(items.get(0));
        fullCart.addItem(items.get(0));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(items.get(0).getId());
        modifyCartRequest.setQuantity(3);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(java.util.Optional.of(items.get(0)));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(items.get(0).getId(), responseEntity.getBody().getItems().get(0).getId());
        assertEquals(3, responseEntity.getBody().getItems().size());
        assertEquals(fullCart.getTotal(), responseEntity.getBody().getTotal());
    }

    @Test
    /**
     * Tests removeFromCart from CartController
     * Gets user and items from the data class. Adds the items to the users cart
     * Creates a ModifyCartRequest with the first item of items
     * Checks the response from the cartController against the cart size
     * and that the total is equal to the total with the first item removed
     */
    public void remove_from_cart(){
        User user = data.user();
        Cart userCart = user.getCart();
        List<Item> items = data.items();

        for (Item item : items){
            userCart.addItem(item);
        }

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(items.get(0).getId());
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(java.util.Optional.of(items.get(0)));

        ResponseEntity<Cart> remove = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(remove);
        assertEquals(1, remove.getBody().getItems().size());
        assertEquals(userCart.getTotal(), remove.getBody().getTotal());
    }
}
