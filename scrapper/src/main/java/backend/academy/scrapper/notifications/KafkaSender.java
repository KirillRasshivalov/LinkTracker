package backend.academy.scrapper.notifications;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.dto.MainInfoFromGithubDTO;
import backend.academy.dto.MainInfoFromStackOverlowDTO;
import backend.academy.scrapper.kafka.Producer;
import backend.academy.scrapper.managers.CheckLink;
import backend.academy.scrapper.services.ServerLogger;
import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * Реализация метода отправки сообщения по средствам kafka
 */
@RequiredArgsConstructor
public class KafkaSender implements Sender {
    private final CheckLink checkLink = new CheckLink();
    private final Producer producer;

    @Override
    public void sendNotification(String link, List<Long> IDS, Object info) {
        if (checkLink.isGitHubLink(link)) {
            MainInfoFromGithubDTO mainInfoFromGithubDTO = (MainInfoFromGithubDTO) info;
            for (int i = 0; i < IDS.size(); i++) {
                LinkUpdateRequestDTO linkUpdateRequestDTO = new LinkUpdateRequestDTO();
                linkUpdateRequestDTO.setUrl(link);
                linkUpdateRequestDTO.setId(IDS.get(i));
                linkUpdateRequestDTO.setTgChatIds(IDS);
                linkUpdateRequestDTO.setDescription("Пришло обновление по ссылке: " + link + ".\n" + "Автор: "
                    + mainInfoFromGithubDTO.authorName() + ".\n" + "Содержание: "
                    + mainInfoFromGithubDTO.message() + ".\n" + "Название " + mainInfoFromGithubDTO.type() + ": "
                    + mainInfoFromGithubDTO.nameOfAnswer() + ".\n" + "Время последнего коммита: "
                    + mainInfoFromGithubDTO.createdAt() + ".\n");
                try {
                    producer.sendMessage(linkUpdateRequestDTO, false);
                } catch (Exception e) {
                    ServerLogger.LOGGER.atError().setMessage(e.getMessage()).log();
                }
                ServerLogger.LOGGER
                    .atInfo()
                    .setMessage("Отправил сообщение в очередь.")
                    .log();
            }
        } else {
            MainInfoFromStackOverlowDTO mainInfoFromStackOverlowDTO = (MainInfoFromStackOverlowDTO) info;
            for (int i = 0; i < IDS.size(); i++) {
                LinkUpdateRequestDTO linkUpdateRequestDTO = new LinkUpdateRequestDTO();
                linkUpdateRequestDTO.setUrl(link);
                linkUpdateRequestDTO.setId(IDS.get(i));
                linkUpdateRequestDTO.setTgChatIds(IDS);
                linkUpdateRequestDTO.setDescription("Пришло обновление по ссылке: " + link + ".\n" + "Автор: "
                    + mainInfoFromStackOverlowDTO.name() + ".\n" + "Тема: "
                    + mainInfoFromStackOverlowDTO.theme() + ".\n" + "Комментарий: "
                    + mainInfoFromStackOverlowDTO.answer() + ".\n" + "Время последнего коммита: "
                    + mainInfoFromStackOverlowDTO.time() + ".\n");
                String botUrl = "http://localhost:8080/updates";
                try {
                    producer.sendMessage(linkUpdateRequestDTO, false);
                } catch (Exception e) {
                    ServerLogger.LOGGER.atError().setMessage(e.getMessage()).log();
                }
                ServerLogger.LOGGER
                    .atInfo()
                    .setMessage("Отправил сообщение в очередь")
                    .log();
            }
        }
    }
}
