package com.example.moses.mosesnews.utils;

/**
 * Created by 丹 on 2015.1.8
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HTTPUtil {
    String httpUrl;
    HashMap parameters;
    String reqMethod;
    HttpCallBackInterface mHttpCallBackInterface;

    public final static String METHOD_GET = "GET";
    public final static String METHOD_POST = "POST";

    public HTTPUtil(String httpUrl, HashMap parameters,
                    String reqMethod) {
        this.httpUrl = httpUrl;
        this.parameters = parameters;
        this.reqMethod = reqMethod;
    }

    public void sendRequest(HttpCallBackInterface httpCallBackInterface) {
        this.mHttpCallBackInterface = httpCallBackInterface;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ss = null;
                try {
                    ss = getHttpResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Log.e("TAG", "ss>>>>>>>>>>>>>>>>>>>>>" + ss);
                mHttpCallBackInterface.onFinish(ss);
            }
        }).start();
    }

    /**
     * Http获取响应回调接口
     */
    public interface HttpCallBackInterface {
        void onFinish(String s);
    }

    /**
     * 将HashMap转化成List<NameValuePair>
     *
     * @param parameters : HashMap参数
     * @return : List<NameValuePair>对象
     */
    private List<NameValuePair> getParameters(HashMap parameters) {
        List<NameValuePair> paramList = new ArrayList<>();
        for (Object o : parameters.entrySet()) {
            HashMap.Entry entry = (HashMap.Entry) o;
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            paramList.add(new BasicNameValuePair(key, value));
        }
        return paramList;
    }

    /**
     * 网络或取数据的具体内容
     *
     * @return ： 响应数据
     * @throws java.io.IOException
     */
    private String getHttpResponse() throws IOException {
        List<NameValuePair> paramList = getParameters(parameters);
        //post参数
        HttpClient httpClient = new DefaultHttpClient();
        StringBuilder stringBuilder = null;
        HttpResponse httpResponse;
        /**
         * GET 方式
         */
        if (reqMethod.equals(HTTPUtil.METHOD_GET)) {

            String news_type_id = parameters.get("news_type_id").toString();
            int pageNo = Integer.parseInt(parameters.get("pageNo").toString());
            int pageSize = Integer.parseInt(parameters.get("pageSize").toString());

            String obStr = news_type_id + "/" + pageNo * pageSize + "-" + pageSize + ".html";

//            Log.e("TAG", "httpURL>>>>>>>>>>>>>>>>>>>>>" + httpUrl + obStr);

            HttpGet httpGet = new HttpGet(httpUrl + obStr);
            httpClient.execute(httpGet);
            httpResponse = httpClient.execute(httpGet);
            stringBuilder = getHttpStringBuilder(httpResponse);    //-------StringBuilder重构数据---------------

            /**
             * POST方式
             */
        } else if (reqMethod.equals(HTTPUtil.METHOD_POST)) {
            HttpPost httpPost = new HttpPost(httpUrl);
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(paramList, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            stringBuilder = getHttpStringBuilder(httpResponse);    //-------StringBuilder重构数据---------------
        }

        /**
         * 接收响应数据
         */
        assert stringBuilder != null;
        return new String(stringBuilder.toString().getBytes(), "utf-8");
    }

    /**
     * 获取服务端响应数据，使用StringBuilder重构数据
     *
     * @param httpResponse : 对应POST 或 GET 请求的响应对象。
     * @return : 返回StringBuilder到调用getHttpResponse()中， 输出响应结果。
     */
    private StringBuilder getHttpStringBuilder(HttpResponse httpResponse) {
        StringBuilder stringBuilder = null;
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            try {
                InputStream is = httpEntity.getContent();
                stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder;
    }

}
