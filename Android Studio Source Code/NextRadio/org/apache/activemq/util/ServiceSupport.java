package org.apache.activemq.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServiceSupport implements Service {
    private static final Logger LOG;
    private List<ServiceListener> serviceListeners;
    private AtomicBoolean started;
    private AtomicBoolean stopped;
    private AtomicBoolean stopping;

    protected abstract void doStart() throws Exception;

    protected abstract void doStop(ServiceStopper serviceStopper) throws Exception;

    public void start() throws java.lang.Exception {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 0, insn: 0x0022: INVOKE  (r3 boolean) = (r0 java.util.Iterator) java.util.Iterator.hasNext():boolean type: INTERFACE, block:B:6:0x0022, method: org.apache.activemq.util.ServiceSupport.start():void
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:162)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:191)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:191)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:191)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:191)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:131)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r6 = this;
        r5 = 0;
        r3 = r6.started;
        r4 = 1;
        r3 = r3.compareAndSet(r5, r4);
        if (r3 == 0) goto L_0x0039;
    L_0x000a:
        r2 = 0;
        r3 = r6.stopped;
        r3.set(r5);
        preStart();	 Catch:{ all -> 0x0032 }
        doStart();	 Catch:{ all -> 0x0032 }
        r2 = 1;
        r3 = r6.started;
        r3.set(r2);
        r3 = r6.serviceListeners;
        r0 = r3.iterator();
    L_0x0022:
        r3 = r0.hasNext();
        if (r3 == 0) goto L_0x0039;
    L_0x0028:
        r1 = r0.next();
        r1 = (org.apache.activemq.util.ServiceListener) r1;
        r1.started(r6);
        goto L_0x0022;
    L_0x0032:
        r3 = move-exception;
        r4 = r6.started;
        r4.set(r2);
        throw r3;
    L_0x0039:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.util.ServiceSupport.start():void");
    }

    public ServiceSupport() {
        this.started = new AtomicBoolean(false);
        this.stopping = new AtomicBoolean(false);
        this.stopped = new AtomicBoolean(false);
        this.serviceListeners = new CopyOnWriteArrayList();
    }

    static {
        LOG = LoggerFactory.getLogger(ServiceSupport.class);
    }

    public static void dispose(Service service) {
        try {
            service.stop();
        } catch (Exception e) {
            LOG.debug("Could not stop service: " + service + ". Reason: " + e, e);
        }
    }

    public void stop() throws Exception {
        if (this.stopped.compareAndSet(false, true)) {
            this.stopping.set(true);
            ServiceStopper stopper = new ServiceStopper();
            try {
                doStop(stopper);
            } catch (Exception e) {
                stopper.onException(this, e);
            } finally {
                postStop(stopper);
            }
            this.stopped.set(true);
            this.started.set(false);
            this.stopping.set(false);
            for (ServiceListener l : this.serviceListeners) {
                l.stopped(this);
            }
            stopper.throwFirstException();
        }
    }

    public boolean isStarted() {
        return this.started.get();
    }

    public boolean isStopping() {
        return this.stopping.get();
    }

    public boolean isStopped() {
        return this.stopped.get();
    }

    public void addServiceListener(ServiceListener l) {
        this.serviceListeners.add(l);
    }

    public void removeServiceListener(ServiceListener l) {
        this.serviceListeners.remove(l);
    }

    protected void postStop(ServiceStopper stopper) throws Exception {
    }

    protected void preStart() throws Exception {
    }
}
