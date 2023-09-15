package com.tf1997.supervisorStrategy;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;
import scala.PartialFunction;
import java.util.concurrent.TimeUnit;

/**
 * @author tf1997
 * @date 2023/9/15 14:09
 **/

public class StoreActor extends AbstractActor {
    private final ActorRef shoppingCart;

    public StoreActor() {
        this.shoppingCart = getContext().actorOf(Props.create(ShoppingCartActor.class), "shoppingCart");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("AddItemToCart")) {
                        shoppingCart.tell("AddItem", getSelf());
                    } else if (message.equals("CheckoutCart")) {
                        shoppingCart.tell("Checkout", getSelf());
                    }
                })
                .build();
    }

    private PartialFunction<Throwable, SupervisorStrategy.Directive> decider = DeciderBuilder
            .match(RuntimeException.class, ex -> SupervisorStrategy.stop()) // 停止 ShoppingCartActor
            .matchAny(o -> SupervisorStrategy.escalate()) // 抛出其他异常，上升到更高级别的监督者
            .build();

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10, // 最多重试次数
                Duration.create(1, TimeUnit.MINUTES), // 重试时间间隔,
                decider
        );
    }
}
