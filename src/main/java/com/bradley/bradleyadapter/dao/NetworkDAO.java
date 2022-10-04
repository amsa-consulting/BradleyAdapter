package com.bradley.bradleyadapter.dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NetworkDAO {

    private final String BASE_URL = "http://solus.bradley.edu:8099/bill/";
    private final RestTemplate restTemplate;
    @Value("${access.odata}")
    private String CREDENTIALS;

    public NetworkDAO(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String request(String cust_code) throws Exception  {
        String requestUrl = BASE_URL + "tsbGet?cust_code="+cust_code+"&$format=json";
        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);

        String inputLine = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity,String.class).getBody();

        return inputLine.toString();

    }

}
