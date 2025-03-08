package backend.academy.bot.managers.links;

import backend.academy.dto.ShowListResponseDTO;

/**
 * Класс для парсинга выходной информации об ссылках пользователя со скрапера.
 */
public class LinksDataParser {

    public static String parseInfo(ShowListResponseDTO showListResponseDTO) {
        StringBuilder answer = new StringBuilder("Список отслеживаемых ссылок:\n");
        for (int i = 0; i < showListResponseDTO.links.size(); i++) {
            answer.append("Ссылка: ").append(showListResponseDTO.links.get(i).url()).append("\n");
            answer.append("Теги: ").append(showListResponseDTO.links.get(i).tags()).append("\n");
            answer.append("Фильтры: ").append(showListResponseDTO.links.get(i).filters()).append("\n");
            answer.append("\n");
        }
        return answer.toString();
    }
}
