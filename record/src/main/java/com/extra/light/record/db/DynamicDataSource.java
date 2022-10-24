package com.extra.light.record.db;

import com.extra.light.record.enums.DateBaseType;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author 林树毅
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static String dateBaseType = DateBaseType.SQL_SERVER_ONE.name();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object,Object> targetDataSource){
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Object dateBase = getDateBase();
        if (dateBase == null){
            return DateBaseType.SQL_SERVER_ONE.name();
        }
        return dateBase;
    }

    private Object getDateBase() {
        return dateBaseType;
    }

    public static void setDateBaseType(String dateBaseType){
        DynamicDataSource.dateBaseType = dateBaseType;
    }
}
