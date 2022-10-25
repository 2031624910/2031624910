package com.extra.light.record.dao.mapper;

import com.extra.light.record.annotation.ReaderSql;
import com.extra.light.record.annotation.WriterSql;

/**
 * @author 林树毅
 */
public interface TestMapper {
    /**
     * 测试用
     * @return
     */
    @ReaderSql
    String getUserId();

    /**
     * 测试用
     * @return
     */
    @WriterSql
    String getWriterUserId();
}
