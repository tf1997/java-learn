package com.tf1997;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author tf1997
 * @date 2023/9/15 10:55
 **/

public class MyActorSimple extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("Hello")) {
                        System.out.println("Hello from MyActor");
                    } else if (message.equals("Goodbye")) {
                        System.out.println("Goodbye from MyActor");
                    } else {
                        System.out.println("Unknown message");
                    }
                })
                .build();
    }
}

class Main {
    public static void main(String[] args) {
        // 创建 ActorSystem, ActorSystem 是 Akka 应用程序的主要入口点，它用于管理 Actor。
        ActorSystem system = ActorSystem.create("MyActorSystem");

        // 创建 MyActor 实例
        ActorRef myActor = system.actorOf(Props.create(MyActorSimple.class), "myActor");

        // 向 Actor 发送消息
        myActor.tell("Hello", ActorRef.noSender());
        myActor.tell("Goodbye", ActorRef.noSender());

        // 关闭 ActorSystem
        system.terminate();
    }
}
