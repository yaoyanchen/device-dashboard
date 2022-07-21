package com.easy.dashboard.utils;

import cn.hutool.extra.mail.MailUtil;
import com.easy.dashboard.model.EmailDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmailUtil {

    @Async
    public void sendEmail(EmailDTO emailDTO) {
        MailUtil.send(emailDTO.getEmail(), emailDTO.getTitle(), emailDTO.getContent(), false);
    }

    @Async
    public  void sendEmail(List<EmailDTO> emailDTOList) {
        for (EmailDTO emilDTO : emailDTOList) {
            MailUtil.send(emilDTO.getEmail(), emilDTO.getTitle(), emilDTO.getContent(), false);
        }
    }

    @Async
    public void sendHtmlEmail(EmailDTO emailDTO) {
        MailUtil.send(emailDTO.getEmail(), emailDTO.getTitle(), emailDTO.getContent(), true);
    }

    @Async
    public void sendHtmlEmail(List<EmailDTO> emailDTOList) {
        for (EmailDTO emilDTO : emailDTOList) {
            MailUtil.send(emilDTO.getEmail(), emilDTO.getTitle(), emilDTO.getContent(), true);
        }
    }
}
