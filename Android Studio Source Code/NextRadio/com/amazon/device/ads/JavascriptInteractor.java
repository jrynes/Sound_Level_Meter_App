package com.amazon.device.ads;

import android.webkit.JavascriptInterface;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

class JavascriptInteractor {
    private static final String LOGTAG;
    private static String executorMethodName;
    private static final MobileAdsLogger logger;
    private final Executor executor;
    private final Map<String, JavascriptMethodExecutor> methodMap;

    public static abstract class JavascriptMethodExecutor {
        private final String methodName;

        protected abstract JSONObject execute(JSONObject jSONObject);

        protected JavascriptMethodExecutor(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return this.methodName;
        }
    }

    public static class Executor {
        private final JavascriptInteractor interactor;
        private boolean proguardKeeper;

        public Executor(JavascriptInteractor interactor) {
            this.proguardKeeper = false;
            this.interactor = interactor;
            if (this.proguardKeeper) {
                execute(null, null);
            }
        }

        @JavascriptInterface
        public String execute(String method, String javascriptObjectParameters) {
            JSONObject returnJSON = this.interactor.execute(method, javascriptObjectParameters);
            return returnJSON == null ? null : returnJSON.toString();
        }
    }

    static {
        LOGTAG = JavascriptInteractor.class.getSimpleName();
        logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    public JavascriptInteractor() {
        this.methodMap = new ConcurrentHashMap();
        this.executor = new Executor(this);
    }

    public static String getExecutorMethodName() {
        if (executorMethodName == null) {
            Method[] methods = Executor.class.getDeclaredMethods();
            if (methods == null || methods.length != 1) {
                logger.m639e("Could not obtain the method name for javascript interfacing.");
            } else {
                executorMethodName = methods[0].getName();
            }
        }
        return executorMethodName;
    }

    public void addMethodExecutor(JavascriptMethodExecutor javascriptMethodExecutor) {
        if (this.methodMap.containsKey(javascriptMethodExecutor.getMethodName())) {
            throw new IllegalArgumentException("There is another executor with that method name already added.");
        }
        this.methodMap.put(javascriptMethodExecutor.getMethodName(), javascriptMethodExecutor);
    }

    public Executor getExecutor() {
        return this.executor;
    }

    private JSONObject execute(String method, String javascriptObjectParameters) {
        JSONObject jsonObject = null;
        if (javascriptObjectParameters != null && javascriptObjectParameters.length() > 2) {
            jsonObject = JSONUtils.getJSONObjectFromString(javascriptObjectParameters);
            if (jsonObject == null) {
                logger.m646w("The javascript object \"%s\" could not be parsed for method \"%s\".", javascriptObjectParameters, method);
                return null;
            }
        }
        return execute(method, jsonObject);
    }

    private JSONObject execute(String method, JSONObject jsonObject) {
        if (this.methodMap.containsKey(method)) {
            return ((JavascriptMethodExecutor) this.methodMap.get(method)).execute(jsonObject);
        }
        logger.m646w("The method %s was not recongized by this javascript interface.", method);
        return null;
    }
}
