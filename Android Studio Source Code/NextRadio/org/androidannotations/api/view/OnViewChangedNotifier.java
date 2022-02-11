package org.androidannotations.api.view;

import java.util.LinkedList;
import java.util.List;

public class OnViewChangedNotifier {
    private static OnViewChangedNotifier currentNotifier;
    private final List<OnViewChangedListener> listeners;

    public OnViewChangedNotifier() {
        this.listeners = new LinkedList();
    }

    public static OnViewChangedNotifier replaceNotifier(OnViewChangedNotifier notifier) {
        OnViewChangedNotifier previousNotifier = currentNotifier;
        currentNotifier = notifier;
        return previousNotifier;
    }

    public static void registerOnViewChangedListener(OnViewChangedListener listener) {
        if (currentNotifier != null) {
            currentNotifier.listeners.add(listener);
        }
    }

    public void notifyViewChanged(HasViews hasViews) {
        for (OnViewChangedListener listener : this.listeners) {
            listener.onViewChanged(hasViews);
        }
    }
}
