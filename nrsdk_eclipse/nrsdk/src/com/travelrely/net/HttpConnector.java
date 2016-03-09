package com.travelrely.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.travelrely.app.activity.BaseActivity;
import com.travelrely.sdk.R;
import com.travelrely.v2.util.LOGManager;

public class HttpConnector
{
    /**
     * 消息地址
     */

    public static String roam_glms_loc = "";

    public static Object lock = new Object();

    public static final int HTTP_TIMEOUT = 30 * 1000; // ms

    public static final int MESSAGE_HTTP_TIMEOUT = 24 * 60 * 60 * 1000; // ms

    public static DefaultHttpClient mHttpClient;

    private static DefaultHttpClient messageHttpClient;

    private static DefaultHttpClient otherMessageHttpClient;

    public static void initConnectors()
    {

        if (mHttpClient != null)
        {
            mHttpClient.clearRequestInterceptors();
            mHttpClient.clearResponseInterceptors();
        }
        mHttpClient = new DefaultHttpClient();
        if (messageHttpClient != null)
        {
            messageHttpClient.clearRequestInterceptors();
            messageHttpClient.clearResponseInterceptors();
        }
        messageHttpClient = new DefaultHttpClient();
        if (otherMessageHttpClient != null)
        {
            otherMessageHttpClient.clearRequestInterceptors();
            otherMessageHttpClient.clearResponseInterceptors();
        }
        otherMessageHttpClient = new DefaultHttpClient();
        final HttpParams params = mHttpClient.getParams();

        HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
        ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);

        final HttpParams params1 = messageHttpClient.getParams();

        HttpConnectionParams
                .setConnectionTimeout(params1, MESSAGE_HTTP_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params1, MESSAGE_HTTP_TIMEOUT);
        ConnManagerParams.setTimeout(params1, MESSAGE_HTTP_TIMEOUT);

        final HttpParams params2 = otherMessageHttpClient.getParams();

