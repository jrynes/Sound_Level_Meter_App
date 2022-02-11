package com.splGUI;


import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.media.AudioRecord;
import android.os.Handler;
import android.preference.PreferenceManager;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Date;

public class SplEngine extends Thread {
    private static final int CALIB_DEFAULT = -80;
    private static final int CALIB_INCREMENT = 3;
    private static final int CHANNEL = 2;
    private static final int ENCODING = 2;
    private static final int ERROR_MSG = -1;
    private static final int FREQUENCY = 44100;
    private static String LOGPATH = null;
    private static final int MAXOVER_MSG = 2;
    private static final int MY_MSG = 1;
    private static final double P0 = 2.0E-6d;
    private volatile int BUFFSIZE;
    private int LOGLIMIT;
    String PREFS_NAME;
    private int logCount;
    private int mCaliberationValue;
    Context mContext;
    private Handler mHandle;
    private volatile boolean mIsLogging;
    private volatile boolean mIsRunning;
    private double mMaxValue;
    AudioRecord mRecordInstance;
    private volatile boolean mShowMaxValue;
    private FileWriter mSplLog;
    private volatile String mode;

    static {
        LOGPATH = "/sdcard/splmeter_";
    }

    public SplEngine(Handler handle, Context context) {
        this.BUFFSIZE = 0;
        this.mCaliberationValue = CALIB_DEFAULT;
        this.mIsRunning = false;
        this.mHandle = null;
        this.mMaxValue = 0.0d;
        this.mShowMaxValue = false;
        this.mSplLog = null;
        this.mIsLogging = false;
        this.LOGLIMIT = 50;
        this.logCount = 0;
        this.mode = "FAST";
        this.mRecordInstance = null;
        this.mContext = null;
        this.PREFS_NAME = "SPLMETER";
        this.mHandle = handle;
        this.mContext = context;
        this.mCaliberationValue = readCalibValue();
        this.mode = "FAST";
        this.mIsLogging = false;
        this.mIsRunning = false;
        this.mMaxValue = 0.0d;
        this.mShowMaxValue = false;
        this.BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, MAXOVER_MSG, MAXOVER_MSG);
        this.mRecordInstance = new AudioRecord(MY_MSG, FREQUENCY, MAXOVER_MSG, MAXOVER_MSG, this.BUFFSIZE * MAXOVER_MSG);
    }

    public void reset() {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editor.putInt("CalibSlow", CALIB_DEFAULT);
        editor.putInt("CalibFast", CALIB_DEFAULT);
        editor.commit();
        this.mCaliberationValue = CALIB_DEFAULT;
    }

    public void start_engine() {
        this.mIsRunning = true;
        start();
    }

    public void stop_engine() {
        this.mIsRunning = false;
    }

    public void setMode(String mode) {
        this.mode = mode;
        setCalibValue(readCalibValue());
        if ("SLOW".equals(mode)) {
            this.BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, MAXOVER_MSG, MAXOVER_MSG) * MAXOVER_MSG;
            this.LOGLIMIT = 10;
            return;
        }
        this.BUFFSIZE = AudioRecord.getMinBufferSize(FREQUENCY, MAXOVER_MSG, MAXOVER_MSG);
        this.LOGLIMIT = 50;
    }

    public int getCalibValue() {
        return this.mCaliberationValue;
    }

    public void setCalibValue(int value) {
        this.mCaliberationValue = value;
    }

    public int readCalibValue() {
        return this.mContext.getSharedPreferences(this.PREFS_NAME, MY_MSG).getInt(this.mode, CALIB_DEFAULT);
    }

    public void storeCalibvalue() {
        Editor editor = this.mContext.getSharedPreferences(this.PREFS_NAME, MAXOVER_MSG).edit();
        editor.putInt(this.mode, this.mCaliberationValue);
        editor.commit();
    }

    public void calibUp() {
        this.mCaliberationValue += CALIB_INCREMENT;
        if (this.mCaliberationValue == 0) {
            this.mCaliberationValue += MY_MSG;
        }
    }

    public void calibDown() {
        this.mCaliberationValue -= CALIB_INCREMENT;
        if (this.mCaliberationValue == 0) {
            this.mCaliberationValue -= MY_MSG;
        }
    }

    public double showMaxValue() {
        this.mShowMaxValue = true;
        return this.mMaxValue;
    }

    public double getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(double max) {
        this.mMaxValue = max;
    }

    public void startLogging() {
        this.mIsLogging = true;
    }

    public void stopLogging() {
        this.mIsLogging = false;
    }

    private void writeLog(double value) {
        String str = "_";
        if (this.mIsLogging) {
            int i = this.logCount;
            this.logCount = i + MY_MSG;
            if (i > this.LOGLIMIT) {
                try {
                    Date now = new Date();
                    this.mSplLog = new FileWriter(LOGPATH + now.getDate() + "_" + now.getMonth() + "_" + (now.getYear() + 1900) + ".xls", true);
                    this.mSplLog.append(new StringBuilder(String.valueOf(value)).append("\n").toString());
                    this.mSplLog.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.logCount = 0;
            }
        }
    }

    public void run() {
        this.mRecordInstance.startRecording();
        double rmsValue = 0.0d;
        while (this.mIsRunning) {
            try {
                int SIZE = this.BUFFSIZE;
                short[] tempBuffer = new short[SIZE];
                this.mRecordInstance.read(tempBuffer, 0, SIZE);
                for (int i = 0; i < SIZE - MY_MSG; i += MY_MSG) {
                    rmsValue += (double) (tempBuffer[i] * tempBuffer[i]);
                }
                rmsValue = Math.sqrt(rmsValue / ((double) SIZE));
                double splValue = round((20.0d * Math.log10(rmsValue / P0)) + ((double) this.mCaliberationValue), MAXOVER_MSG);
                if (this.mMaxValue < splValue) {
                    this.mMaxValue = splValue;
                }
                if (this.mShowMaxValue) {
                    this.mHandle.sendMessage(this.mHandle.obtainMessage(MY_MSG, Double.valueOf(this.mMaxValue)));
                    Thread.sleep(2000);
                    this.mHandle.sendMessage(this.mHandle.obtainMessage(MAXOVER_MSG, Double.valueOf(this.mMaxValue)));
                    this.mShowMaxValue = false;
                } else {
                    this.mHandle.sendMessage(this.mHandle.obtainMessage(MY_MSG, Double.valueOf(splValue)));
                }
                writeLog(splValue);
            } catch (Exception e) {
                Exception e2 = e;
                e2.printStackTrace();
                this.mHandle.sendMessage(this.mHandle.obtainMessage(ERROR_MSG, new StringBuilder(String.valueOf(e2.getLocalizedMessage())).toString()));
            }
        }
        if (this.mRecordInstance != null) {
            this.mRecordInstance.stop();
            this.mRecordInstance.release();
            this.mRecordInstance = null;
        }
    }

    public double round(double d, int decimalPlace) {
        return new BigDecimal(Double.toString(d)).setScale(decimalPlace, 4).doubleValue();
    }
}
