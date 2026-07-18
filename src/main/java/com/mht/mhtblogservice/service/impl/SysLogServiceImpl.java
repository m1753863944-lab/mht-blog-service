package com.mht.mhtblogservice.service.impl;
import com.mht.mhtblogservice.entity.SysLog;
import com.mht.mhtblogservice.mapper.SysLogMapper;
import com.mht.mhtblogservice.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    @Async // 🚀 关键：标记为异步方法，主线程瞬间返回，日志写入在后台由虚拟线程默默执行
    public void saveLog(SysLog sysLog) {
        sysLogMapper.insertLog(sysLog);
    }
}
