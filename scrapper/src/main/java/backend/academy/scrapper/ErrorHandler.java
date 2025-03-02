package backend.academy.scrapper;

import backend.academy.dto.BadResponseDTO;

public class ErrorHandler {

    public static BadResponseDTO sameLinkError() {
        BadResponseDTO badResponseDTO = new BadResponseDTO();
        badResponseDTO.setCode("400");
        badResponseDTO.setDescription("Такая ссылка уже отслеживается.");
        badResponseDTO.setExceptionMessage("The same link execption.");
        return badResponseDTO;
    }
}
