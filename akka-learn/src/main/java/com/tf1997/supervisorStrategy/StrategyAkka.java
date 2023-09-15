package com.tf1997.supervisorStrategy;

import akka.actor.*;
import akka.japi.Function;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.Function1;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.util.Try;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * @author tf1997
 * @date 2023/9/15 15:38
 **/

public class StrategyAkka extends UntypedAbstractActor {

    // 定义监督策略
    private SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.apply("1 minute"),
            (Function<Throwable, SupervisorStrategy.Directive>) err -> {
                if (err instanceof IOException) {
                    System.out.println("-----------IOException-----------");
                    return SupervisorStrategy.resume(); // 恢复运行
                } else if (err instanceof IndexOutOfBoundsException) {
                    System.out.println("-----------IndexOutOfBoundsException-----------");
                    return SupervisorStrategy.restart(); // 重启
                } else if (err instanceof SQLException) {
                    System.out.println("-----------SQLException-----------");
                    return SupervisorStrategy.stop(); // 停止
                } else {
                    System.out.println("-----------UnkownException-----------");
                    return SupervisorStrategy.escalate(); // 升级失败
                }
            });

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void preStart() throws Exception {
        ActorRef ref = getContext().actorOf(Props.create(WorkActor.class), "workActor");

        // 监控生命周期
        getContext().watch(ref);

        ref.tell("Hello", ActorRef.noSender());
        ref.tell(new IOException(), ActorRef.noSender());
        ref.tell(new IndexOutOfBoundsException(), ActorRef.noSender());

        Timeout timeout = new Timeout(10, TimeUnit.SECONDS);
        Future<Object> akka_ask = Patterns.ask(ref, "getValue", timeout);
        System.out.println("ask...");
        akka_ask.onComplete(new Function1<Try<Object>, Object>() {
            @Override
            public Object apply(Try<Object> v1) {
                if (v1.isSuccess()) System.out.println("发送成功，收到消息:" + v1.get());
                if (v1.isFailure()) System.out.println("发送失败：" + v1.get());
                return null;
            }
        }, getContext().dispatcher());

        System.out.println("continue...");
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Throwable, Throwable {
        if (message instanceof Terminated)
            System.out.println(((Terminated)message).getActor() + "已经停止");
        else System.out.println("stateCount:" + message);
    }

    public static void main(String[] args) {
        ActorSystem sys = ActorSystem.create("sys");
        ActorRef ref = sys.actorOf(Props.create(StrategyAkka.class), "strategyActor");
        sys.terminate();
    }
}

class WorkActor extends UntypedAbstractActor {

    private int state = 1; // 状态参数

    @Override
    public void preStart() throws Exception, Exception {
        System.out.println("start, state is:" + state++);
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("stop");
        super.postStop();
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        System.out.println("postRestart");
        super.postRestart(reason);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        // 模拟计算任务
        this.state++;
        System.out.println("message:" + message);
        if (message instanceof Exception) throw (Exception) message;
        else if ("getValue".equals(message)) getSender().tell(state, getSelf());
        else unhandled(message);
    }

}
