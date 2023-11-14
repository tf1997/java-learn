package com.tf1997.actorCounter;

import akka.actor.*;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;

//public class CounterActor extends AbstractActorWithTimers {
//    public static class CountRequest {
//        public final String requestId;
//        public final FiniteDuration timeout;
//
//        public CountRequest(String requestId, FiniteDuration timeout) {
//            this.requestId = requestId;
//            this.timeout = timeout;
//        }
//    }
//
//    public static class FinishCounting {
//        public final String requestId;
//        public final ActorRef originalSender;
//
//        public FinishCounting(String requestId, ActorRef originalSender) {
//            this.requestId = requestId;
//            this.originalSender = originalSender;
//        }
//    }
//
//    public static class CountResponse {
//        public final String requestId;
//        public final int count;
//
//        public CountResponse(String requestId, int count) {
//            this.requestId = requestId;
//            this.count = count;
//        }
//    }
//
//    public static class OnlineActorCountRequest {
//        public final ActorRef originalSender;
//
//        public OnlineActorCountRequest(ActorRef originalSender) {
//            this.originalSender = originalSender;
//        }
//    }
//
//    public static class OnlineActorCountResponse {
//        public final int onlineActorCount;
//
//        public OnlineActorCountResponse(int onlineActorCount) {
//            this.onlineActorCount = onlineActorCount;
//        }
//    }
//
//    private Map<String, Integer> counters = new HashMap<>();
//
//    @Override
//    public Receive createReceive() {
//        return receiveBuilder()
//                .match(CountRequest.class, this::handleCountRequest)
//                .match(ActorIdentity.class, this::handleActorIdentity)
//                .match(FinishCounting.class, this::handleFinishCounting)
//                .match(OnlineActorCountRequest.class, this::handleOnlineActorCountRequest)
//                .build();
//    }
//
//    private void handleCountRequest(CountRequest countRequest) {
//        counters.put(countRequest.requestId, 1);
//        getContext().actorSelection("/user/*").tell(new Identify(countRequest.requestId), getSelf());
//        getTimers().startSingleTimer("timeout", new FinishCounting(countRequest.requestId, getSender()), countRequest.timeout);
//    }
//
////    private void handleActorIdentity(ActorIdentity actorIdentity) {
////        String cId = actorIdentity.correlationId().toString();
////        counters.put(cId, counters.getOrDefault(cId, 0) + 1);
////        actorIdentity.getRef().ifPresent(ref ->
////                getContext().actorSelection(ref.path().child("*")).tell(new Identify(cId), getSelf())
////        );
////    }
//    private void handleActorIdentity(ActorIdentity actorIdentity) {
//        String cId = actorIdentity.correlationId().toString();
//        counters.put(cId, counters.getOrDefault(cId, 0) + 1);
//        ActorRef ref = actorIdentity.getActorRef().orElse(null);
//        if (ref != null) {
//            getContext().actorSelection(ref.path().child("*")).tell(new Identify(cId), getSelf());
//        }
//    }
//
//    private void handleFinishCounting(FinishCounting finishCounting) {
//        finishCounting.originalSender.tell(new CountResponse(finishCounting.requestId, counters.getOrDefault(finishCounting.requestId, 0)), getSelf());
//    }
//
////    private void handleOnlineActorCountRequest(OnlineActorCountRequest onlineActorCountRequest) {
////        int onlineActorCount = counters.values().stream().mapToInt(Integer::intValue).sum();
////        onlineActorCountRequest.originalSender.tell(new OnlineActorCountResponse(onlineActorCount), getSelf());
////    }
//private void handleOnlineActorCountRequest(OnlineActorCountRequest onlineActorCountRequest) {
//        if (counters != null) {
//            int onlineActorCount = counters.values().stream().mapToInt(Integer::intValue).sum();
//            onlineActorCountRequest.originalSender.tell(new OnlineActorCountResponse(onlineActorCount), getSelf());
//        } else {
//            // 处理计数器为空的情况
//            onlineActorCountRequest.originalSender.tell(new OnlineActorCountResponse(0), getSelf());
//        }
//    }
//
//}

public class CounterActor1 extends AbstractActorWithTimers {

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

    public static class OnlineActorCountRequest {
        public final ActorRef originalSender;

        public OnlineActorCountRequest(ActorRef originalSender) {
            this.originalSender = originalSender;
        }
    }

    public static class OnlineActorCountResponse {
        public final int onlineActorCount;

        public OnlineActorCountResponse(int onlineActorCount) {
            this.onlineActorCount = onlineActorCount;
        }
    }

    private Map<String, Integer> counters = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CountRequest.class, this::handleCountRequest)
                .match(ActorIdentity.class, this::handleActorIdentity)
                .match(FinishCounting.class, this::handleFinishCounting)
                .match(OnlineActorCountRequest.class, this::handleOnlineActorCountRequest)
                .build();
    }

    private void handleCountRequest(CountRequest countRequest) {
        counters.put(countRequest.requestId, 0);
        getContext().actorSelection("/user/*").tell(new Identify(countRequest.requestId), getSelf());
        getTimers().startSingleTimer("timeout", new FinishCounting(countRequest.requestId, getSender()), countRequest.timeout);
    }

    private void handleActorIdentity(ActorIdentity actorIdentity) {
        String cId = actorIdentity.correlationId().toString();
        counters.put(cId, counters.getOrDefault(cId, 0) + 1);
        actorIdentity.getActorRef().ifPresent(ref ->
                getContext().actorSelection(ref.path().child("*")).tell(new Identify(cId), getSelf())
        );
    }

    private void handleFinishCounting(FinishCounting finishCounting) {
        finishCounting.originalSender.tell(new CountResponse(finishCounting.requestId, counters.getOrDefault(finishCounting.requestId, 0)), getSelf());
    }

    private void handleOnlineActorCountRequest(OnlineActorCountRequest onlineActorCountRequest) {
        int onlineActorCount = counters.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("--------------");
        System.out.println(onlineActorCount);
        onlineActorCountRequest.originalSender.tell(new OnlineActorCountResponse(onlineActorCount), getSelf());
    }
}
