package com.de.service;

import com.de.entity.HttpResult;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author gs
 * @date 2020/6/19 - 23:42
 */
public interface DoMOTService {

    public void setRequestConfig();

    public String doGet(String url) throws IOException;

    public String doGet(String url, Map<String, String> paramMap) throws IOException, URISyntaxException;

    public HttpResult doPost(String url, Map<String, String> paramMap) throws IOException;

    public HttpResult doPost(String url) throws IOException;

    public HttpResult doPostJson(String url, String json) throws ClientProtocolException, IOException;

    public JSONObject post(String url, JSONObject json);

}
