package sti.project.template.base.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Helper component for getting localized messages.
 * Use this instead of directly injecting MessageSource for cleaner code.
 */
@Component
@RequiredArgsConstructor
public class MessageHelper {

    private final MessageSource messageSource;

    /**
     * Get message by key using current locale.
     */
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, key, getLocale());
    }

    /**
     * Get message by key with arguments using current locale.
     * Example: getMessage("error.blank_field", "Email") -> "Email is required" (en)
     * / "Email là bắt buộc" (vi)
     */
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, key, getLocale());
    }

    /**
     * Get message by key with specific locale.
     */
    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }

    /**
     * Get message by key with arguments and specific locale.
     */
    public String getMessage(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, key, locale);
    }

    /**
     * Get current locale from context.
     */
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * Get current language code (e.g., "vi", "en").
     */
    public String getCurrentLanguage() {
        return getLocale().getLanguage();
    }
}
