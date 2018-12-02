package com.party.game.common.mybatis;

import com.party.game.common.model.ShortDate;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.Calendar;


/** 日期类型转换
 * @author yifeng
 * @version 2016/8/1
 */
public class ShortDateTypeHandler implements TypeHandler<ShortDate> {


    /**
     * 用于定义在Mybatis设置参数时该如何把Java类型的参数转换为对应的数据库类型
     * @param preparedStatement 当前的PreparedStatement对象
     * @param i 当前参数的位置
     * @param shortDate 日期类型
     * @param jdbcType 当前参数的数据库类型
     * @throws SQLException
     */
    public void setParameter(PreparedStatement preparedStatement, int i,
                             ShortDate shortDate, JdbcType jdbcType)
            throws SQLException {
        Date date = null;
        if (shortDate != null) {
            date = new Date(shortDate.toDate().getTime());
        }
        preparedStatement.setDate(i, date);
    }

    /**
     * 用于在Mybatis获取数据结果集时如何把数据库类型转换为对应的Java类型
     * @param rs 当前的结果集
     * @param columnName 当前的字段名称
     * @return 日期类型
     * @throws SQLException
     */
    public ShortDate getResult(ResultSet rs, String columnName)
            throws SQLException {
        return transferType(rs.getDate(columnName));
    }

    /**
     * 用于在Mybatis通过字段位置获取字段数据时把数据库类型转换为对应的Java类型
     * @param rs 当前的结果集
     * @param columnIndex 当前字段的位置
     * @return 日期类型
     * @throws SQLException
     */
    public ShortDate getResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return transferType(rs.getDate(columnIndex));
    }

    /**
     * 用于Mybatis在调用存储过程后把数据库类型的数据转换为对应的Java类型
     * @param cs 当前的CallableStatement执行后的CallableStatement
     * @param columnIndex 当前输出参数的位置
     * @return 日期类型
     * @throws SQLException
     */
    public ShortDate getResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return transferType(cs.getDate(columnIndex));
    }

    /**
     * 数据库时间类型和日期类型转换
     * @param inputDate 数据时间类型
     * @return 日期类型
     */
    private ShortDate transferType(final Date inputDate) {
        Date dateTransfer = new Date(inputDate.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTransfer);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new ShortDate(year, month, day);
    }
}
