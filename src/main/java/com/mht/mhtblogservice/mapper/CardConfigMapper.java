package com.mht.mhtblogservice.mapper;

import com.mht.mhtblogservice.entity.CardConfig;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CardConfigMapper {

    /**
     * 1. 首页直调：只拉取状态开启(status=1)且分好位置的卡片
     */
    List<CardConfig> selectActiveConfigs();

    /**
     * 2. 后台直调：拉取所有的卡片池资产
     */
    List<CardConfig> selectAllConfigs();

    /**
     * 3. 🚀 黄金局部精准更新：由动态 XML 实现，哪个字段变了就更新哪个
     */
    int updateSelective(CardConfig config);
}
