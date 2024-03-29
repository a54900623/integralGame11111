/*
 * Copyright 2014 Qunar.com All right reserved. This software is the
 * confidential and proprietary information of Qunar.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Qunar.com.
 */
package com.party.game.common.paging;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/** MyBatis分页拦截器
 * @author yifeng
 * @version 2016/8/1
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class PagingInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(PagingInterceptor.class);

    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
        ResultHandler<?> resultHandler = (ResultHandler<?>) invocation.getArgs()[3];

        if(rowBounds == null) {
            rowBounds = new RowBounds();
        }

        BoundSql boundSql = ms.getBoundSql(parameter);

        DataParam dataParam = null;
        try {
            if (parameter instanceof Map) {
                Map<String, Object> paramMap = (Map<String, Object>) parameter;
                if (null != paramMap) {
                    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                        if (entry.getValue() instanceof DataParam) {
                            dataParam = null != paramMap.get("dataParam") ? (DataParam) paramMap.get("dataParam") : null;
                            break;
                        }
                    }
                }
            }

            if (null != dataParam && !dataParam.getShowAll()) {
                String sql = boundSql.getSql().toLowerCase();
                String newSql;
                // 有where条件
                if (sql.indexOf("where") != -1) {
                    newSql = sql.substring(0, sql.indexOf("where"));
                    newSql += "inner join member_partner_privilege ppr on ppr.partner_id = " + dataParam.getJoinColumn() + " ";
                    newSql += sql.substring(sql.indexOf("where"));
                    sql = newSql;

                    if (sql.indexOf("group by") != -1) {
                        newSql = sql.substring(0, sql.indexOf("group by"));
                        newSql += " and ppr.member_id = '" + dataParam.getInitiatorId() + "' ";
                        newSql += sql.substring(sql.indexOf("group by"));
                        sql = newSql;
                    } else if (sql.indexOf("order by") != -1) {
                        newSql = sql.substring(0, sql.indexOf("order by"));
                        newSql += " and ppr.member_id = '" + dataParam.getInitiatorId() + "' ";
                        newSql += sql.substring(sql.indexOf("order by"));
                        sql = newSql;
                    }
                } else {
                    // 没有where条件
                    if (sql.indexOf("group by") != -1) {
                        newSql = sql.substring(0, sql.indexOf("group by"));
                        newSql += "inner join member_partner_privilege ppr on ppr.partner_id = " + dataParam.getJoinColumn() + " ";
                        newSql += "where ppr.member_id = '" + dataParam.getInitiatorId() + "' ";
                        newSql += sql.substring(sql.indexOf("group by"));
                        sql = newSql;
                    } else if (sql.indexOf("order by") != -1) {
                        newSql = sql.substring(0, sql.indexOf("order by"));
                        newSql += "inner join member_partner_privilege ppr on ppr.partner_id = " + dataParam.getJoinColumn() + " ";
                        newSql += "where ppr.member_id = '" + dataParam.getInitiatorId() + "' ";
                        newSql += sql.substring(sql.indexOf("order by"));
                        sql = newSql;
                    }
                }

                System.out.println(sql);
                if (StringUtils.isNotEmpty(sql)) {
                    setFieldValue(boundSql, "sql", sql);
                }
            }
        } catch (Exception e) {
            logger.debug("dataParam is not exists");
        }

        CacheKey key = executor.createCacheKey(ms, parameter, rowBounds, boundSql);

        if (rowBounds instanceof Page) {
            // 执行count操作
            String sql = MySQLHelper.count(boundSql.getSql());
            logger.debug("generate count total rows sql: {}", StringUtils.replacePattern(sql, "\\s+", " "));
            int total = query(executor, ms, parameter, boundSql, sql);
            sql = MySQLHelper.limit(boundSql.getSql(), rowBounds.getOffset(), rowBounds.getLimit());
            Page page = (Page) rowBounds;
            page.setTotalCount(total);
            page.setPage(page.computePageNo());

            setFieldValue(boundSql, "sql", sql);
        }

        rowBounds = RowBounds.DEFAULT; // 清除翻页参数，禁止DefaultResultSetHandler#skipRows跳过结果集
        return executor.query(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

    private int query(Executor executor, MappedStatement statement, Object parameter, BoundSql boundSql, String sql) {
        if (executor instanceof CachingExecutor) {
            executor = getFieldValue(executor, "delegate");
        }

        if (executor instanceof SimpleExecutor) {
            try {
                BoundSql newBoundSql = new BoundSql(statement.getConfiguration(), sql, boundSql.getParameterMappings(),
                        boundSql.getParameterObject());
                setFieldValue(newBoundSql, "metaParameters", getFieldValue(boundSql, "metaParameters"));
                MappedStatement newStatement = copyFromMappedStatement(statement, newBoundSql);
                List<Integer> result = ((SimpleExecutor) executor).doQuery(newStatement, parameter, RowBounds.DEFAULT,
                        Executor.NO_RESULT_HANDLER, newBoundSql);
                if (result.size() == 1)
                    return result.get(0);
            } catch (SQLException e) {
                logger.error("query count total rows failure, origin sql:{}, generated sql:{}", boundSql.getSql(), sql,
                        e);
            }
        }
        return 0;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, final BoundSql newBoundSql) {
        // 参考MapperBuilderAssistant
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId()
                + "-Count", new SqlSource() {
            public BoundSql getBoundSql(Object parameterObject) {
                return newBoundSql;
            }
        }, ms.getSqlCommandType());
        statementBuilder.resource(ms.getResource());
        statementBuilder.fetchSize(ms.getFetchSize());
        statementBuilder.statementType(ms.getStatementType());
        statementBuilder.keyGenerator(ms.getKeyGenerator());
        statementBuilder.keyProperty(join(ms.getKeyProperties()));
        statementBuilder.keyColumn(join(ms.getKeyColumns()));
        statementBuilder.databaseId(ms.getDatabaseId());
        statementBuilder.lang(ms.getLang());
        statementBuilder.resultOrdered(ms.isResultOrdered());
        statementBuilder.resulSets(join(ms.getResulSets()));

        // setStatementTimeout
        statementBuilder.timeout(ms.getTimeout());
        // setStatementParameterMap
        statementBuilder.parameterMap(ms.getParameterMap());
        // setStatementResultMap
        statementBuilder.resultSetType(ms.getResultSetType());
        // setStatementCache
        statementBuilder.flushCacheRequired(ms.isFlushCacheRequired());
        statementBuilder.useCache(ms.isUseCache());
        statementBuilder.cache(ms.getCache());

        // 修改结果集类型为int
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), "Count", Integer.class,
                new ArrayList<ResultMapping>(), null).build();
        statementBuilder.resultMaps(Lists.newArrayList(resultMap));

        return statementBuilder.build();
    }

    private String join(String[] items) {
        if (items == null || items.length == 0)
            return null;
        return StringUtils.join(items);
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = ReflectionUtils.findField(object.getClass(), fieldName);
        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, object, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String fieldName) {
        Field field = ReflectionUtils.findField(object.getClass(), fieldName);
        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        ReflectionUtils.makeAccessible(field);
        return (T) ReflectionUtils.getField(field, object);
    }

}
