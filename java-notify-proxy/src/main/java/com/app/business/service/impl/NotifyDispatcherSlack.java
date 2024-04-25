package com.app.business.service.impl;

import com.app.business.config.AppConfig;
import com.app.business.model.ofservice.NotifyEventItem;
import com.app.business.model.ofservice.NotifyForwarder;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class NotifyDispatcherSlack extends NotifyDispatcherLogging {

    public NotifyDispatcherSlack(AppConfig appConfig) {
        super(appConfig);
    }

    @Override
    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.Slack;
    }

    @Override
    public void dispatcher(NotifyForwarder forwarder, NotifyEventItem eventItem) {
        String token = "";

        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods(token);

        String messageText = "Sua mensagem aqui!";
        String channel = eventItem.getDestination(); // Ou "@usuário" para mensagens diretas

        // Crie a solicitação para enviar a mensagem
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(channel)
                .text(messageText)
                .build();

        // Envie a mensagem e capture a resposta
        try {
            ChatPostMessageResponse response = methods.chatPostMessage(request);
            System.out.println("Mensagem enviada com sucesso! Timestamp: " + response.getTs());
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }

}
