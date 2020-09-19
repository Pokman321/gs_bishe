package com.de.service.impl;

import com.de.entity.HttpResult;
import com.de.service.DoMOTService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author gs
 * @date 2020/6/16 - 14:51
 */
@Service
public class DoMOTServiceImpl implements DoMOTService {


    @Autowired
    private CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    @Autowired
    private RequestConfig requestConfig;

    public void setRequestConfig(){
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        this.requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
//                .setProxy(new HttpHost("myotherproxy", 8080))
                .build();
    }

    /**
     * 执行get请求,200返回响应内容，其他状态码返回null
     *
     * @param url
     * @return
     * @throws IOException
     */
//    @Autowired
    public String doGet(String url) throws IOException {
        //创建httpClient对象
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        //设置请求参数



        httpGet.setConfig(requestConfig);
        try {
            //执行请求
            response = httpClient.execute(httpGet);
            //判断返回状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 执行带有参数的get请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
//    @Autowired
    public String doGet(String url, Map<String, String> paramMap) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        for (String s : paramMap.keySet()) {
            builder.addParameter(s, paramMap.get(s));
        }
        return doGet(builder.build().toString());
    }

    /**
     * 执行post请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
//    @Autowired
    public HttpResult doPost(String url, Map<String, String> paramMap) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        //设置请求参数
        httpPost.setConfig(requestConfig);
        if (paramMap != null) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (String s : paramMap.keySet()) {
                parameters.add(new BasicNameValuePair(s, paramMap.get(s)));
            }
            //构建一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8"));
            //将请求实体放入到httpPost中
            httpPost.setEntity(formEntity);
        }
        //创建httpClient对象
        CloseableHttpResponse response = null;
        try {
            //执行请求
            response = httpClient.execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 执行post请求
     *
     * @param url
     * @return
     * @throws IOException
     */
//    @Autowired
    public HttpResult doPost(String url) throws IOException {
        return doPost(url, null);
    }


    /**
     * 提交json数据
     *
     * @param url
     * @param json
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
//    @Autowired
    public HttpResult doPostJson(String url, String json) throws ClientProtocolException, IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);

        if (json != null) {
            // 构造一个请求实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.httpClient.execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public JSONObject post(String url, JSONObject json){
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);

            HttpResponse res = client.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.OK.value()){
                HttpEntity entity = res.getEntity();
//                String charset = EntityUtils.getContentCharSet(entity);
                String charset = "UTF-8";
                response = new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(),charset)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public HttpResult jsonPost(String url,JSONObject json){
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        try{
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            return new HttpResult(res.getStatusLine().getStatusCode(),
                    EntityUtils.toString(res.getEntity(), "UTF-8"));
        } catch (Exception e) {
            return new HttpResult(600,"None");

        }

    }



}
