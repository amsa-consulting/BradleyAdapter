package com.bradley.bradleyadapter.controller;

import com.bradley.bradleyadapter.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class bradleyController {
    private final RestTemplate restTemplate;

    @Value("${base.url}")
    private String BASE_URL;
    @Value("${base.url.test}")
    private String BASE_URL_TEST;
    @Value("${access.odata}")
    private String CREDENTIALS;

    Logger logger = LoggerFactory.getLogger(bradleyController.class);

    public bradleyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping({"/connection_test"})
    public @ResponseBody String testConnection(){

        return "<cashnet>result=0&respmessage=connection_successful</cashnet>";
    }
    @RequestMapping(path = "/pull", method = RequestMethod.POST,produces = {"application/xml"})
    public //@ResponseBody
    String getAllData(@RequestParam String cust_code2)  throws Exception {
          //Basic Auth Authentication
        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
        // httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        // httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);

        String requestUrl_look = BASE_URL + "tsdGet?cust_code2="+cust_code2+"&$format=json";
        String requestUrl_bal = BASE_URL + "tsbGet?cust_code2="+cust_code2+"&$format=json";
        String requestUrl_note = BASE_URL + "tsnGet?cust_code2="+cust_code2+"&$format=json";
        String requestUrl_schedule = BASE_URL + "tdbs?cust_code2="+cust_code2+"&$format=json";
        String requestUrl_transaction = BASE_URL + "tdbt?cust_code2="+cust_code2+"&$format=json";
        String requestUrl_amountcode = BASE_URL + "tisac?cust_code2="+cust_code2+"&$format=json";

        String rawJson_look = restTemplate.exchange(requestUrl_look,HttpMethod.GET, httpEntity, String.class).getBody();
        String rawJson_bal = restTemplate.exchange(requestUrl_bal, HttpMethod.GET, httpEntity,String.class).getBody();
        String rawJson_note = restTemplate.exchange(requestUrl_note, HttpMethod.GET, httpEntity,String.class).getBody();
        String rawJson_schedule = restTemplate.exchange(requestUrl_schedule, HttpMethod.GET, httpEntity,String.class).getBody();
        String rawJson_transaction = restTemplate.exchange(requestUrl_transaction, HttpMethod.GET, httpEntity,String.class).getBody();
        String rawJson_amountcode = restTemplate.exchange(requestUrl_amountcode, HttpMethod.GET, httpEntity,String.class).getBody();

        JSONObject root_look = new JSONObject(rawJson_look);
        JSONArray LookupResult = root_look.getJSONObject("d").getJSONArray("results");

        JSONObject root_bal = new JSONObject(rawJson_bal);
        JSONArray items = root_bal.getJSONObject("d").getJSONArray("results");

        JSONObject root_note = new JSONObject(rawJson_note);
        JSONArray notes = root_note.getJSONObject("d").getJSONArray("results");

        JSONObject root_schedule = new JSONObject(rawJson_schedule);
        JSONArray schedule = root_schedule.getJSONObject("d").getJSONArray("results");

        JSONObject root_transaction = new JSONObject(rawJson_transaction);
        JSONArray transaction = root_transaction.getJSONObject("d").getJSONArray("results");

        JSONObject root_amountcode = new JSONObject(rawJson_amountcode);
        JSONArray amountcode = root_amountcode.getJSONObject("d").getJSONArray("results");

        String flat_xml="<CustomerLookupResultDS>"+"\n";
        for(int i = 0; i < LookupResult.length(); i++){
            flat_xml=flat_xml+"<LookupResult>"+"\n";
            flat_xml=flat_xml+"<result>0</result>"+"\n";
            flat_xml=flat_xml+"<respmessage>Successful lookup</respmessage>"+"\n";
            flat_xml=flat_xml+"<cust_code>"+LookupResult.getJSONObject(i).getInt("rtn1")+"</cust_code>"+"\n";
            flat_xml=flat_xml+"<last_name>"+LookupResult.getJSONObject(i).getString("rtn2")+"</last_name>"+"\n";
            flat_xml=flat_xml+"<first_name>"+LookupResult.getJSONObject(i).getString("rtn3")+"</first_name>"+"\n";
            flat_xml=flat_xml+"<cust_group>"+LookupResult.getJSONObject(i).getString("rtn4")+"</cust_group>"+"\n";
            flat_xml=flat_xml+"<cust_pin>"+LookupResult.getJSONObject(i).getString("rtn5")+"</cust_pin>"+"\n";
            flat_xml=flat_xml+"<address_1>"+LookupResult.getJSONObject(i).getString("rtn6")+"</address_1>"+"\n";
            flat_xml=flat_xml+"<address_2>"+LookupResult.getJSONObject(i).getString("rtn7")+"</address_2>"+"\n";
            flat_xml=flat_xml+"<address_3>"+LookupResult.getJSONObject(i).getString("rtn8")+"</address_3>"+"\n";
            flat_xml=flat_xml+"<city>"+LookupResult.getJSONObject(i).getString("rtn9")+"</city>"+"\n";
            flat_xml=flat_xml+"<state>"+LookupResult.getJSONObject(i).getString("rtn10")+"</state>"+"\n";
            flat_xml=flat_xml+"<zip>"+LookupResult.getJSONObject(i).getString("rtn11")+"</zip>"+"\n";
            flat_xml=flat_xml+"<country>"+LookupResult.getJSONObject(i).getString("rtn12")+"</country>"+"\n";
            flat_xml=flat_xml+"<area_code>"+LookupResult.getJSONObject(i).getString("rtn13")+"</area_code>"+"\n";
            flat_xml=flat_xml+"<phone_number>"+LookupResult.getJSONObject(i).getString("rtn14")+"</phone_number>"+"\n";
            flat_xml=flat_xml+"<email_id>"+LookupResult.getJSONObject(i).getString("rtn15")+"</email_id>"+"\n";
            flat_xml=flat_xml+"<custotherdata>"+"\n";
            flat_xml=flat_xml+"<![CDATA["+"<activities>"+"\n";
            for(int j = 0; j < transaction.length(); j++){
                flat_xml=flat_xml+"<activity>"+"\n";
                flat_xml=flat_xml+"<Trans_type>"+transaction.getJSONObject(j).getString("rtn1")+"</Trans_type>"+"\n";
                flat_xml=flat_xml+"<term_code>"+transaction.getJSONObject(j).getString("rtn2")+"</term_code>"+"\n";
                flat_xml=flat_xml+"<activity_date>"+transaction.getJSONObject(j).getString("rtn3")+"</activity_date>"+"\n";
                flat_xml=flat_xml+"<item_desc>"+transaction.getJSONObject(j).getString("rtn4")+"</item_desc>"+"\n";
                flat_xml=flat_xml+"<Charge_amt>"+transaction.getJSONObject(j).getDouble("rtn5")+"</Charge_amt>"+"\n";
                flat_xml=flat_xml+"<Credit_amt>"+transaction.getJSONObject(j).getDouble("rtn6")+"</Credit_amt>"+"\n";
                flat_xml=flat_xml+"</activity>"+"\n";
            }
           /*
            if(schedule.length() == 0){
                flat_xml = flat_xml + "]]>" + "\n";
            }else if(schedule.length() > 0){
                flat_xml = flat_xml + "\n";
            }
            */

            for(int k = 0; k < schedule.length(); k++){
                flat_xml=flat_xml+"<CourseList>"+"\n";
                flat_xml=flat_xml+"<term_code>"+schedule.getJSONObject(k).getString("rtn1")+"</term_code>"+"\n";
                flat_xml=flat_xml+"<Course>"+schedule.getJSONObject(k).getString("rtn2")+"</Course>"+"\n";
                flat_xml=flat_xml+"<Class_nbr>"+schedule.getJSONObject(k).getString("rtn3")+"</Class_nbr>"+"\n";
                flat_xml=flat_xml+"<Section>"+schedule.getJSONObject(k).getString("rtn4")+"</Section>"+"\n";
                flat_xml=flat_xml+"<Course_descr>"+schedule.getJSONObject(k).getString("rtn5")+"</Course_descr>"+"\n";
                flat_xml=flat_xml+"<Units>"+schedule.getJSONObject(k).getDouble("rtn6")+"</Units>"+"\n";
                flat_xml=flat_xml+"<CourseFeeFlat>"+schedule.getJSONObject(k).getDouble("rtn7")+"</CourseFeeFlat>"+"\n";
                flat_xml=flat_xml+"<CourseFeePerHr>"+schedule.getJSONObject(k).getDouble("rtn8")+"</CourseFeePerHr>"+"\n";
                flat_xml=flat_xml+"<SurchargeFlat>"+schedule.getJSONObject(k).getDouble("rtn9")+"</SurchargeFlat>"+"\n";
                flat_xml=flat_xml+"<SurchargePerHr>"+schedule.getJSONObject(k).getDouble("rtn10")+"</SurchargePerHr>"+"\n";
               /*
                if(k == schedule.length()-1) {
                    flat_xml = flat_xml + "</CourseList>]]>" + "\n";
                }else{
                    flat_xml = flat_xml + "</CourseList>" + "\n";
                }
               */
                flat_xml = flat_xml + "</CourseList>" + "\n";
            }
            flat_xml=flat_xml+"</activities>]]>";
            flat_xml=flat_xml+"</custotherdata>"+"\n";
            flat_xml=flat_xml+"<dob>"+LookupResult.getJSONObject(i).getString("rtn16")+"</dob>"+"\n";
            flat_xml=flat_xml+"<custattr_1>"+LookupResult.getJSONObject(i).getString("rtn17")+"</custattr_1>"+"\n";
            flat_xml=flat_xml+"<custattr_2>"+LookupResult.getJSONObject(i).getString("rtn18")+"</custattr_2>"+"\n";
            flat_xml=flat_xml+"<custattr_3>"+LookupResult.getJSONObject(i).getString("rtn19")+"</custattr_3>"+"\n";
            flat_xml=flat_xml+"<custattr_4>"+LookupResult.getJSONObject(i).getString("rtn20")+"</custattr_4>"+"\n";
            flat_xml=flat_xml+"<alternateid_1>"+LookupResult.getJSONObject(i).getString("rtn21")+"</alternateid_1>"+"\n";
            flat_xml=flat_xml+"<alternateid_1_type>"+LookupResult.getJSONObject(i).getString("rtn22")+"</alternateid_1_type>"+"\n";
            flat_xml=flat_xml+"<alternateid_1_email>"+LookupResult.getJSONObject(i).getString("rtn23")+"</alternateid_1_email>"+"\n";
            flat_xml=flat_xml+"<alternateid_1_activeflag>"+LookupResult.getJSONObject(i).getString("rtn24")+"</alternateid_1_activeflag>"+"\n";
            flat_xml=flat_xml+"<alternateid_2>"+LookupResult.getJSONObject(i).getString("rtn25")+"</alternateid_2>"+"\n";
            flat_xml=flat_xml+"<alternateid_2_type>"+LookupResult.getJSONObject(i).getString("rtn26")+"</alternateid_2_type>"+"\n";
            flat_xml=flat_xml+"<alternateid_3>"+LookupResult.getJSONObject(i).getString("rtn27")+"</alternateid_3>"+"\n";
            flat_xml=flat_xml+"<alternateid_3_type>"+LookupResult.getJSONObject(i).getString("rtn28")+"</alternateid_3_type>"+"\n";
            flat_xml=flat_xml+"<alternateid_4>"+LookupResult.getJSONObject(i).getString("rtn29")+"</alternateid_4>"+"\n";
            flat_xml=flat_xml+"<alternateid_4_type>"+LookupResult.getJSONObject(i).getString("rtn30")+"</alternateid_4_type>"+"\n";

            flat_xml=flat_xml+"</LookupResult>"+"\n";
        }

       // String flat_item="";
        if(items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                flat_xml = flat_xml + "<items>" + "\n";
                flat_xml = flat_xml + "<item_code>" + items.getJSONObject(i).getString("rtn1") + "</item_code>" + "\n";
                flat_xml = flat_xml + "<bill_desc>" + items.getJSONObject(i).getString("rtn2") + "</bill_desc>" + "\n";
                // flat_xml=flat_xml+"<bill_no>"+items.getJSONObject(i).getInt("rtn3")+"</bill_no>"+"\n";
                // flat_xml=flat_xml+"<balance_due>"+items.getJSONObject(i).getDouble("rtn4")+"</balance_due>"+"\n";
                // flat_xml=flat_xml+"<minimum_due>"+items.getJSONObject(i).getDouble("rtn5")+"</minimum_due>"+"\n";
                flat_xml = flat_xml + "<bill_no>" + items.getJSONObject(i).optString("rtn3") + "</bill_no>" + "\n";
                flat_xml = flat_xml + "<balance_due>" + items.getJSONObject(i).optString("rtn4") + "</balance_due>" + "\n";
                flat_xml = flat_xml + "<minimum_due>" + items.getJSONObject(i).optString("rtn5") + "</minimum_due>" + "\n";
                flat_xml = flat_xml + "<balotherdata>" + items.getJSONObject(i).getString("rtn6") + "</balotherdata>" + "\n";
                flat_xml = flat_xml + "</items>" + "\n";
            }
        }else{
            flat_xml = flat_xml + "<items>" + "\n";
            flat_xml = flat_xml + "<item_code></item_code>" + "\n";
            flat_xml = flat_xml + "<bill_desc></bill_desc>" + "\n";
            flat_xml = flat_xml + "<bill_no></bill_no>" + "\n";
            flat_xml = flat_xml + "<balance_due></balance_due>" + "\n";
            flat_xml = flat_xml + "<minimum_due></minimum_due>" + "\n";
            flat_xml = flat_xml + "<balotherdata></balotherdata>" + "\n";
            flat_xml = flat_xml + "</items>" + "\n";
        }
        for(int i = 0; i < amountcode.length(); i++){
            flat_xml=flat_xml+"<IPPSourceAmount>"+"\n";
            flat_xml=flat_xml+"<termcode>"+amountcode.getJSONObject(i).getString("rtn1")+"</termcode>"+"\n";
            flat_xml=flat_xml+"<ippsourceamountcode>"+amountcode.getJSONObject(i).getString("rtn2")+"</ippsourceamountcode>"+"\n";
            flat_xml=flat_xml+"<amount>"+amountcode.getJSONObject(i).getString("rtn3")+"</amount>"+"\n";
            flat_xml=flat_xml+"</IPPSourceAmount>"+"\n";
        }
        for (int i = 0; i < notes.length(); i++) {
                flat_xml = flat_xml + "<notes>" + "\n";
                flat_xml = flat_xml + "<note_code>" + notes.getJSONObject(i).getString("rtn1") + "</note_code>" + "\n";
                flat_xml = flat_xml + "<note_exp_date>" + notes.getJSONObject(i).getString("rtn2") + "</note_exp_date>" + "\n";
                flat_xml = flat_xml + "</notes>" + "\n";
            }

       // String flat_note="";
        flat_xml=flat_xml+"</CustomerLookupResultDS>";
        return flat_xml;
    }

    @RequestMapping(path = "/push", method = RequestMethod.POST
            ,consumes = {"application/json"} ,
            produces = {"application/json"}
    )
    @ResponseBody
    public String getStudentBalance1(@RequestBody Cashnet cashnet) throws Exception {
         String requestUrl = BASE_URL + "TransactTransactionData?$format=json";

        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
        //httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> bodyParamMap = new HashMap<>();

        bodyParamMap.put("cust_code", cashnet.getCust_code());
        bodyParamMap.put("description", cashnet.getDescription());
        bodyParamMap.put("term_code", cashnet.getTerm_code());
        bodyParamMap.put("billno", cashnet.getBillno());
        bodyParamMap.put("busdate", cashnet.getBusdate());
        bodyParamMap.put("amount", String.valueOf(cashnet.getAmount()));
        bodyParamMap.put("batchno", cashnet.getBatchno());
        bodyParamMap.put("cctype", cashnet.getCctype());
        bodyParamMap.put("paymenttype", cashnet.getPaymenttype());
        bodyParamMap.put("paymentcode", cashnet.getPaymentcode());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(bodyParamMap);

       // logger.info(reqBodyData);
        HttpEntity<JsonNode> httpEntity = new HttpEntity<JsonNode>(jsonNode,httpHeaders);
        //HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(bodyParamMap, httpHeaders);
        logger.info(jsonNode.toString());
        logger.info(httpEntity.toString());
        // transactTransactionalImport;
        return restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity,String.class).getBody();
     }

     //BELOW ARE ENDPOINTS FOR TEST, Bradley wants to have prod and test enviroment to go side by side so instead of
    //setting a profile in the application.properties which would only enable us to run a single profile at a time
    //we've setup a separate endpoint for each prod and test

    @RequestMapping(path = "/pull_test", method = RequestMethod.POST,produces = {"application/xml"})
     public //@ResponseBody
     String getAllData_test(@RequestParam String cust_code2)  throws Exception {
         //Basic Auth Authentication
         String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

         HttpHeaders httpHeaders = new HttpHeaders();
         httpHeaders.set("Authorization", "Basic " + encodedCredentials);
         // httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         // httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

         HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);

         String requestUrl_look = BASE_URL_TEST + "tsdGet?cust_code2="+cust_code2+"&$format=json";
         String requestUrl_bal = BASE_URL_TEST + "tsbGet?cust_code2="+cust_code2+"&$format=json";
         String requestUrl_note = BASE_URL_TEST + "tsnGet?cust_code2="+cust_code2+"&$format=json";
         String requestUrl_schedule = BASE_URL_TEST + "tdbs?cust_code2="+cust_code2+"&$format=json";
         String requestUrl_transaction = BASE_URL_TEST + "tdbt?cust_code2="+cust_code2+"&$format=json";
         String requestUrl_amountcode = BASE_URL_TEST + "tisac?cust_code2="+cust_code2+"&$format=json";

         String rawJson_look = restTemplate.exchange(requestUrl_look,HttpMethod.GET, httpEntity, String.class).getBody();
         String rawJson_bal = restTemplate.exchange(requestUrl_bal, HttpMethod.GET, httpEntity,String.class).getBody();
         String rawJson_note = restTemplate.exchange(requestUrl_note, HttpMethod.GET, httpEntity,String.class).getBody();
         String rawJson_schedule = restTemplate.exchange(requestUrl_schedule, HttpMethod.GET, httpEntity,String.class).getBody();
         String rawJson_transaction = restTemplate.exchange(requestUrl_transaction, HttpMethod.GET, httpEntity,String.class).getBody();
         String rawJson_amountcode = restTemplate.exchange(requestUrl_amountcode, HttpMethod.GET, httpEntity,String.class).getBody();

         JSONObject root_look = new JSONObject(rawJson_look);
         JSONArray LookupResult = root_look.getJSONObject("d").getJSONArray("results");

         JSONObject root_bal = new JSONObject(rawJson_bal);
         JSONArray items = root_bal.getJSONObject("d").getJSONArray("results");

         JSONObject root_note = new JSONObject(rawJson_note);
         JSONArray notes = root_note.getJSONObject("d").getJSONArray("results");

         JSONObject root_schedule = new JSONObject(rawJson_schedule);
         JSONArray schedule = root_schedule.getJSONObject("d").getJSONArray("results");

         JSONObject root_transaction = new JSONObject(rawJson_transaction);
         JSONArray transaction = root_transaction.getJSONObject("d").getJSONArray("results");

         JSONObject root_amountcode = new JSONObject(rawJson_amountcode);
         JSONArray amountcode = root_amountcode.getJSONObject("d").getJSONArray("results");

         String flat_xml="<CustomerLookupResultDS>"+"\n";
         for(int i = 0; i < LookupResult.length(); i++){
             flat_xml=flat_xml+"<LookupResult>"+"\n";
             flat_xml=flat_xml+"<result>0</result>"+"\n";
             flat_xml=flat_xml+"<respmessage>Successful lookup</respmessage>"+"\n";
             flat_xml=flat_xml+"<cust_code>"+LookupResult.getJSONObject(i).getInt("rtn1")+"</cust_code>"+"\n";
             flat_xml=flat_xml+"<last_name>"+LookupResult.getJSONObject(i).getString("rtn2")+"</last_name>"+"\n";
             flat_xml=flat_xml+"<first_name>"+LookupResult.getJSONObject(i).getString("rtn3")+"</first_name>"+"\n";
             flat_xml=flat_xml+"<cust_group>"+LookupResult.getJSONObject(i).getString("rtn4")+"</cust_group>"+"\n";
             flat_xml=flat_xml+"<cust_pin>"+LookupResult.getJSONObject(i).getString("rtn5")+"</cust_pin>"+"\n";
             flat_xml=flat_xml+"<address_1>"+LookupResult.getJSONObject(i).getString("rtn6")+"</address_1>"+"\n";
             flat_xml=flat_xml+"<address_2>"+LookupResult.getJSONObject(i).getString("rtn7")+"</address_2>"+"\n";
             flat_xml=flat_xml+"<address_3>"+LookupResult.getJSONObject(i).getString("rtn8")+"</address_3>"+"\n";
             flat_xml=flat_xml+"<city>"+LookupResult.getJSONObject(i).getString("rtn9")+"</city>"+"\n";
             flat_xml=flat_xml+"<state>"+LookupResult.getJSONObject(i).getString("rtn10")+"</state>"+"\n";
             flat_xml=flat_xml+"<zip>"+LookupResult.getJSONObject(i).getString("rtn11")+"</zip>"+"\n";
             flat_xml=flat_xml+"<country>"+LookupResult.getJSONObject(i).getString("rtn12")+"</country>"+"\n";
             flat_xml=flat_xml+"<area_code>"+LookupResult.getJSONObject(i).getString("rtn13")+"</area_code>"+"\n";
             flat_xml=flat_xml+"<phone_number>"+LookupResult.getJSONObject(i).getString("rtn14")+"</phone_number>"+"\n";
             flat_xml=flat_xml+"<email_id>"+LookupResult.getJSONObject(i).getString("rtn15")+"</email_id>"+"\n";
             flat_xml=flat_xml+"<custotherdata>"+"\n";
             flat_xml=flat_xml+"<![CDATA["+"<activities>"+"\n";
             for(int j = 0; j < transaction.length(); j++){
                 flat_xml=flat_xml+"<activity>"+"\n";
                 flat_xml=flat_xml+"<Trans_type>"+transaction.getJSONObject(j).getString("rtn1")+"</Trans_type>"+"\n";
                 flat_xml=flat_xml+"<term_code>"+transaction.getJSONObject(j).getString("rtn2")+"</term_code>"+"\n";
                 flat_xml=flat_xml+"<activity_date>"+transaction.getJSONObject(j).getString("rtn3")+"</activity_date>"+"\n";
                 flat_xml=flat_xml+"<item_desc>"+transaction.getJSONObject(j).getString("rtn4")+"</item_desc>"+"\n";
                 flat_xml=flat_xml+"<Charge_amt>"+transaction.getJSONObject(j).getDouble("rtn5")+"</Charge_amt>"+"\n";
                 flat_xml=flat_xml+"<Credit_amt>"+transaction.getJSONObject(j).getDouble("rtn6")+"</Credit_amt>"+"\n";
                 flat_xml=flat_xml+"</activity>"+"\n";
             }
           /*
            if(schedule.length() == 0){
                flat_xml = flat_xml + "]]>" + "\n";
            }else if(schedule.length() > 0){
                flat_xml = flat_xml + "\n";
            }
            */

             for(int k = 0; k < schedule.length(); k++){
                 flat_xml=flat_xml+"<CourseList>"+"\n";
                 flat_xml=flat_xml+"<term_code>"+schedule.getJSONObject(k).getString("rtn1")+"</term_code>"+"\n";
                 flat_xml=flat_xml+"<Course>"+schedule.getJSONObject(k).getString("rtn2")+"</Course>"+"\n";
                 flat_xml=flat_xml+"<Class_nbr>"+schedule.getJSONObject(k).getString("rtn3")+"</Class_nbr>"+"\n";
                 flat_xml=flat_xml+"<Section>"+schedule.getJSONObject(k).getString("rtn4")+"</Section>"+"\n";
                 flat_xml=flat_xml+"<Course_descr>"+schedule.getJSONObject(k).getString("rtn5")+"</Course_descr>"+"\n";
                 flat_xml=flat_xml+"<Units>"+schedule.getJSONObject(k).getDouble("rtn6")+"</Units>"+"\n";
                 flat_xml=flat_xml+"<CourseFeeFlat>"+schedule.getJSONObject(k).getDouble("rtn7")+"</CourseFeeFlat>"+"\n";
                 flat_xml=flat_xml+"<CourseFeePerHr>"+schedule.getJSONObject(k).getDouble("rtn8")+"</CourseFeePerHr>"+"\n";
                 flat_xml=flat_xml+"<SurchargeFlat>"+schedule.getJSONObject(k).getDouble("rtn9")+"</SurchargeFlat>"+"\n";
                 flat_xml=flat_xml+"<SurchargePerHr>"+schedule.getJSONObject(k).getDouble("rtn10")+"</SurchargePerHr>"+"\n";
               /*
                if(k == schedule.length()-1) {
                    flat_xml = flat_xml + "</CourseList>]]>" + "\n";
                }else{
                    flat_xml = flat_xml + "</CourseList>" + "\n";
                }
               */
                 flat_xml = flat_xml + "</CourseList>" + "\n";
             }
             flat_xml=flat_xml+"</activities>]]>";
             flat_xml=flat_xml+"</custotherdata>"+"\n";
             flat_xml=flat_xml+"<dob>"+LookupResult.getJSONObject(i).getString("rtn16")+"</dob>"+"\n";
             flat_xml=flat_xml+"<custattr_1>"+LookupResult.getJSONObject(i).getString("rtn17")+"</custattr_1>"+"\n";
             flat_xml=flat_xml+"<custattr_2>"+LookupResult.getJSONObject(i).getString("rtn18")+"</custattr_2>"+"\n";
             flat_xml=flat_xml+"<custattr_3>"+LookupResult.getJSONObject(i).getString("rtn19")+"</custattr_3>"+"\n";
             flat_xml=flat_xml+"<custattr_4>"+LookupResult.getJSONObject(i).getString("rtn20")+"</custattr_4>"+"\n";
             flat_xml=flat_xml+"<alternateid_1>"+LookupResult.getJSONObject(i).getString("rtn21")+"</alternateid_1>"+"\n";
             flat_xml=flat_xml+"<alternateid_1_type>"+LookupResult.getJSONObject(i).getString("rtn22")+"</alternateid_1_type>"+"\n";
             flat_xml=flat_xml+"<alternateid_1_email>"+LookupResult.getJSONObject(i).getString("rtn23")+"</alternateid_1_email>"+"\n";
             flat_xml=flat_xml+"<alternateid_1_activeflag>"+LookupResult.getJSONObject(i).getString("rtn24")+"</alternateid_1_activeflag>"+"\n";
             flat_xml=flat_xml+"<alternateid_2>"+LookupResult.getJSONObject(i).getString("rtn25")+"</alternateid_2>"+"\n";
             flat_xml=flat_xml+"<alternateid_2_type>"+LookupResult.getJSONObject(i).getString("rtn26")+"</alternateid_2_type>"+"\n";
             flat_xml=flat_xml+"<alternateid_3>"+LookupResult.getJSONObject(i).getString("rtn27")+"</alternateid_3>"+"\n";
             flat_xml=flat_xml+"<alternateid_3_type>"+LookupResult.getJSONObject(i).getString("rtn28")+"</alternateid_3_type>"+"\n";
             flat_xml=flat_xml+"<alternateid_4>"+LookupResult.getJSONObject(i).getString("rtn29")+"</alternateid_4>"+"\n";
             flat_xml=flat_xml+"<alternateid_4_type>"+LookupResult.getJSONObject(i).getString("rtn30")+"</alternateid_4_type>"+"\n";

             flat_xml=flat_xml+"</LookupResult>"+"\n";
         }

         // String flat_item="";
         if(items.length() > 0) {
             for (int i = 0; i < items.length(); i++) {
                 flat_xml = flat_xml + "<items>" + "\n";
                 flat_xml = flat_xml + "<item_code>" + items.getJSONObject(i).getString("rtn1") + "</item_code>" + "\n";
                 flat_xml = flat_xml + "<bill_desc>" + items.getJSONObject(i).getString("rtn2") + "</bill_desc>" + "\n";
                 // flat_xml=flat_xml+"<bill_no>"+items.getJSONObject(i).getInt("rtn3")+"</bill_no>"+"\n";
                 // flat_xml=flat_xml+"<balance_due>"+items.getJSONObject(i).getDouble("rtn4")+"</balance_due>"+"\n";
                 // flat_xml=flat_xml+"<minimum_due>"+items.getJSONObject(i).getDouble("rtn5")+"</minimum_due>"+"\n";
                 flat_xml = flat_xml + "<bill_no>" + items.getJSONObject(i).optString("rtn3") + "</bill_no>" + "\n";
                 flat_xml = flat_xml + "<balance_due>" + items.getJSONObject(i).optString("rtn4") + "</balance_due>" + "\n";
                 flat_xml = flat_xml + "<minimum_due>" + items.getJSONObject(i).optString("rtn5") + "</minimum_due>" + "\n";
                 flat_xml = flat_xml + "<balotherdata>" + items.getJSONObject(i).getString("rtn6") + "</balotherdata>" + "\n";
                 flat_xml = flat_xml + "</items>" + "\n";
             }
         }else{
             flat_xml = flat_xml + "<items>" + "\n";
             flat_xml = flat_xml + "<item_code></item_code>" + "\n";
             flat_xml = flat_xml + "<bill_desc></bill_desc>" + "\n";
             flat_xml = flat_xml + "<bill_no></bill_no>" + "\n";
             flat_xml = flat_xml + "<balance_due></balance_due>" + "\n";
             flat_xml = flat_xml + "<minimum_due></minimum_due>" + "\n";
             flat_xml = flat_xml + "<balotherdata></balotherdata>" + "\n";
             flat_xml = flat_xml + "</items>" + "\n";
         }

         for(int i = 0; i < amountcode.length(); i++){
             flat_xml=flat_xml+"<IPPSourceAmount>"+"\n";
             flat_xml=flat_xml+"<termcode>"+amountcode.getJSONObject(i).getString("rtn1")+"</termcode>"+"\n";
             flat_xml=flat_xml+"<ippsourceamountcode>"+amountcode.getJSONObject(i).getString("rtn2")+"</ippsourceamountcode>"+"\n";
             flat_xml=flat_xml+"<amount>"+amountcode.getJSONObject(i).getString("rtn3")+"</amount>"+"\n";
             flat_xml=flat_xml+"</IPPSourceAmount>"+"\n";
         }
         for (int i = 0; i < notes.length(); i++) {
             flat_xml = flat_xml + "<notes>" + "\n";
             flat_xml = flat_xml + "<note_code>" + notes.getJSONObject(i).getString("rtn1") + "</note_code>" + "\n";
             flat_xml = flat_xml + "<note_exp_date>" + notes.getJSONObject(i).getString("rtn2") + "</note_exp_date>" + "\n";
             flat_xml = flat_xml + "</notes>" + "\n";
         }

         // String flat_note="";
         flat_xml=flat_xml+"</CustomerLookupResultDS>";
         return flat_xml;
     }

    @RequestMapping(path = "/push_test", method = RequestMethod.POST
            ,consumes = {"application/json"} ,
            produces = {"application/json"}
    )
    @ResponseBody
    public String getStudentBalance1_test(@RequestBody Cashnet cashnet) throws Exception {
        String requestUrl = BASE_URL_TEST + "TransactTransactionData?$format=json";

        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
        //httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> bodyParamMap = new HashMap<>();

        bodyParamMap.put("cust_code", cashnet.getCust_code());
        bodyParamMap.put("description", cashnet.getDescription());
        bodyParamMap.put("term_code", cashnet.getTerm_code());
        bodyParamMap.put("billno", cashnet.getBillno());
        bodyParamMap.put("busdate", cashnet.getBusdate());
        bodyParamMap.put("amount", String.valueOf(cashnet.getAmount()));
        bodyParamMap.put("batchno", cashnet.getBatchno());
        bodyParamMap.put("cctype", cashnet.getCctype());
        bodyParamMap.put("paymenttype", cashnet.getPaymenttype());
        bodyParamMap.put("paymentcode", cashnet.getPaymentcode());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(bodyParamMap);

        // logger.info(reqBodyData);
        HttpEntity<JsonNode> httpEntity = new HttpEntity<JsonNode>(jsonNode,httpHeaders);
        //HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(bodyParamMap, httpHeaders);
        logger.info(jsonNode.toString());
        logger.info(httpEntity.toString());
        // transactTransactionalImport;
        return restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity,String.class).getBody();
    }

    @GetMapping("term_code_validation_test")
    public @ResponseBody
    String term_code_list_test() throws Exception {

        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders1 = new HttpHeaders();
        httpHeaders1.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> httpEntity1 = new HttpEntity<>(null,httpHeaders1);

        String requestUrl_term_code_list = BASE_URL_TEST + "tpc?$format=json";

        String rawJson_term_code_list = restTemplate.exchange(requestUrl_term_code_list, HttpMethod.GET, httpEntity1,String.class).getBody();

        JSONObject root_term_code_list = new JSONObject(rawJson_term_code_list);
        JSONArray term_code_list = root_term_code_list.getJSONObject("d").getJSONArray("results");

        return term_code_list.toString();
        //return root_term_code_list;
    }
    @GetMapping("term_code_validation_prod")

    public @ResponseBody
    String term_code_list_prod() throws Exception {

        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders1 = new HttpHeaders();
        httpHeaders1.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> httpEntity1 = new HttpEntity<>(null,httpHeaders1);

        String requestUrl_term_code_list = BASE_URL + "tpc?$format=json";

        String rawJson_term_code_list = restTemplate.exchange(requestUrl_term_code_list, HttpMethod.GET, httpEntity1,String.class).getBody();

        JSONObject root_term_code_list = new JSONObject(rawJson_term_code_list);
        JSONArray term_code_list = root_term_code_list.getJSONObject("d").getJSONArray("results");

        return term_code_list.toString();
        //return root_term_code_list;
    }
}
