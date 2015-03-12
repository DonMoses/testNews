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

public class MosesHTTPUtil {
    String httpUrl;
    HashMap parameters;
    String reqMethod;
    String reqType;
    HttpCallBackInterface mHttpCallBackInterface;

    public final static String METHOD_GET = "GET";
    public final static String METHOD_POST = "POST";
    public final static String NORMAL_SIMPLE_TYPE = "SIMPLE_HTTP_GET";

    public MosesHTTPUtil(String httpUrl, HashMap parameters,
                         String reqMethod, String reqType) {
        this.httpUrl = httpUrl;
        this.parameters = parameters;
        this.reqMethod = reqMethod;
        this.reqType = reqType;
    }

    public MosesHTTPUtil(String httpUrl, String reqMethod, String reqType) {
        this.httpUrl = httpUrl;
        this.reqType = reqType;
        this.reqMethod = reqMethod;
    }

    public void sendRequest(HttpCallBackInterface httpCallBackInterface) {
        this.mHttpCallBackInterface = httpCallBackInterface;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ss = null;
                switch (reqType) {
                    case "NEWS_IMG_GET":
                        try {
                            ss = getImgGetHttpResponse();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "NEWS_VIDEO_GET":
                        try {
                            ss = getSimpleHttpResponse();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case NORMAL_SIMPLE_TYPE:
                        try {
                            ss = getSimpleHttpResponse();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        try {
                            ss = getHttpResponse();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
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
    private String getSimpleHttpResponse() throws IOException {
        //post参数
        HttpClient httpClient = new DefaultHttpClient();
        StringBuilder stringBuilder = null;
        HttpResponse httpResponse;
        /**
         * GET 方式
         */
        if (reqMethod.equals(MosesHTTPUtil.METHOD_GET)) {
//            Log.e("TAG", ">>>httpUrl>>moses>" + httpUrl);
            HttpGet httpGet = new HttpGet(httpUrl);
            httpResponse = httpClient.execute(httpGet);
            stringBuilder = getHttpStringBuilder(httpResponse);    //-------StringBuilder重构数据---------------

        }

        /**
         * 接收响应数据
         */
        assert stringBuilder != null;
        return new String(stringBuilder.toString().getBytes(), "utf-8");
    }

    /**
     * 网络或取数据的具体内容
     *
     * @return ： 响应数据
     * @throws java.io.IOException
     */
    private String getImgGetHttpResponse() throws IOException {
        //post参数
        HttpClient httpClient = new DefaultHttpClient();
        StringBuilder stringBuilder = null;
        HttpResponse httpResponse;
        /**
         * GET 方式
         */
        if (reqMethod.equals(MosesHTTPUtil.METHOD_GET)) {
            String channel = parameters.get("channel").toString();
            String adid = parameters.get("adid").toString();
            String wm = parameters.get("wm").toString();
            String from = parameters.get("from").toString();
            String chwm = parameters.get("chwm").toString();
            String oldchwm = parameters.get("oldchwm").toString();
            String imei = parameters.get("imei").toString();
            String uid = parameters.get("uid").toString();
            int p = Integer.parseInt(parameters.get("pageNo").toString());

//              http://api.sina.cn/sinago/list.json?channel=hdpic_toutiao&adid=4ad30dabe13469
//              5c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=&imei=8670
//              64013906290&uid=802909da86d9f5fc&p=

            String obStr = "?" + "channel" + "=" + channel + "&" +
                    "adid" + "=" + adid + "&" +
                    "wm" + "=" + wm + "&" +
                    "from" + "=" + from + "&" +
                    "chwm" + "=" + chwm + "&" +
                    "oldchwm" + "=" + oldchwm + "&" +
                    "imei" + "=" + imei + "&" +
                    "uid" + "=" + uid + "&" +
                    "p" + "=" + p;

//            Log.e("TAG", "httpURL>>>>>>>>>>>>>>>>>>>>>" + httpUrl + obStr);

            HttpGet httpGet = new HttpGet(httpUrl + obStr);
            httpResponse = httpClient.execute(httpGet);
            stringBuilder = getHttpStringBuilder(httpResponse);    //-------StringBuilder重构数据---------------

        }

        /**
         * 接收响应数据
         */
        assert stringBuilder != null;
        return new String(stringBuilder.toString().getBytes(), "utf-8");
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
        if (reqMethod.equals(MosesHTTPUtil.METHOD_GET)) {

            String news_type_id = parameters.get("news_type_id").toString();
            int pageNo = Integer.parseInt(parameters.get("pageNo").toString());
            int pageSize = Integer.parseInt(parameters.get("pageSize").toString());

            String obStr = news_type_id + "/" + pageNo * pageSize + "-" + pageSize + ".html";

//            Log.e("TAG", "httpURL>>>>>>>>>>>>>>>>>>>>>" + httpUrl + obStr);

            HttpGet httpGet = new HttpGet(httpUrl + obStr);
            httpResponse = httpClient.execute(httpGet);
            stringBuilder = getHttpStringBuilder(httpResponse);    //-------StringBuilder重构数据---------------

            /**
             * POST方式
             */
        } else if (reqMethod.equals(MosesHTTPUtil.METHOD_POST)) {
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
