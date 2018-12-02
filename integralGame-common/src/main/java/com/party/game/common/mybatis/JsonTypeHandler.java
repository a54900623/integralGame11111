package com.party.game.common.mybatis;

import com.party.game.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/** json类型处理器
 * @author yifeng
 * @version 2016/8/1
 */
public class JsonTypeHandler<T> implements TypeHandler<T> {

    private Type type;

    public JsonTypeHandler() {
        type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, toJson(parameter));
    }

    public T getResult(ResultSet rs, String columnName) throws SQLException {
        return fromJson(rs.getString(columnName));
    }

    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromJson(rs.getString(columnIndex));
    }

    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromJson(cs.getString(columnIndex));
    }

    private String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return JsonUtils.getObjectMapperInstance().writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("JsonTypeHandler write json error!", e);
        }
    }

    @SuppressWarnings("unchecked")
    private T fromJson(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return (T) JsonUtils.getObjectMapperInstance().readValue(json, new GenericTypeReference(type));
        } catch (IOException e) {
            throw new RuntimeException("JsonTypeHandler read json error!", e);
        }
    }

    public static class GenericTypeReference extends TypeReference<Void> {
        private Type typeValue;

        public GenericTypeReference(Type type) {
            this.typeValue = type;
        }

        @Override
        public Type getType() {
            return typeValue;
        }
    }

}
