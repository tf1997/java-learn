package com.tf1997.actorCounter;

import akka.actor.*;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class CounterActorTest {

    public static void main(String[] args) {
        // 创建 Actor 系统
        ActorSystem system = ActorSystem.create("TestSystem");

        // 创建 CounterActor
        ActorRef counterActor = system.actorOf(Props.create(CounterActor.class), "counterActor");

        // 发送 CountRequest 消息给 CounterActor
        CounterActor.CountRequest countRequest = new CounterActor.CountRequest("request1", Duration.create(5, TimeUnit.SECONDS));
        counterActor.tell(countRequest, ActorRef.noSender());

        // 等待一段时间，确保 CounterActor 有足够的时间进行处理
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 关闭 Actor 系统
        system.terminate();
    }

    public static class SampleActor extends AbstractActor {
        @Override
        public Receive createReceive() {
            return receiveBuilder().build();
        }
    }
}