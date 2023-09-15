package com.tf1997.supervisorStrategy;

import akka.actor.AbstractActor;

/**
 * @author tf1997
 * @date 2023/9/15 14:08
 **/

public class ShoppingCartActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("AddItem")) {
                        addItemToCart();
                    } else if (message.equals("Checkout")) {
                        checkout();
                    }
                })
                .build();
    }

    private void addItemToCart() {
        // 模拟添加商品到购物车的操作
        System.out.println("Item added to cart.");
    }

    private void checkout() {
        // 模拟结算购物车的操作
        throw new RuntimeException("Checkout failed: Payment gateway error.");
    }
}