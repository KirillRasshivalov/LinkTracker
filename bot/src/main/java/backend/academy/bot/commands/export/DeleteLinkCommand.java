//package backend.academy.bot.commands;
//
//import backend.academy.dto.BadResponseDTO;
//import backend.academy.dto.AddLinkResponseDTO;
//import backend.academy.dto.AddLinkRequestDTO;
//import backend.academy.loggs.LoggFactory;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pengrad.telegrambot.model.Update;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//public class DeleteLinkCommand implements ServerCommands{
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public String applyCommand(String link, Update update) {
//        AddLinkResponseDTO collectionUpdateRequestDTO = new AddLinkResponseDTO();
//        //collectionUpdateRequestDTO.setLink(link);
//        String serverUrl = "http://localhost:8081/links";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("tg-chat-id", update.message().chat().id().toString());
//        HttpEntity<AddLinkResponseDTO> requestEntity = new HttpEntity<>(collectionUpdateRequestDTO, headers);
//
//        try {
//            ResponseEntity<?> response = restTemplate.postForEntity(
//                serverUrl,
//                requestEntity,
//                String.class
//            );
//            if (response.hasBody()) {
//                if (response.getStatusCode().is2xxSuccessful()) {
//                    AddLinkRequestDTO collectionUpdateResponseDTO = objectMapper.readValue(
//                        response.getBody().toString(),
//                        AddLinkRequestDTO.class
//                    );
//                    return collectionUpdateResponseDTO.getMessage();
//                } else if (response.getStatusCode().is4xxClientError()) {
//                    BadResponseDTO badResponseDTO = objectMapper.readValue(
//                        response.getBody().toString(),
//                        BadResponseDTO.class
//                    );
//                    return badResponseDTO.getExceptionName();
//                } else {
//                    LoggFactory.addLog("Неопознаная ошибка" + response.getStatusCode());
//                    return "Что то пошло не так.";
//                }
//            }
//        } catch (Exception e) {
//            LoggFactory.addLog(e.getMessage());
//        }
//        return null;
//    }
//}
