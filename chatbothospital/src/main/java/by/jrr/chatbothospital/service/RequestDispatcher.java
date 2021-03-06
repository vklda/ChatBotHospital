package by.jrr.chatbothospital.service;

import by.jrr.chatbothospital.processor.HelpProcessor;
import by.jrr.chatbothospital.processor.NoneProcessor;
import by.jrr.chatbothospital.processor.SettingsProcessor;
import by.jrr.chatbothospital.processor.StartProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RequestDispatcher {
    @Autowired
    TelegramBotHospital telegramBotHospital;
    @Autowired
    MessageService messageService;
    @Autowired
    StartProcessor startProcessor;
    @Autowired
    SettingsProcessor settingsProcessor;
    @Autowired
    HelpProcessor helpProcessor;
    @Autowired
    NoneProcessor noneProcessor;

    public SendMessage dispatch(Update update) {
        switch (getCommand(update)) {
            case HELP:
                return messageService.sendMessage(update.getMessage(), helpProcessor.run());
            case START:
                return messageService.sendMessage(update.getMessage(), startProcessor.run());
            case SETTINGS:
                return messageService.sendMessage(update.getMessage(), settingsProcessor.run());
            case NONE:
                return messageService.sendMessage(update.getMessage(), noneProcessor.run());
            default:
                return messageService.sendMessage(update.getMessage(), "Do no");
        }
    }

    private BotCommands getCommand(Update update) {
        if (update != null) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String msgText = message.getText();
                if (msgText.startsWith(BotCommands.HELP.getCommand())) {
                    return BotCommands.HELP;
                } else if (msgText.startsWith(BotCommands.START.getCommand())) {
                    return BotCommands.START;
                } else if (msgText.startsWith(BotCommands.SETTINGS.getCommand())) {
                    return BotCommands.SETTINGS;
                }
            }
        }
        return BotCommands.NONE;
    }
}
