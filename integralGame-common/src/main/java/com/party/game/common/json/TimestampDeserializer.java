package com.party.game.common.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;


/** TimeStamp 序列化与反序列化
 * @author wei.li
 * @version 2016/8/1
 */
public class TimestampDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String time=jp.getText();
        return time != null && !time.isEmpty()?Timestamp.valueOf(time):null;
    }

}
