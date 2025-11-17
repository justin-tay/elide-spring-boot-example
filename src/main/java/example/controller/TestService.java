package example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Transactional
public class TestService {
    private final JdbcTemplate jdbcTemplate;

    public TestService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void doTest() {
        System.out.println("--TestService.doTest() start--");
        System.out.println(Thread.currentThread().getName());
        System.out.println(TransactionSynchronizationManager.getResourceMap());
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList("SELECT * FROM post");
        System.out.println(result);
        System.out.println(TransactionSynchronizationManager.getResourceMap());
        System.out.println("--TestService.doTest() end--");
    }
}
