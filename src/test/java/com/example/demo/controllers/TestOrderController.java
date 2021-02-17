package com.example.demo.controllers;

import com.example.demo.Data;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for testing OrderController
 * Mocks UserRepo and OrderRepo
 * Uses Data to produce data objects of User and Items
 */
public class TestOrderController {
    private OrderController orderController;
    private Data data;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        data = new Data();
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    /**
     * Tests submit from OrderController
     * Sets the users cart to the list of items from data class
     * Checks the response from the orderController against the list of items,
     * the total of the current usercart against the submitted order
     */
    public void submit_order(){
        User user = data.user();
        List<Item> items = data.items();
        user.getCart().setItems(items);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user); //stubbing to keep single logic

        ResponseEntity<UserOrder> submit = orderController.submit(user.getUsername());

        assertNotNull(submit);
        assertEquals(200, submit.getStatusCodeValue());
        assertEquals(items, submit.getBody().getItems());
        assertEquals(user.getCart().getTotal(), submit.getBody().getTotal());
    }

    @Test
    /**
     * Tests getUserOrder from OrderController
     * Populates a list of UserOrders with an UserOrder of two items and an UserOrder of one item
     * Checks the response from the orderController against the number of expected elements,
     * the total of the current usercart against the second UserOrder and that the total of
     * the current usercart not is equal to the total of the first userorder
     */
    public void get_orders_for_user(){
        User user = data.user();
        List<Item> items = data.items();
        for (Item item : items){
            user.getCart().addItem(item);
        }
        List<UserOrder> userOrders = new ArrayList<>();

        userOrders.add(UserOrder.createFromCart(user.getCart())); //add two items

        user.getCart().removeItem(items.get(0)); //removes first element from items
        userOrders.add(UserOrder.createFromCart(user.getCart())); //add one item

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> getOrders = orderController.getOrdersForUser(user.getUsername());

        assertNotNull(getOrders);
        assertEquals(200, getOrders.getStatusCodeValue());
        assertEquals(2, getOrders.getBody().size());
        assertEquals(user.getCart().getTotal(), getOrders.getBody().get(1).getTotal());
        assertNotEquals(user.getCart().getTotal(), getOrders.getBody().get(0).getTotal());
    }
}
