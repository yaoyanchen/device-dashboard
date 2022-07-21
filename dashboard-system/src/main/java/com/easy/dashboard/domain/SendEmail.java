package com.easy.dashboard.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SendEmail {
    private Integer id;

    private String sendTo;

    private String sendContent;

    private String sendCode;

    private Date sendTime;

    public SendEmail(String sendTo, String sendContent, String sendCode, Date sendTime) {
        this.sendTo = sendTo;
        this.sendContent = sendContent;
        this.sendCode = sendCode;
        this.sendTime = sendTime;
    }
}