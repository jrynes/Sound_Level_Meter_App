package com.amazon.device.associates;

import android.os.AsyncTask;

/* compiled from: AsyncInternalTask */
class av extends AsyncTask<ag, Void, Void> {
    private static final String f1203a;

    av() {
    }

    protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        return m799a((ag[]) objArr);
    }

    static {
        f1203a = av.class.getName();
    }

    protected Void m799a(ag... agVarArr) {
        try {
            agVarArr[0].m705e();
        } catch (Exception e) {
            Log.m1018c(f1203a, "AsyncInternalTask failed. Ex=" + e);
        }
        return null;
    }
}
