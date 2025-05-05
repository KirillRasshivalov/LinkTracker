package backend.academy.scrapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessagePreviewTest extends AbstractIntegrationTest {

    @Test
    void shouldGenerateDifferentPreviewsForDifferentTypes() {
        String commitMessage = generatePreview("commit", "Fixed bug #123");
        String answerMessage = generatePreview("answer", "You should use jpa");

        assertTrue(commitMessage.contains("Commit"));
        assertTrue(answerMessage.contains("Answer"));
    }

    private String generatePreview(String type, String content) {
        return switch (type) {
            case "commit" -> "Commit: " + content.substring(0, Math.min(50, content.length()));
            case "answer" -> "Answer: " + content.substring(0, Math.min(100, content.length()));
            default -> content;
        };
    }
}
