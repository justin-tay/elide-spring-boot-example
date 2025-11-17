package example.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Configuration
public class TaskExecutorConfiguration {

    public static class TransactionSynchronizationAwareThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
        private Logger logger = LoggerFactory.getLogger(TransactionSynchronizationAwareThreadPoolTaskExecutor.class);
        private static final long serialVersionUID = 1L;

        @Override
        protected void beforeExecute(Thread thread, Runnable task) {
            super.beforeExecute(thread, task);
        }

        @Override
        protected void afterExecute(Runnable task, Throwable ex) {
            super.afterExecute(task, ex);
            boolean active = TransactionSynchronizationManager.isSynchronizationActive();
            if (active) {
                logger.error("TransactionSynchronizationManager synchronization is still active.");
            }
            Map<Object, Object> resources = TransactionSynchronizationManager.getResourceMap();
            if (resources != null && !resources.isEmpty()) {
                logger.error("Transactional resources [{}] are present in the thread [{}]", resources, Thread.currentThread().getName());
                // Clear
                for (Object key : resources.keySet()) {
                    logger.error("Unbinding transactional resource with key [{}]", key);
                    TransactionSynchronizationManager.unbindResourceIfPossible(key);
                }
            }
            // Clear transaction synchronization state
            TransactionSynchronizationManager.clear();
        }
    }

    @Bean
    TransactionSynchronizationAwareThreadPoolTaskExecutor applicationTaskExecutor(
            ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder) {
        return threadPoolTaskExecutorBuilder.build(TransactionSynchronizationAwareThreadPoolTaskExecutor.class);
    }

}
