package example.models;

import com.yahoo.elide.core.utils.coerce.converters.ElideTypeConverter;
import com.yahoo.elide.core.utils.coerce.converters.Serde;

@ElideTypeConverter(type = ObfuscatedId.class, name = "ObfuscatedId")
public class ObfuscatedIdSerde implements Serde<String, ObfuscatedId> {
    @Override
    public ObfuscatedId deserialize(String val) {
        ObfuscatedId obfuscatedId = new ObfuscatedId();
        obfuscatedId.setId(Long.valueOf(val));
        return obfuscatedId;
    }

    @Override
    public String serialize(ObfuscatedId val) {
        return val.id.toString();
    }
}