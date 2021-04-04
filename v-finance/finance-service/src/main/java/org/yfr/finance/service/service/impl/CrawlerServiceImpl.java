package org.yfr.finance.service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.yfr.finance.core.component.bot.TelegramBot;
import org.yfr.finance.core.pojo.dto.OperationResponse;
import org.yfr.finance.service.service.CrawlerService;

import javax.annotation.Resource;

@Slf4j
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private TelegramBot telegramBot;

    @Value("${bot.chatId}")
    private String chatId;

    @Override
    public OperationResponse fetchAmericaIdx() {
        String body = restTemplate.exchange("https://tw.stock.yahoo.com/us/worldidx.php", HttpMethod.GET, null, String.class).getBody();

        String range = body.substring(body.indexOf("美加"), body.indexOf("歐洲"));

        String dji = range.substring(range.indexOf("道瓊"), range.indexOf("那斯"));
        String dji_n = dji.substring(dji.indexOf("</td>") + 6, dji.indexOf("</tr>"));
        String[] dji_arr = dji_n.replace("<td align=center nowrap>", "")
                .replace("<td align=center id=\"trade\" nowrap><b>", "")
                .replace("<td align=center nowrap>", "")
                .replace("</td>", "")
                .replace("</b>", "")
                .replace("<font color=green>", "")
                .replace("<font color=red>", "")
                .replace("</font>", "")
                .split("\n");

        String ixic = range.substring(range.indexOf("那斯"), range.indexOf("史坦"));
        String ixic_n = ixic.substring(ixic.indexOf("</td>") + 6, ixic.indexOf("</tr>"));
        String[] ixic_arr = ixic_n.replace("<td align=center nowrap>", "")
                .replace("<td align=center id=\"trade\" nowrap><b>", "")
                .replace("<td align=center nowrap>", "")
                .replace("</td>", "")
                .replace("</b>", "")
                .replace("<font color=green>", "")
                .replace("<font color=red>", "")
                .replace("</font>", "")
                .split("\n");

        String spx = range.substring(range.indexOf("史坦"), range.indexOf("費城"));
        String spx_n = spx.substring(spx.indexOf("</td>") + 6, spx.indexOf("</tr>"));
        String[] spx_arr = spx_n.replace("<td align=center nowrap>", "")
                .replace("<td align=center id=\"trade\" nowrap><b>", "")
                .replace("<td align=center nowrap>", "")
                .replace("</td>", "")
                .replace("</b>", "")
                .replace("<font color=green>", "")
                .replace("<font color=red>", "")
                .replace("</font>", "")
                .split("\n");

        String sox = range.substring(range.indexOf("費城"), range.indexOf("羅素"));
        String sox_n = sox.substring(sox.indexOf("</td>") + 6, sox.indexOf("</tr>"));
        String[] sox_arr = sox_n.replace("<td align=center nowrap>", "")
                .replace("<td align=center id=\"trade\" nowrap><b>", "")
                .replace("<td align=center nowrap>", "")
                .replace("</td>", "")
                .replace("</b>", "")
                .replace("<font color=green>", "")
                .replace("<font color=red>", "")
                .replace("</font>", "")
                .split("\n");

        String rut = range.substring(range.indexOf("羅素"), range.indexOf("加拿"));
        String rut_n = rut.substring(sox.indexOf("</td>") + 6, rut.indexOf("</tr>"));
        String[] rut_arr = rut_n.replace("<td align=center nowrap>", "")
                .replace("<td align=center id=\"trade\" nowrap><b>", "")
                .replace("<td align=center nowrap>", "")
                .replace("</td>", "")
                .replace("</b>", "")
                .replace("<font color=green>", "")
                .replace("<font color=red>", "")
                .replace("</font>", "")
                .split("\n");

        StringBuilder builder = new StringBuilder();
        builder.append("道瓊").append("\t").append(dji_arr[1]).append("\t").append(dji_arr[2]).append("\t").append(dji_arr[3]).append("\n\n");
        builder.append("那斯").append("\t").append(ixic_arr[1]).append("\t").append(ixic_arr[2]).append("\t").append(ixic_arr[3]).append("\n\n");
        builder.append("史坦").append("\t").append(spx_arr[1]).append("\t").append(spx_arr[2]).append("\t").append(spx_arr[3]).append("\n\n");
        builder.append("費城").append("\t").append(sox_arr[1]).append("\t").append(sox_arr[2]).append("\t").append(sox_arr[3]).append("\n\n");
        builder.append("羅素").append("\t").append(rut_arr[1]).append("\t").append(rut_arr[2]).append("\t").append(rut_arr[3]).append("\n\n");

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(builder.toString());
        telegramBot.sendAsync(sendMessage);

        return OperationResponse.SUCCESS_RESPONSE;
    }

}
