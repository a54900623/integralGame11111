package com.party.game.common.utils;

import com.party.game.common.json.*;
import com.party.game.common.model.ShortDate;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Json工具类
 *
 * @author changjiang.tang Date:8/19/15 Time:3:41 PM
 */
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule("DateTimeModule", Version.unknownVersion());
        module.addDeserializer(ShortDate.class, new ShortDateJsonDeserializer());
        module.addSerializer(ShortDate.class, new ShortDateJsonSerializer());
        module.addSerializer(Date.class, new DateJsonSerializer());
        module.addDeserializer(Date.class, new DateJsonDeserializer());
        module.addSerializer(Timestamp.class,new TimestampSerializer());
        module.addDeserializer(Timestamp.class,new TimestampDeserializer());
        objectMapper.registerModule(module);
    }

    public static ObjectMapper getObjectMapperInstance() {
        return objectMapper;
    }

}
