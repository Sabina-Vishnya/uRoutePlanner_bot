package com.example.urouteplanner_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSetting {
    private long userId;
    private long chatId;
    private String username;
    private String pointA; //todo rename
    private String pointB;  //todo rename
    private String pointTime;   //todo rename
}
