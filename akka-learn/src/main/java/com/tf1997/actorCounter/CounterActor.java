package com.tf1997.actorCounter;

import akka.actor.*;
import akka.actor.AbstractActor.Receive;
import akka.actor.Identify;
import akka.actor.ActorIdentity;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;

public class CounterActor extends AbstractActorWithTimers {

    public static class CountRequest {
        public final String requestId;
        public final FiniteDuration timeout;

        public CountRequest(String requestId, FiniteDuration timeout) {
            this.requestId = requestId;
            this.timeout = timeout;
        }
    }

    public static class FinishCounting {
        public final String requestId;
        public final ActorRef originalSender;

        public FinishCounting(String requestId, ActorRef originalSender) {
            this.requestId = requestId;
            this.originalSender = originalSender;
        }
    }

    public static class CountResponse {
        public final String requestId;
        public final int count;

        public CountResponse(String requestId, int count) {
            this.requestId = requestId;
            this.count = count;
        }
    }

    private Map<String, Integer> counters = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CountRequest.class, this::handleCountRequest)
                .match(ActorIdentity.class, this::handleActorIdentity)
                .match(FinishCounting.class, this::handleFinishCounting)
                .build();
    }

    private void handleCountRequest(CountRequest request) {
        // 初始化计数器，将计数器设置为0
        counters.put(request.requestId, 0);
        // 向所有的Actor发送Identify消息
        context().actorSelection("/user/*").tell(new Identify(request.requestId), self());
        // 启动定时器，在超时时发送FinishCounting消息
        timers().startSingleTimer("timeout", new FinishCounting(request.requestId, sender()), request.timeout);
    }

    private void handleActorIdentity(ActorIdentity identity) {
        // 处理ActorIdentity消息，更新计数器并向找到的Actor发送Identify消息
        String cId = identity.correlationId().toString();
        ActorRef ref = identity.getActorRef().get();
        if (ref != null) {
            counters.put(cId, counters.getOrDefault(cId, 0) + 1);
            System.out.println("count: " + counters.get(cId));
            ref.tell(new Identify(cId), self());
        }
    }

    private void handleFinishCounting(FinishCounting finishCounting) {
        // 完成计数，向发起CountRequest请求的Actor发送CountResponse消息
        String requestId = finishCounting.requestId;
        finishCounting.originalSender.tell(new CountResponse(requestId, counters.getOrDefault(requestId, 0)), self());
    }
}
