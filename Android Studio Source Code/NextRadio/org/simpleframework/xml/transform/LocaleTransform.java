package org.simpleframework.xml.transform;

import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.activemq.transport.stomp.Stomp;

class LocaleTransform implements Transform<Locale> {
    private final Pattern pattern;

    public LocaleTransform() {
        this.pattern = Pattern.compile(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
    }

    public Locale read(String locale) throws Exception {
        String[] list = this.pattern.split(locale);
        if (list.length >= 1) {
            return read(list);
        }
        throw new InvalidFormatException("Invalid locale %s", locale);
    }

    private Locale read(String[] locale) throws Exception {
        String[] list = new String[]{Stomp.EMPTY, Stomp.EMPTY, Stomp.EMPTY};
        for (int i = 0; i < list.length; i++) {
            if (i < locale.length) {
                list[i] = locale[i];
            }
        }
        return new Locale(list[0], list[1], list[2]);
    }

    public String write(Locale locale) {
        return locale.toString();
    }
}
