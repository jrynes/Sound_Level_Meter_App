package com.onelouder.adlib;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class AdEventsCache extends DataCache {
    private static AdEventsCache _instance;
    private static final ArrayList<String> mEvents;
    private static final Object mLock;

    class EventsHandler extends DefaultHandler {
        private StringBuilder sbToken;

        EventsHandler() {
            this.sbToken = null;
        }

        public void startElement(String uri, String name, String qName, Attributes atts) {
            if (name.equals("e")) {
                this.sbToken = new StringBuilder();
            }
        }

        public void endElement(String uri, String name, String qName) throws SAXException {
            if (this.sbToken != null && AdEventsCache.mEvents.size() < ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) {
                AdEventsCache.mEvents.add(this.sbToken.toString());
            }
            this.sbToken = null;
        }

        public void characters(char[] ch, int start, int length) {
            if (this.sbToken != null) {
                this.sbToken.append(ch, start, length);
            }
        }
    }

    private AdEventsCache() {
    }

    static {
        mLock = new Object();
        mEvents = new ArrayList();
    }

    public static AdEventsCache getInstance(Context context) {
        AdEventsCache adEventsCache;
        synchronized (mLock) {
            if (_instance == null) {
                _instance = new AdEventsCache();
                _instance.LoadFromCache(context);
            }
            adEventsCache = _instance;
        }
        return adEventsCache;
    }

    protected String TAG() {
        return "AdEventsCache";
    }

    protected String getFilename() {
        return "onelouder-adlib.dat";
    }

    public DefaultHandler getParser() {
        return new EventsHandler();
    }

    public void onContentParsed(Object result) {
    }

    public void addEvent(ArrayList<String> events) {
        synchronized (mLock) {
            mEvents.addAll(events);
        }
    }

    public void addEvent(String event) {
        synchronized (mLock) {
            mEvents.add(event);
        }
    }

    public int size() {
        int size;
        synchronized (mLock) {
            size = mEvents.size();
        }
        return size;
    }

    public String getNextEvent() {
        String event;
        synchronized (mLock) {
            event = null;
            if (mEvents.size() > 0) {
                event = (String) mEvents.remove(0);
            }
        }
        return event;
    }

    public void SaveCache(Context context) {
        synchronized (mLock) {
            StringBuilder sb = new StringBuilder();
            sb.append("<events>");
            Iterator i$ = mEvents.iterator();
            while (i$.hasNext()) {
                String event = (String) i$.next();
                sb.append("<e>");
                sb.append(event);
                sb.append("</e>");
            }
            sb.append("</events>");
            save(context, sb.toString().getBytes());
        }
    }
}
