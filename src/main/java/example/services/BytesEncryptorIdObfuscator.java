package example.services;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.crypto.encrypt.BytesEncryptor;

import com.yahoo.elide.core.exceptions.InvalidValueException;
import com.yahoo.elide.core.type.ClassType;
import com.yahoo.elide.core.type.Type;
import com.yahoo.elide.core.utils.obfuscation.IdObfuscator;

/**
 * Obfuscates Ids by using a {@link BytesEncryptor}.
 */
public class BytesEncryptorIdObfuscator implements IdObfuscator  {
    private final BytesEncryptor bytesEncryptor;
    
    public BytesEncryptorIdObfuscator(BytesEncryptor bytesEncryptor) {
        this.bytesEncryptor = bytesEncryptor;
    }

    @Override
    public String obfuscate(Object id) {
        byte[] encrypted;
        if (id instanceof Long value) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(value);
            encrypted = bytesEncryptor.encrypt(buffer.array());
        } else if (id instanceof String value) {
            encrypted = bytesEncryptor.encrypt(id.toString().getBytes(StandardCharsets.UTF_8));
        } else {
            throw new InvalidValueException("id");
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
    }

    @Override
    public <T> T deobfuscate(String obfuscatedId, Type<?> type) {
        try {
            byte[] value = Base64.getUrlDecoder().decode(obfuscatedId);
            byte[] decrypted = bytesEncryptor.decrypt(value);
            if (ClassType.LONG_TYPE.equals(type)) {
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                buffer.put(decrypted);
                buffer.flip();
                return (T) Long.valueOf(buffer.getLong());
            } else if (ClassType.STRING_TYPE.equals(type)) {
                return (T) new String(decrypted, StandardCharsets.UTF_8);
            }
            throw new IllegalArgumentException();
        } catch (RuntimeException e) {
            throw new InvalidValueException("id");
        }
    }
}
