package com.tf1997.messageSend;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author tf1997
 * @date 2023/9/15 15:30
 **/

// 定义一个消息类
class RequestMessage {
    // 请求消息的内容
}

class ResponseMessage {
    // 响应消息的内容
    private final String content;

    public ResponseMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

// 接收方 Actor
class ReceiverActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RequestMessage.class, message -> {
                    // 处理请求消息
                    String responseContent = "This is the response.";
                    getSender().tell(new ResponseMessage(responseContent), getSelf());
                })
                .build();
    }
}

// 发送方 Actor
class SenderActor extends AbstractActor {
    private final ActorRef receiver;

    public SenderActor(ActorRef receiver) {
        this.receiver = receiver;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("SendRequest", message -> {
                    // 发送请求消息并等待结果
                    Future<Object> future = Patterns.ask(receiver, new RequestMessage(), Timeout.apply(Duration.create(5, TimeUnit.SECONDS)));

                    try {
                        // 使用 Await 等待结果
                        ResponseMessage response = (ResponseMessage) Await.result(future, Duration.create(5, TimeUnit.SECONDS));
                        System.out.println("Received response: " + response.getContent());
                    } catch (TimeoutException | InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .build();
    }
}

public class AskTest {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");

        // 创建接收方 Actor
        ActorRef receiver = system.actorOf(Props.create(ReceiverActor.class), "receiver");

        // 创建发送方 Actor，并发送请求消息
        ActorRef sender = system.actorOf(Props.create(SenderActor.class, receiver), "sender");
        sender.tell("SendRequest", ActorRef.noSender());

        // 关闭 ActorSystem
        system.terminate();
    }
}
