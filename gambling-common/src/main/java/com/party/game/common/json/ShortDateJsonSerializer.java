package com.party.game.common.json;

import com.party.game.common.model.ShortDate;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class ShortDateJsonSerializer extends JsonSerializer<ShortDate> {
    public ShortDateJsonSerializer() {
    }

    public void serialize(ShortDate shortDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(shortDate != null?shortDate.toString():"null");
    }
}