        HttpConnectionParams
                .setConnectionTimeout(params2, MESSAGE_HTTP_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params2, MESSAGE_HTTP_TIMEOUT);
        ConnManagerParams.setTimeout(params2, MESSAGE_HTTP_TIMEOUT);

    }

    public String requestByHttpPost(String url, byte[] bytes, Context context)
    {

        LOGManager.d("url:" + url);
        synchronized (lock)
        {

            HttpPost httpRequest = new HttpPost(url);

            try
            {
                ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
                httpRequest.setEntity(byteArrayEntity);
                httpRequest.setHeader("Accept-Encoding", "gzip, deflate");
                httpRequest
                        .setHeader("Content-Type",
                                "text/html; boundary=----WebKitFormBoundaryTowhxUoSqFqPQ2El");
                httpRequest.setHeader("Accept", "*/*");
                HttpResponse rsp = mHttpClient.execute(httpRequest);
                HttpEntity httpEntity;
                int code = rsp.getStatusLine().getStatusCode();
                LOGManager.d("resultCode" + code);
                if (code == HttpURLConnection.HTTP_OK)
                {

                    StringBuffer sb = new StringBuffer();
                    httpEntity = rsp.getEntity();
                    InputStream is = httpEntity.getContent();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is, "utf-8"));
                    String data = "";
                    while ((data = br.readLine()) != null)
                    {
                        sb.append(data);
                    }
                    String result = sb.toString();
                    LOGManager.d("上传成功   result=" + result);
                    return result;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                httpRequest.abort();
            }
        }
        return null;
    }

    public static void print(byte[] bytess, String chartSet)
    {
        String str = "";
        try
        {
            str = new String(bytess, chartSet);
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LOGManager.d("response" + chartSet + str);
    }

    public static String PHPSESSID;

    public String requestByHttpPut(String url, String postdata,
            Context context, boolean needSession)
    {
        HttpPut httpRequest = new HttpPut(url);
        HttpClient httpClient = new DefaultHttpClient();

        synchronized (lock)
        {
            LOGManager.d(url + postdata);
            try
            {
                StringEntity stringEntity = new StringEntity(postdata,
                        HTTP.UTF_8);
                httpRequest.setEntity(stringEntity);
                HttpResponse rsp = httpClient.execute(httpRequest);

                int code = rsp.getStatusLine().getStatusCode();
                LOGManager.d("resultCode" + code);
                if (code == HttpURLConnection.HTTP_OK)
                {
                    StringBuffer sb = new StringBuffer();
                    HttpEntity httpEntity = rsp.getEntity();
                    InputStream is = httpEntity.getContent();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is, "utf-8"));
                    String data = "";

                    while ((data = br.readLine()) != null)
                    {
                        sb.append(data);
                    }
                    String result = sb.toString();
                    LOGManager.d("result" + result);
                    return result;
                }
                else
                {
                    if (context != null && context instanceof Activity)
                    {
                        // BaseActivity baseActivity = (BaseActivity) context;
                    }
                }
            }
            catch (Exception e)
            {
                if (e instanceof SocketTimeoutException)
                {
                    if (context != null && context instanceof Activity)
                    {
                        Activity activity = (Activity) context;
                        showShortToast(activity, activity.getResources()
                                .getString(R.string.errorNetwork3));
                    }
                }
                e.printStackTrace();
                LOGManager.e("httpConnector error" + e.getMessage());
            }
            finally
            {
                httpRequest.abort();
            }
        }
        return null;
    }

    public static Object messageLock = new Object();

    public static Object ortherMessageLock = new Object();

    /**
     * 请求message
     * 
     * @param url
     * @param postdata
     * @param context
     * @param needSession
     * @return
     */
    public String requestByHttpPutMessage(String url, String postdata,
            Context context, boolean needSession)
    {
        // LOGManager.d("requestByHttpPutMessage");
        HttpPut httpRequest = new HttpPut(url);

        synchronized (messageLock)
        {
            return requestMessage(messageHttpClient, httpRequest, url,
                    postdata, context, needSession);
        }
    }

    public String requestByHttpPutMessageOrtherThread(String url,
            String postdata, Context context, boolean needSession)
    {
        LOGManager.d("异步getMessage");
        HttpPut httpRequest = new HttpPut(url);
        synchronized (ortherMessageLock)
        {
            return requestMessage(otherMessageHttpClient, httpRequest, url,
                    postdata, context, needSession);
        }
    }

    public String requestMessage(DefaultHttpClient defaultHttpClient,
            HttpPut httpRequest, String url, String postdata, Context context,
            boolean needSession)
    {
        try
        {
            LOGManager.d(url + postdata);
            StringEntity stringEntity = new StringEntity(postdata, HTTP.UTF_8);
            httpRequest.setEntity(stringEntity);

            httpRequest.setHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            if (null != PHPSESSID)
            {
                if (needSession)
                {
                    httpRequest.setHeader("webpy_session_id", PHPSESSID);
                }
            }
            HttpResponse rsp = defaultHttpClient.execute(httpRequest);

            int code = rsp.getStatusLine().getStatusCode();
            LOGManager.d("resultCode" + code);
            if (code == HttpURLConnection.HTTP_OK)
            {
                StringBuffer sb = new StringBuffer();
                HttpEntity httpEntity = rsp.getEntity();
                InputStream is = httpEntity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        is, "utf-8"));
                // String s = EntityUtils.toString(httpEntity);
                String data = "";

                while ((data = br.readLine()) != null)
                {
                    sb.append(data);
                }
                String result = sb.toString();
                if (result.contains("会话不存在或者已过期"))
                {

                    // TravelrelyApplication.app.isOutTime = true;
                }
                LOGManager.d("result" + result);
                return result;
            }
            else
            {
                if (context != null && context instanceof Activity)
                {
                    Activity activity = (Activity) context;
                    showShortToast(activity, "errorcode:" + code);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGManager.e("httpConnector error" + e.getMessage());
        }
        finally
        {
            httpRequest.abort();
        }
        return null;
    }

    public String requestByHttpGet(String url)
    {
        String strResult = null;
        HttpGet httpRequest = new HttpGet(url);
        try
        {
            // 取得HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();
            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity httpEntity = httpResponse.getEntity();
                // 取得返回的字符串
                strResult = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            }
            else
            {
                return null;
            }
        }
        catch (ClientProtocolException e)
        {
            e.getLocalizedMessage();
        }
        catch (IOException e)
        {
            e.getLocalizedMessage();
        }
        catch (Exception e)
        {
            e.getLocalizedMessage();
        }
        return strResult;
    }
    
    /**
     * 用于微信支付
     * @param url
     * @return
     */
    public byte[] HttpGet(String url)
    {
        HttpGet httpRequest = new HttpGet(url);
        try
        {
            // 取得HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();
            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toByteArray(httpResponse.getEntity());
            }
            else
            {
                return null;
            }
        }
        catch (ClientProtocolException e)
        {
            e.getLocalizedMessage();
        }
        catch (IOException e)
        {
            e.getLocalizedMessage();
        }
        catch (Exception e)
        {
            e.getLocalizedMessage();
        }
        return null;
    }
    
    public static byte[] httpPost(String url, String entity) {
        if (url == null || url.length() == 0) {
            LOGManager.e("httpPost, url is null");
            return null;
        }
        
        HttpClient httpClient = getNewHttpClient();
        
        HttpPost httpPost = new HttpPost(url);
        
        try {
            httpPost.setEntity(new StringEntity(entity));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            
            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOGManager.e("httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());
        } catch (Exception e) {
            LOGManager.e("httpPost exception, e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private static HttpClient getNewHttpClient() { 
        try { 
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); 
            trustStore.load(null, null); 

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore); 
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 

            HttpParams params = new BasicHttpParams(); 
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); 
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8); 

            SchemeRegistry registry = new SchemeRegistry(); 
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80)); 
            registry.register(new Scheme("https", sf, 443)); 

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry); 

            return new DefaultHttpClient(ccm, params); 
        } catch (Exception e) { 
            return new DefaultHttpClient(); 
        } 
     }
    
    private static class SSLSocketFactoryEx extends SSLSocketFactory {      
        
        SSLContext sslContext = SSLContext.getInstance("TLS");      
          
        public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {      
            super(truststore);      
          
            TrustManager tm = new X509TrustManager() {      
          
                public X509Certificate[] getAcceptedIssuers() {      
                    return null;      
                }      
          
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }  
            };      
          
            sslContext.init(null, new TrustManager[] { tm }, null);      
        }      
          
        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        } 
    }  

    public void showShortToast(final Activity activity, final String pMsg)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(activity, pMsg, Toast.LENGTH_SHORT)
                .show();
            }
        });
    }
}
