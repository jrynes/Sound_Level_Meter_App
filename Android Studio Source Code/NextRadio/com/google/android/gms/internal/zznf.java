package com.google.android.gms.internal;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Binder;
import java.util.List;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class zznf {
    private static String zza(StackTraceElement[] stackTraceElementArr, int i) {
        if (i + 4 >= stackTraceElementArr.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement stackTraceElement = stackTraceElementArr[i + 4];
        return stackTraceElement.getClassName() + ActiveMQDestination.PATH_SEPERATOR + stackTraceElement.getMethodName() + Headers.SEPERATOR + stackTraceElement.getLineNumber();
    }

    public static String zzaz(Context context) {
        return zzi(context, Binder.getCallingPid());
    }

    public static String zzi(Context context, int i) {
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == i) {
                    return runningAppProcessInfo.processName;
                }
            }
        }
        return null;
    }

    public static String zzn(int i, int i2) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        int i3 = i2 + i;
        while (i < i3) {
            stringBuffer.append(zza(stackTrace, i)).append(" ");
            i++;
        }
        return stringBuffer.toString();
    }
}
