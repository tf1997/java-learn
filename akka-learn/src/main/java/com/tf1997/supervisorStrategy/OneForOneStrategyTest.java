package com.tf1997.supervisorStrategy;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * 监督策略测试
 * @author tf1997
 * @date 2023/9/15 13:46
 **/

class ChildActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("ThrowException")) {
                        throw new RuntimeException("An error occurred");
                    }
                })
                .build();
    }
}

class ParentActor extends AbstractActor {
    private final ActorRef child;

    public ParentActor() {
        this.child = getContext().actorOf(Props.create(ChildActor.class), "child");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("Start")) {
                        child.tell("ThrowException", getSelf());
                    }
                })
                .build();
    }

    private PartialFunction<Throwable, SupervisorStrategy.Directive> decider = DeciderBuilder
            .match(RuntimeException.class, ex -> SupervisorStrategy.restart()) // 停止 ShoppingCartActor
            .matchAny(o -> SupervisorStrategy.escalate()) // 抛出其他异常，上升到更高级别的监督者
            .build();

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10, // 最多重试次数
                Duration.create(1, TimeUnit.MINUTES), // 重试时间间隔, // 重试时间间隔
                decider
        );
    }
}
public class OneForOneStrategyTest {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("SupervisionSystem");

        // 创建 ParentActor
        ActorRef parent = system.actorOf(Props.create(ParentActor.class), "parent");

        // 向 ParentActor 发送消息，导致子 Actor 抛出异常
        parent.tell("Start", ActorRef.noSender());

        //// 创建 StoreActor
        //ActorRef storeActor = system.actorOf(Props.create(StoreActor.class), "store");
        //
        //// 向 StoreActor 发送消息，模拟购物车操作
        //storeActor.tell("AddItemToCart", ActorRef.noSender());
        //storeActor.tell("CheckoutCart", ActorRef.noSender());


        // 关闭 ActorSystem
        system.terminate();
    }
}
