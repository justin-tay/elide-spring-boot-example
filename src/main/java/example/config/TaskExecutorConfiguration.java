package example.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.ConnectionHandle;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Configuration
public class TaskExecutorConfiguration {

    public static class TransactionSynchronizationAwareThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
        private Logger logger = LoggerFactory.getLogger(TransactionSynchronizationAwareThreadPoolTaskExecutor.class);
        private static final long serialVersionUID = 1L;
        
        private Runnable beforeExecute = null;
        
        public void setBeforeExecute(Runnable beforeExecute) {
            this.beforeExecute = beforeExecute;
        }

        @Override
        protected void beforeExecute(Thread thread, Runnable task) {
            super.beforeExecute(thread, task);
            if (this.beforeExecute != null) {
                this.beforeExecute.run();
            }
        }

        @Override
        public Future<?> submit(Runnable task) {
            logger.info("Task [{}]", task);
            return super.submit(task);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            logger.info("Task [{}]", task);
            return super.submit(task);
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
                // Close entity manager first which should close the connection attached to the entity manager
                for (Object value : resources.values()) {
                    if (value instanceof EntityManagerHolder emHolder) {
                        try {
                            EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
                        } catch (Throwable e) {
                            // Do nothing
                        }
                    }
                }

                // Call connectionHolder.released note that by default the
                // org.springframework.orm.jpa.DefaultJpaDialect#releaseJdbcConnection doesn't
                // attempt to close the connection as this should be closed when the
                // entitymanager is closed but it is unclear how it ended up in this state so
                // attempt to call released.. but actually no ConnectionHolder implementation actually
                // closes a connection that is bound to a transaction so this likely doesn't do anything
                for (Object value : resources.values()) {
                    if (value instanceof ConnectionHolder connectionHolder) {
                        try {
                            connectionHolder.released();
                        } catch (Throwable e) {
                            // Do nothing
                        }
                    }
                }

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
            ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder, DataSource dataSource) {
        TransactionSynchronizationAwareThreadPoolTaskExecutor result = threadPoolTaskExecutorBuilder
                .build(TransactionSynchronizationAwareThreadPoolTaskExecutor.class);
        result.setBeforeExecute(() -> {
            ConnectionHandle connectionHandle = new ConnectionHandle() {
                Connection connection = null;

                @Override
                public Connection getConnection() {
                    try {
                        if (this.connection == null) {
                            this.connection = dataSource.getConnection();
                        }
                        return this.connection;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                
                @Override 
                public void releaseConnection(Connection con) {
                    try {
                        if (!con.isClosed()) {
                            con.close();
                        }
                    } catch (Throwable e) {
                        // Do nothing
                    }
                    this.connection = null;
                }
            };
            // This sets the pre-requisite to mock the failure
            TransactionSynchronizationManager.bindResource(dataSource, new ConnectionHolder(connectionHandle));
        });
        return result;
    }
}
