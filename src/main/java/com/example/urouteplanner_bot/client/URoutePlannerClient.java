package com.example.urouteplanner_bot.client;

import com.example.urouteplanner_bot.model.UserSetting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "uRoutePlanner", url = "http://localhost:8081")
public interface URoutePlannerClient {

    @PostMapping("/setting")
    void processUserSetting(@RequestBody UserSetting userSetting);
}