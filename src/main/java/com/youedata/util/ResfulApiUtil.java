package com.youedata.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;

public class ResfulApiUtil {

    private static Logger logger = LoggerFactory.getLogger(ResfulApiUtil.class);

    /**
     * 接口调用（GET专用）
     *
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static JSONObject sendRequestOfGet(String url, JSONObject param) throws Exception {
        RestTemplate restTemplate = getRestTemplate();

        //Get请求 参数拼接（?key1=value1&key2=value2）
        URI uri = getURI(url, param);

        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,null, JSONObject.class);
        if (responseEntity == null || responseEntity.getBody() == null) {
            logger.info("接口调用失败");
        }

        return responseEntity.getBody();
    }

    /**
     * 接口调用（POST专用）
     *
     * @param url
     * @param headersParam
     * @param param
     * @return
     * @throws Exception
     */
    public static JSONObject sendRequestOfPostByQueryParam(String url, JSONObject headersParam, JSONObject param) throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        //Get请求 参数拼接（?key1=value1&key2=value2）
        URI uri = getURI(url, param);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, null, JSONObject.class);
        if (responseEntity == null || responseEntity.getBody() == null) {
            logger.info("接口调用失败");
        }
        return responseEntity.getBody();
    }
    /**
     * 接口调用（POST专用）
     *
     * @param url
     * @param headersParam
     * @param bodyParam
     * @return
     * @throws Exception
     */
    public static JSONObject sendRequestOfPostByBodyParam(String url, JSONObject headersParam, JSONObject bodyParam) throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Get请求 参数拼接（?key1=value1&key2=value2）
        HttpEntity httpEntity = new HttpEntity(bodyParam,headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST,httpEntity, JSONObject.class);
        if (responseEntity == null || responseEntity.getBody() == null) {
            logger.info("接口调用失败");
        }
        return responseEntity.getBody();
    }

    public static void sendRequestOfPostByBodyParamAsync(String url, JSONObject headersParam, JSONObject bodyParam) throws Exception {
        AsyncRestTemplate asyncRestTemplate = asyncRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Get请求 参数拼接（?key1=value1&key2=value2）
        HttpEntity httpEntity = new HttpEntity(bodyParam,headers);
        // 将参数拼入请求url中
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        //异步发送
        ListenableFuture<ResponseEntity<JSONObject>> forEntity = asyncRestTemplate.exchange(url, HttpMethod.POST,httpEntity, JSONObject.class);
        //异步调用后的回调函数
        forEntity.addCallback(new ListenableFutureCallback<ResponseEntity<JSONObject>>() {
            @Override
            public void onSuccess(ResponseEntity<JSONObject> result) {
                logger.info("--->async rest response success----, result = "+result.getBody());
            }
            //调用失败
            @Override
            public void onFailure(Throwable ex) {
                logger.error("=====rest response faliure======");
            }
        });
    }
        public static JSONObject post(String url) {
//        String url=userApiConfig.getStandardProviderByInCode()+"?code="+code+"&parentStandardProviderStr="+parentStandardProviderStr;
        //url="http://10.0.43.66:28011/yyzhzx/api/v1/organization/selectStandardProviderByInCode?code="+code+"&parentStandardProviderStr="+parentStandardProviderStr;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, null, JSONObject.class);
        return responseEntity.getBody();
    }

    /**
     * PATH请求
     * @param url
     */
    public static void patch(String url) {
//        String url=userApiConfig.getStandardProviderByInCode()+"?code="+code+"&parentStandardProviderStr="+parentStandardProviderStr;
        //url="http://10.0.43.66:28011/yyzhzx/api/v1/organization/selectStandardProviderByInCode?code="+code+"&parentStandardProviderStr="+parentStandardProviderStr;
        RestTemplate restTemplate = getRestTemplate();
        restTemplate.exchange(url, HttpMethod.PATCH, null,Void.class);
    }

    public static JSONObject sendRequestOfUpload(String url, String path) throws Exception{
        RestTemplate restTemplate = getRestTemplate();
        FileSystemResource resource = new FileSystemResource(new File(path));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("jarfile", resource);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
        if (responseEntity == null || responseEntity.getBody() == null) {
            logger.info("接口调用失败");
        }
        return responseEntity.getBody();
    }

    private static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private static URI getURI(String url, JSONObject param) {
        URI uri = null;
        url = getUrlParam(url, param);
        // 这里进行网络转码，因为我们传递的URL是字符串，最后都要统一转为URL，而且对于字符都应该进行网络编码，防止#等特殊字符在自动转换过程中失败
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        uri = builder.build().encode().toUri();
        return uri;
    }

    private static String getUrlParam(String url, JSONObject param) {
        if (param != null && param.size() > 0) {
            StringBuilder pathUri = new StringBuilder("?");
            for (String key : param.keySet()) {
                Object value = param.get(key);
                if (value != null) {
                    pathUri.append(key)
                            .append("=")
                            .append(value)
                            .append("&");
                }
            }
            pathUri = pathUri.deleteCharAt(pathUri.length() - 1);
            url = url + pathUri.toString();
        }
        return url;
    }

    private static AsyncRestTemplate asyncRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //设置链接超时时间
        factory.setConnectTimeout(100);
        //设置读取资料超时时间
        factory.setReadTimeout(200);
        //设置异步任务（线程不会重用，每次调用时都会重新启动一个新的线程）
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return new AsyncRestTemplate(factory);
    }
}
