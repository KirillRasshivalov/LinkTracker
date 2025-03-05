package backend.academy.scrapper.managers;

import backend.academy.dto.BadResponseDTO;

import java.util.List;

/**
 * Класс кастомных ответов на ошибки которые произошли на сервере.
 */
public class ErrorHandler {

    public static BadResponseDTO sameLinkError() {
        BadResponseDTO badResponseDTO = new BadResponseDTO();
        badResponseDTO.setCode("400");
        badResponseDTO.setDescription("Такая ссылка уже отслеживается.");
        badResponseDTO.setExceptionMessage("The same link execption.");
        badResponseDTO.setExceptionName("Link");
        badResponseDTO.setStacktrace(List.of("linkError"));
        return badResponseDTO;
    }

    public static BadResponseDTO chatHasAlreadyExist() {
        BadResponseDTO badResponseDTO = new BadResponseDTO();
        badResponseDTO.setCode("400");
        badResponseDTO.setDescription("Вы уже зарегистрированы.");
        return badResponseDTO;
    }

    public static BadResponseDTO userNotExist() {
        BadResponseDTO badResponseDTO = new BadResponseDTO();
        badResponseDTO.setCode("400");
        badResponseDTO.setDescription("Данного пользователя не существует.");
        return badResponseDTO;
    }

    public static BadResponseDTO userHasNoLinks() {
        BadResponseDTO badResponseDTO = new BadResponseDTO();
        badResponseDTO.setCode("400");
        badResponseDTO.setDescription("У вас нет никаких ссылок сейчас.");
        return badResponseDTO;
    }

    public static BadResponseDTO linkDoesntExist() {
        BadResponseDTO badResponseDTO = new BadResponseDTO();
        badResponseDTO.setCode("400");
        badResponseDTO.setDescription("Такой ссылки вы не отслеживали.");
        return badResponseDTO;
    }

    public static BadResponseDTO unrespectedError() {
        BadResponseDTO badResponseDTO = new BadResponseDTO();
        badResponseDTO.setCode("400");
        badResponseDTO.setDescription("Произошла непредвиденная ошибка.");
        return badResponseDTO;
    }
}
