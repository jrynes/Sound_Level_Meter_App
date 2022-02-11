package com.crashlytics.android.answers;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.events.EventsStorageListener;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import java.util.concurrent.ScheduledExecutorService;

class AnswersEventsHandler implements EventsStorageListener {
    private final Context context;
    final ScheduledExecutorService executor;
    private final AnswersFilesManagerProvider filesManagerProvider;
    private final Kit kit;
    private final SessionMetadataCollector metadataCollector;
    private final HttpRequestFactory requestFactory;
    SessionAnalyticsManagerStrategy strategy;

    /* renamed from: com.crashlytics.android.answers.AnswersEventsHandler.1 */
    class C03511 implements Runnable {
        final /* synthetic */ AnalyticsSettingsData val$analyticsSettingsData;
        final /* synthetic */ String val$protocolAndHostOverride;

        C03511(AnalyticsSettingsData analyticsSettingsData, String str) {
            this.val$analyticsSettingsData = analyticsSettingsData;
            this.val$protocolAndHostOverride = str;
        }

        public void run() {
            try {
                AnswersEventsHandler.this.strategy.setAnalyticsSettingsData(this.val$analyticsSettingsData, this.val$protocolAndHostOverride);
            } catch (Exception e) {
                Fabric.getLogger().e(Answers.TAG, "Failed to set analytics settings data", e);
            }
        }
    }

    /* renamed from: com.crashlytics.android.answers.AnswersEventsHandler.2 */
    class C03522 implements Runnable {
        C03522() {
        }

        public void run() {
            try {
                SessionAnalyticsManagerStrategy prevStrategy = AnswersEventsHandler.this.strategy;
                AnswersEventsHandler.this.strategy = new DisabledSessionAnalyticsManagerStrategy();
                prevStrategy.deleteAllEvents();
            } catch (Exception e) {
                Fabric.getLogger().e(Answers.TAG, "Failed to disable events", e);
            }
        }
    }

    /* renamed from: com.crashlytics.android.answers.AnswersEventsHandler.3 */
    class C03533 implements Runnable {
        C03533() {
        }

        public void run() {
            try {
                AnswersEventsHandler.this.strategy.sendEvents();
            } catch (Exception e) {
                Fabric.getLogger().e(Answers.TAG, "Failed to send events files", e);
            }
        }
    }

    /* renamed from: com.crashlytics.android.answers.AnswersEventsHandler.4 */
    class C03544 implements Runnable {
        C03544() {
        }

        public void run() {
            try {
                SessionEventMetadata metadata = AnswersEventsHandler.this.metadataCollector.getMetadata();
                SessionAnalyticsFilesManager filesManager = AnswersEventsHandler.this.filesManagerProvider.getAnalyticsFilesManager();
                filesManager.registerRollOverListener(AnswersEventsHandler.this);
                AnswersEventsHandler.this.strategy = new EnabledSessionAnalyticsManagerStrategy(AnswersEventsHandler.this.kit, AnswersEventsHandler.this.context, AnswersEventsHandler.this.executor, filesManager, AnswersEventsHandler.this.requestFactory, metadata);
            } catch (Exception e) {
                Fabric.getLogger().e(Answers.TAG, "Failed to enable events", e);
            }
        }
    }

    /* renamed from: com.crashlytics.android.answers.AnswersEventsHandler.5 */
    class C03555 implements Runnable {
        C03555() {
        }

        public void run() {
            try {
                AnswersEventsHandler.this.strategy.rollFileOver();
            } catch (Exception e) {
                Fabric.getLogger().e(Answers.TAG, "Failed to flush events", e);
            }
        }
    }

    /* renamed from: com.crashlytics.android.answers.AnswersEventsHandler.6 */
    class C03566 implements Runnable {
        final /* synthetic */ Builder val$eventBuilder;
        final /* synthetic */ boolean val$flush;

        C03566(Builder builder, boolean z) {
            this.val$eventBuilder = builder;
            this.val$flush = z;
        }

        public void run() {
            try {
                AnswersEventsHandler.this.strategy.processEvent(this.val$eventBuilder);
                if (this.val$flush) {
                    AnswersEventsHandler.this.strategy.rollFileOver();
                }
            } catch (Exception e) {
                Fabric.getLogger().e(Answers.TAG, "Failed to process event", e);
            }
        }
    }

    public AnswersEventsHandler(Kit kit, Context context, AnswersFilesManagerProvider filesManagerProvider, SessionMetadataCollector metadataCollector, HttpRequestFactory requestFactory, ScheduledExecutorService executor) {
        this.strategy = new DisabledSessionAnalyticsManagerStrategy();
        this.kit = kit;
        this.context = context;
        this.filesManagerProvider = filesManagerProvider;
        this.metadataCollector = metadataCollector;
        this.requestFactory = requestFactory;
        this.executor = executor;
    }

    public void processEventAsync(Builder eventBuilder) {
        processEvent(eventBuilder, false, false);
    }

    public void processEventAsyncAndFlush(Builder eventBuilder) {
        processEvent(eventBuilder, false, true);
    }

    public void processEventSync(Builder eventBuilder) {
        processEvent(eventBuilder, true, false);
    }

    public void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String protocolAndHostOverride) {
        executeAsync(new C03511(analyticsSettingsData, protocolAndHostOverride));
    }

    public void disable() {
        executeAsync(new C03522());
    }

    public void onRollOver(String rolledOverFile) {
        executeAsync(new C03533());
    }

    public void enable() {
        executeAsync(new C03544());
    }

    public void flushEvents() {
        executeAsync(new C03555());
    }

    void processEvent(Builder eventBuilder, boolean sync, boolean flush) {
        Runnable runnable = new C03566(eventBuilder, flush);
        if (sync) {
            executeSync(runnable);
        } else {
            executeAsync(runnable);
        }
    }

    private void executeSync(Runnable runnable) {
        try {
            this.executor.submit(runnable).get();
        } catch (Exception e) {
            Fabric.getLogger().e(Answers.TAG, "Failed to run events task", e);
        }
    }

    private void executeAsync(Runnable runnable) {
        try {
            this.executor.submit(runnable);
        } catch (Exception e) {
            Fabric.getLogger().e(Answers.TAG, "Failed to submit events task", e);
        }
    }
}
