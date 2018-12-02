package com.party.game.common.json;

import org.apache.commons.lang3.time.FastDateFormat;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.Date;


/** 描述
 * @author yifeng
 * @version 2016/8/1
 */
public class DateJsonSerializer extends JsonSerializer<Date> {
    public static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public DateJsonSerializer() {
    }

    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(date != null?DATE_FORMAT.format(date):"null");
    }
}