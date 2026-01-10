package sti.project.template.base.i18n;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Internationalization (i18n) configuration for REST API (stateless).
 * Supports multiple languages via 'lang' query parameter or Accept-Language
 * header.
 * 
 * Priority:
 * 1. Query param: ?lang=vi
 * 2. Accept-Language header
 * 3. Default: vi
 * 
 * Usage:
 * - GET /api/examples?lang=vi → Tiếng Việt
 * - GET /api/examples?lang=en → English
 * - Header: Accept-Language: vi
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    private static final Locale DEFAULT_LOCALE = new Locale("vi");
    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            new Locale("vi"),
            new Locale("en"));

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                // Priority 1: Check query parameter ?lang=vi
                String langParam = request.getParameter("lang");
                if (StringUtils.hasText(langParam)) {
                    Locale locale = Locale.forLanguageTag(langParam);
                    if (SUPPORTED_LOCALES.contains(locale)) {
                        return locale;
                    }
                }

                // Priority 2: Check Accept-Language header
                String acceptLanguage = request.getHeader("Accept-Language");
                if (StringUtils.hasText(acceptLanguage)) {
                    Locale locale = Locale.forLanguageTag(acceptLanguage.split(",")[0].trim());
                    if (SUPPORTED_LOCALES.contains(locale)) {
                        return locale;
                    }
                }

                // Priority 3: Default locale
                return DEFAULT_LOCALE;
            }
        };
    }
}
