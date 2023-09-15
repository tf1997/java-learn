package com.tf1997.messageSend;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author tf1997
 * @date 2023/9/15 16:33
 **/

class MyMessage {
    private final String content;

    public MyMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

// 接收方 Actor
class ReceiverActor1 extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MyMessage.class, message -> {
                    // 处理消息，不返回结果
                    System.out.println("Received message: " + message.getContent());
                })
                .build();
    }
}
public class TellTest {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");

        // 创建接收方 Actor
        ActorRef receiver = system.actorOf(Props.create(ReceiverActor1.class), "receiver");

        // 发送方 Actor 发送消息给接收方 Actor
        receiver.tell(new MyMessage("Hello, Akka!"), ActorRef.noSender());

        // 关闭 ActorSystem
        system.terminate();
    }

}
