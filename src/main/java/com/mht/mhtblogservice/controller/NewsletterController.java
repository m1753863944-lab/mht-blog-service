package com.mht.mhtblogservice.controller;

import com.mht.mhtblogservice.entity.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/newsletter")
@CrossOrigin // 确保跨域正常
@Slf4j
public class NewsletterController {

    @PostMapping("/subscribe")
    public ResultVo<?> subscribe(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("收到新周刊订阅通知:{}",email);
        return ResultVo.success("SUBSCRIBE_SUCCESS");
    }
}
