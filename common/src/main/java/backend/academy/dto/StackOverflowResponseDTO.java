package backend.academy.dto;

import java.util.List;

/** Дтошка для ответа от стека оверфлоу. */
public class StackOverflowResponseDTO {
    private List<Question> items;

    public List<Question> getItems() {
        return items;
    }

    public void setItems(List<Question> items) {
        this.items = items;
    }

    public static class Question {
        private String title;
        private Owner owner;
        private long creation_date;
        private List<Answer> answers;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public long getCreation_date() {
            return creation_date;
        }

        public void setCreation_date(long creation_date) {
            this.creation_date = creation_date;
        }

        public List<Answer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<Answer> answers) {
            this.answers = answers;
        }
    }

    public static class Owner {
        private String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    public static class Answer {
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
