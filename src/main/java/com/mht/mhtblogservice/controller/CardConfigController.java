package com.mht.mhtblogservice.controller;

import com.mht.mhtblogservice.entity.ResultVo;
import com.mht.mhtblogservice.entity.CardConfig;
import com.mht.mhtblogservice.mapper.CardConfigMapper;
import com.mht.mhtblogservice.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blog/cards") // 🚀 对齐您的组件路径前缀
@CrossOrigin
public class CardConfigController {

    @Autowired
    private CardConfigMapper cardConfigMapper;

    /**
     * 1. 首页直调（您已有的接口）
     * GET /api/blog/cards/config
     */
    @GetMapping("/config")
    public ResultVo<List<CardConfig>> getCardConfigs() {
        return ResultVo.success(cardConfigMapper.selectActiveConfigs());
    }

    /**
     * 2. 后台管理直调（新增：拉取库里全部7条记录）
     * GET /api/blog/cards/all
     */
    @GetMapping("/all")
    public ResultVo<List<CardConfig>> getAllConfigs() {
        List<CardConfig> configs = cardConfigMapper.selectAllConfigs();
        if (configs == null || configs.isEmpty()) {
            throw new BusinessException("CARD_CONFIG_NOT_INITIALIZED");
        }
        return ResultVo.success(configs);
    }

    /**
     * 3. 脏数据精准局部更新
     * POST /api/blog/cards/update-selective
     */
    @PostMapping("/update-selective")
    public ResultVo<Boolean> updateSelective(@RequestBody List<CardConfig> dirtyConfigs) {
        if (dirtyConfigs == null || dirtyConfigs.isEmpty()) {
            return ResultVo.success(true);
        }
        for (CardConfig config : dirtyConfigs) {
            if (config.getId() == null) {
                throw new BusinessException("MISSING_PRIMARY_KEY_ID_FOR_UPDATE");
            }
            cardConfigMapper.updateSelective(config);
        }
        return ResultVo.success(true);
    }
}
