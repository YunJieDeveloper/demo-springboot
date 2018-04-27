package com.example.springboot.demo.template;

import com.google.common.collect.Lists;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/***
 * @Date 2017/11/29
 *@Description RestTemplate 使用案例
 * @author zhanghesheng
 * */

@Component
public class RestTemplateDemo {

    /**cookie方式*/
    public void postMethodWithCookie(String url,String...cookieArgs){
        RestTemplate restTemplate =new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
       if(cookieArgs!=null){
           cookies=Arrays.asList(cookieArgs);
       }
        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity request = new HttpEntity(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        response.toString();
    }

    /**参数以表单方式提交*/
    public void postMethodWithForm(String url, Map<String ,String> map){
        RestTemplate restTemplate =new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        multiValueMap.setAll(map);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(multiValueMap, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
    }

    /**参数以json方式提交*/
    public void postMethodWithJson(String url,String requestJson){
        RestTemplate restTemplate =new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> resp = restTemplate.postForEntity(url,entity,String.class);
    }
    /**get请求转为post*/
    public void postMethodFormatUrl(String url,String requestJson){
        RestTemplate restTemplate =new RestTemplate();
        String template = url + "/demo?app={0}&userId={1}";
        String app="",userId="";
        String urlformat = MessageFormat.format(template,app,userId);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlformat, null, String.class);
    }
    //请求图片
    public void getForImage(String url){
        RestTemplate restTemplate =new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
        byte[] imageBytes = response.getBody();
    }

    //上传
    public  void upload( String url ,String fileLocal){
            RestTemplate restTemplate =new RestTemplate();
            FileSystemResource resource = new FileSystemResource(new File(fileLocal));
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("file", resource);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            System.out.println(responseEntity.getBody());
    }

}
