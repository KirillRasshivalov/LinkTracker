package backend.academy.dto;

import java.util.List;

public class StackOverflowResponseDTO {
    private List<Question> items;

    public static class Question {
        private String title;
        private String link;
        private long creation_date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public long getCreation_date() {
            return creation_date;
        }

        public void setCreation_date(long creation_date) {
            this.creation_date = creation_date;
        }
    }

    public List<Question> getItems() {
        return items;
    }

    public void setItems(List<Question> items) {
        this.items = items;
    }
}
