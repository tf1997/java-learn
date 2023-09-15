package com.tf1997;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * AKKA:层次结构
 * @author tf1997
 * @date 2023/9/15 11:30
 **/

// BookstoreManager 是顶级父 Actor
class BookstoreManager extends AbstractActor {
    private final ActorRef orderProcessor;
    private final ActorRef paymentHandler;

    public BookstoreManager() {
        // 创建子 Actor：OrderProcessor 和 PaymentHandler
        this.orderProcessor = getContext().actorOf(Props.create(OrderProcessor.class), "orderProcessor");
        this.paymentHandler = getContext().actorOf(Props.create(PaymentHandler.class), "paymentHandler");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("ProcessOrder")) {
                        // 向子 Actor 发送消息
                        orderProcessor.tell("ProcessOrder", getSelf());//tell(传递信息，接收者) ActorRef.noSender表示不指定接收者
                    } else if (message.equals("ProcessPayment")) {
                        // 向子 Actor 发送消息
                        paymentHandler.tell("ProcessPayment", getSelf());
                    }
                })
                .build();
    }
}

class OrderProcessor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {//String.class 表示接收String类型的消息
                    if (message.equals("ProcessOrder")) {
                        // 处理订单的逻辑
                        System.out.println("OrderProcessor: Processing order");
                    }
                })
                .build();
    }
}

class PaymentHandler extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("ProcessPayment")) {
                        // 处理支付的逻辑
                        System.out.println("PaymentHandler: Processing payment");
                    }
                })
                .build();
    }
}

class Main1 {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("BookstoreSystem");

        // 创建顶级父 Actor：BookstoreManager
        ActorRef bookstoreManager = system.actorOf(Props.create(BookstoreManager.class), "bookstoreManager");

        // 向顶级父 Actor 发送消息
        bookstoreManager.tell("ProcessOrder", ActorRef.noSender());
        bookstoreManager.tell("ProcessPayment", ActorRef.noSender());

        // 关闭 ActorSystem
        system.terminate();
    }
}
