package com.app.business.service.impl;

import com.app.business.model.ofservice.NotifyForwarder;

public class NotifyDispatcherTelegram extends NotifyDispatcherNone {

    @Override
    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.Telegram;
    }

//    @Override
//    public void dispatcherx(NotifyDispatcherX setting, EventNotifyService.Event event) {
//        String botToken = "SEU_TOKEN_DO_BOT";
//        String chatId = "ID_DO_CHAT";
//        String message = "Mensagem de teste do seu bot.";
//
//        try {
//            URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            // Configurando o método HTTP
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//
//            // Criando os parâmetros da requisição
//            String parametros = "chat_id=" + chatId + "&text=" + message;
//            byte[] postData = parametros.getBytes(StandardCharsets.UTF_8);
//
//            // Configurando o cabeçalho da requisição
//            connection.setRequestProperty("Content-Length", String.valueOf(postData.length));
//            connection.getOutputStream().write(postData);
//
//            // Obtendo a resposta
//            int responseCode = connection.getResponseCode();
//            System.out.println("Código de resposta: " + responseCode);
//
//            connection.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
