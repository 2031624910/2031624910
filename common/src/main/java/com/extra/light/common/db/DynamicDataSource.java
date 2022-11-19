package com.extra.light.common.db;

import com.extra.light.common.enums.DateBaseType;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author 林树毅
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static String dateBaseType = DateBaseType.READER.name();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object,Object> targetDataSource){
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Object dateBase = getDateBase();
        if (dateBase == null){
            return DateBaseType.READER.name();
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
