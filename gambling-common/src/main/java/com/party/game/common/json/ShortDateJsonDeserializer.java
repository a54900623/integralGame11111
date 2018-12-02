package com.party.game.common.json;

import com.party.game.common.model.ShortDate;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

public class ShortDateJsonDeserializer extends JsonDeserializer<ShortDate> {

    public ShortDateJsonDeserializer() {
    }

    public ShortDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String time = jsonParser.getText();
        return time != null && !time.isEmpty()?new ShortDate(time):null;
    }
}