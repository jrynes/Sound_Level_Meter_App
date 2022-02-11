package com.nextradioapp.androidSDK.data;

import android.graphics.Bitmap;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public interface IRestClient {
    void AddHeader(String str, String str2);

    void AddParam(String str, Object obj);

    void AddParam(String str, String str2);

    void ClearParams();

    Bitmap ExecuteForBitmap() throws MalformedURLException, IOException;

    InputStream ExecuteForInputStream(RequestMethod requestMethod) throws Exception;

    String ExecuteForString(RequestMethod requestMethod) throws Exception;

    void ExecutePost() throws Exception;

    String getErrorMessage();

    String getResponse();

    int getResponseCode();

    void setURL(String str);
}
