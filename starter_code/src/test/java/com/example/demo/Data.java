package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Data {

    public User user(){
        User user = new User();
        user.setCart(new Cart());
        user.setUsername("test");
        user.setPassword("pass");
        return user;
    }

    public List<Item> items(){
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(3L);
        item.setName("brown circle");
        item.setPrice(BigDecimal.valueOf(19));
        items.add(item);
        Item item2 = new Item();
        item2.setId(4L);
        item2.setName("pink square");
        item2.setPrice(BigDecimal.valueOf(29));
        items.add(item2);
        return items;
    }

}
