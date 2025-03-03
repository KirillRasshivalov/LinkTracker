package backend.academy.dto;

import java.util.List;

public class BadResponseDTO {
    String description;
    String code;
    String exceptionName;
    String exceptionMessage;
    List<String> stacktrace;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String toString() {
        return "BadResponseDTO{" +
            "description='" + description + '\'' +
            ", code='" + code + '\'' +
            ", exceptionName='" + exceptionName + '\'' +
            ", exceptionMessage='" + exceptionMessage + '\'' +
            ", stacktrace=" + stacktrace +
            '}';
    }

    public void setStacktrace(List<String> stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public List<String> getStacktrace() {
        return stacktrace;
    }
}
