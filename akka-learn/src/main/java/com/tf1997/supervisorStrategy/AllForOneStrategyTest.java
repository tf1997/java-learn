package com.tf1997.supervisorStrategy;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author tf1997
 * @date 2023/9/15 14:31
 **/

class ChildActor1 extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("ThrowException")) {
                        throw new RuntimeException("An error occurred in ChildActor");
                    }
                })
                .build();
    }
}

class ParentActor1 extends AbstractActor {
    private final ActorRef child1;
    private final ActorRef child2;

    public ParentActor1() {
        this.child1 = getContext().actorOf(Props.create(ChildActor1.class), "child1");
        this.child2 = getContext().actorOf(Props.create(ChildActor1.class), "child2");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("StartChildren")) {
                        child1.tell("ThrowException", getSelf());
                        child2.tell("ThrowException", getSelf());
                    }
                })
                .build();
    }

    private PartialFunction<Throwable, SupervisorStrategy.Directive> decider = DeciderBuilder
            .match(RuntimeException.class, ex -> SupervisorStrategy.restart()) // 重启子 Actor
            .matchAny(o -> SupervisorStrategy.escalate()) // 抛出其他异常，上升到更高级别的监督者
            .build();

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new AllForOneStrategy(
                10, // 最多重试次数
                Duration.create(1, TimeUnit.MINUTES), // 重试时间间隔
                decider
        );
    }
}
public class AllForOneStrategyTest {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("SupervisionSystem");

        // 创建 ParentActor
        ActorRef parent = system.actorOf(Props.create(ParentActor1.class), "parent");

        // 向 ParentActor 发送消息，导致子 Actor 抛出异常
        parent.tell("StartChildren", ActorRef.noSender());

        // 关闭 ActorSystem
        system.terminate();
    }
}
