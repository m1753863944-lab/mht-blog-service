package com.mht.mhtblogservice.mapper;

import com.mht.mhtblogservice.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLogMapper {
    int insertLog(SysLog sysLog);
}
