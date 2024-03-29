package com.cursosrecomendados.telegram.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class LanguagesService {
	private static final String STRINGS_FILE = "string";
    private static final Object lock = new Object();

    private static final List<Language> supportedLanguages = new ArrayList<>();
    private static final Utf8ResourceBundle defaultLanguage;
    private static final Utf8ResourceBundle spanish;

    static {
        synchronized (lock) {
            defaultLanguage = new Utf8ResourceBundle(STRINGS_FILE, Locale.ROOT);
            supportedLanguages.add(new Language("en", "English"));
            spanish = new Utf8ResourceBundle(STRINGS_FILE, new Locale("es", "ES"));
            supportedLanguages.add(new Language("es", "Español"));
        }
    }

    
    public static String getString(String key) {
        String result;
        try {
            result = defaultLanguage.getString(key);
        } catch (MissingResourceException e) {
            result = "String not found";
        }

        return result;
    }


    public static String getString(String key, String language) {
        String result;
        try {
            switch (language.toLowerCase()) {
                case "es":
                    result = spanish.getString(key);
                    break;
                default:
                    result = defaultLanguage.getString(key);
                    break;
            }
        } catch (MissingResourceException e) {
            result = defaultLanguage.getString(key);
        }

        return result;
    }

    public static List<Language> getSupportedLanguages() {
        return supportedLanguages;
    }

    public static Language getLanguageByCode(String languageCode) {
        return supportedLanguages.stream().filter(x -> x.getCode().equals(languageCode)).findFirst().orElse(null);
    }

    public static Language getLanguageByName(String languageName) {
        return supportedLanguages.stream().filter(x -> x.getName().equals(languageName)).findFirst().orElse(null);
    }

    public static String getLanguageCodeByName(String language) {
        return supportedLanguages.stream().filter(x -> x.getName().equals(language))
                .map(Language::getCode).findFirst().orElse(null);
    }

    public static class Language {
        private String code;
        private String name;

        public Language(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Utf8ResourceBundle extends ResourceBundle {

        private static final String BUNDLE_EXTENSION = "properties";
        private static final String CHARSET = "UTF-8";
        private static final ResourceBundle.Control UTF8_CONTROL = new UTF8Control();

        Utf8ResourceBundle(String bundleName, Locale locale) {
            setParent(ResourceBundle.getBundle(bundleName, locale, UTF8_CONTROL));
        }

        @Override
        protected Object handleGetObject(String key) {
            return parent.getObject(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            return parent.getKeys();
        }

        private static class UTF8Control extends Control {
            public ResourceBundle newBundle
                    (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                    throws IllegalAccessException, InstantiationException, IOException {
                String bundleName = toBundleName(baseName, locale);
                String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
                ResourceBundle bundle = null;
                InputStream stream = null;
                if (reload) {
                    URL url = loader.getResource(resourceName);
                    if (url != null) {
                        URLConnection connection = url.openConnection();
                        if (connection != null) {
                            connection.setUseCaches(false);
                            stream = connection.getInputStream();
                        }
                    }
                } else {
                    stream = loader.getResourceAsStream(resourceName);
                }
                if (stream != null) {
                    try {
                        bundle = new PropertyResourceBundle(new InputStreamReader(stream, CHARSET));
                    } finally {
                        stream.close();
                    }
                }
                return bundle;
            }
        }
    }
}
