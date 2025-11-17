package example.model;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SnowflakeIdGenerator implements IdentifierGenerator {
    static long running = 100;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        try {
            return running++;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
