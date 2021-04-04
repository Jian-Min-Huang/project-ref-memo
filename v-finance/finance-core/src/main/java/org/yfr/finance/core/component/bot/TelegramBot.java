package org.yfr.finance.core.component.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info("{} received message and ignore. Message:{}", this.getClass().getSimpleName(), update.getMessage().getText());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendAsync(SendMessage sendMessage) {
        Mono.just(sendMessage).delayElement(Duration.ofSeconds(5))
                .doOnNext(method -> {
                    try {
                        execute(method);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnError(ex -> log.warn("Sending Telegram message failed! {}", ex.getMessage()))
                .doOnSuccess(d -> log.info("Sent Telegram message: {}", d))
                .retry(5)
                .subscribe();
    }
}
