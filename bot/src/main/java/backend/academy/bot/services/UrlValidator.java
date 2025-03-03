package backend.academy.bot.services;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Класс для проверки ссылки на валидность.
 */
public class UrlValidator {
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
