package example.controller;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TestEventListener {
    private final TestService testService;
    public TestEventListener(TestService testService) {
        this.testService = testService;
    }

    @EventListener
//    @Transactional
    @Async
    public void onEvent(TestEvent event) {
        System.out.println("--TestEventListener.onEvent() start--");
        System.out.println(Thread.currentThread().getName());
        System.out.println(TransactionSynchronizationManager.getResourceMap());
        System.out.println(event);
        testService.doTest();
        System.out.println("--TestEventListener.onEvent() end--");
    }
}
