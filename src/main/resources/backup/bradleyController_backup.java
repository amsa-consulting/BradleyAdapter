package backup;

import com.bradley.bradleyadapter.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class bradleyController_backup {
    private final RestTemplate restTemplate;

    @Value("${base.url}")
    private String BASE_URL;
    @Value("${access.odata}")
    private String CREDENTIALS;

    Logger logger = LoggerFactory.getLogger(bradleyController_backup.class);

    public bradleyController_backup(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @PostMapping({"/header_test"})
    public String headerData(){
        ResponseEntity<String> headerResponse= restTemplate.getForEntity("https://jsonplaceholder.typicode.com/posts",String.class);
        HttpHeaders header=headerResponse.getHeaders();
        return header.get("Connection").get(0);
    }
    @PostMapping({"/connection_test"})
    public @ResponseBody String testConnection(){

        return "<cashnet>result=0&respmessage=connection_successful</cashnet>";
    }

    @GetMapping({"/note_csrf"})
    //in @PathVariable the URL will be /notes/315596/json
    public //@ResponseBody
        // List<Note> getNotes(@RequestParam String cust_code) throws Exception {
    String getNotesCsrf(String cust_code2) throws Exception {
        List<Note> allNote=new ArrayList<Note>();

        String requestUrl = BASE_URL + "tsnGet?cust_code2="+cust_code2+"&$format=json";
        logger.info("requestUrl {}",requestUrl.toString());
        logger.info("cust_code {}",cust_code2.toString());
        ;
        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
        //httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-csrf-token","fetch");

        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);
        logger.info("httpEntity {}",httpEntity);

        String rawJson_note = String.valueOf(restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class).getHeaders());

        return rawJson_note;
    }
   /*
    private static HttpHeaders getHeaders ()
    {
        String credentials = "DBA:bWdMec327v5c4z98";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }
    */
    // @GetMapping({"/{note}/{cust_code}/{format}"})
   //public @ResponseBody Object getNotes(@PathVariable("cust_code") String cust_code, @PathVariable("format") String format) throws Exception {
   @GetMapping({"/note"})
        //in @PathVariable the URL will be /notes/315596/json
       public //@ResponseBody
      // List<Note> getNotes(@RequestParam String cust_code) throws Exception {
       List<Note> getNotes(String cust_code2) throws Exception {
       List<Note> allNote=new ArrayList<Note>();

         String requestUrl = BASE_URL + "tsnGet?cust_code2="+cust_code2+"&$format=json";
        logger.info("requestUrl {}",requestUrl.toString());
        logger.info("cust_code {}",cust_code2.toString());
;
        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
        //httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       httpHeaders.set("x-csrf-token","fetch");

        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);
        logger.info("httpEntity {}",httpEntity);

       String rawJson_note =restTemplate.exchange(requestUrl,HttpMethod.GET, httpEntity, String.class).getBody();

            JSONObject root_note = new JSONObject(rawJson_note);

            JSONArray results_note = root_note.getJSONObject("d").getJSONArray("results");
            for(int i = 0; i < results_note.length(); i++) {
                // the JSON data
                JSONObject jsonBalance_note = results_note.getJSONObject(i);

                Note balance = new Note();

                String note_code = jsonBalance_note.optString("rtn1");
                String note_exp_date = jsonBalance_note.optString("rtn2");

                balance.setNote_code(note_code);
                balance.setNote_exp_date(note_exp_date);

                allNote.add(balance);
            }
            return allNote;
    }
   // @GetMapping({"/{student}/{demography}/{cust_code}/{format}"})
   //public @ResponseBody Object getDemography(@PathVariable("cust_code") String cust_code, @PathVariable("format") String format) throws Exception {
   //@GetMapping ({"/demography"})
   // public List<Demography> getDemography(@RequestParam("cust_code2") String cust_code2) throws Exception {
   public List<Demography> getDemography(String cust_code2) throws Exception {
       List<Demography> allDemography=new ArrayList<Demography>();
        //in @PathVariable the URL will be /notes/315596/json
        //@GetMapping("/note")
        //public @ResponseBody List getRecordFetch(@PathParam("cust_code") String cust_code, @PathParam("$format") String format) throws Exception {
        // in @PathParm the url will be like /notes?cust_code=315196&$format=json
        //String requestUrl = BASE_URL + "tsdGet?cust_code="+cust_code+"&$format="+format;
        String requestUrl = BASE_URL + "tsdGet?cust_code2="+cust_code2+"&$format=json";
        logger.info("requestUrl {}",requestUrl.toString());
        logger.info("cust_code2 {}",cust_code2.toString());
        //http://solus.bradley.edu:8099/bill/tsnGet?cust_code=315596&$format=json

        // HttpHeaders httpHeaders = getHeaders();

        //String credentials = "DBA:bWdMec327v5c4z98";
       // String credentials = "Transact:IceySunnyflowers22";
        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
        //httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);
        logger.info("httpEntity {}",httpEntity);
       String rawJson = restTemplate.exchange(requestUrl,HttpMethod.GET, httpEntity, String.class).getBody();

       JSONObject root = new JSONObject(rawJson);

       JSONArray results = root.getJSONObject("d").getJSONArray("results");
       for(int i = 0; i < results.length(); i++) {
           // the JSON data
           JSONObject jsonDemography = results.getJSONObject(i);

           Demography demography = new Demography();
           String cust_code = String.valueOf(jsonDemography.getInt("rtn1"));
           String last_name = jsonDemography.getString("rtn2");
           String first_name = jsonDemography.getString("rtn3");
           String cust_group = jsonDemography.getString("rtn4");
           String cust_pin = jsonDemography.getString("rtn5");
           String address_1 = jsonDemography.getString("rtn6");
           String address_2 = jsonDemography.getString("rtn7");
           String address_3 = jsonDemography.getString("rtn8");
           String city = jsonDemography.getString("rtn9");
           String state = jsonDemography.getString("rtn10");
           String zip = jsonDemography.getString("rtn11");
           String country = jsonDemography.getString("rtn12");
           String area_code = jsonDemography.getString("rtn13");
           String phone_number = jsonDemography.getString("rtn14");
           String email_id = jsonDemography.getString("rtn15");
           String dob = jsonDemography.getString("rtn16");
           String custattr_1 = jsonDemography.getString("rtn17");
           String custattr_2 = jsonDemography.getString("rtn18");
           String custattr_3 = jsonDemography.getString("rtn19");
           String custattr_4 = jsonDemography.getString("rtn20");
           String alternateid_1 = jsonDemography.getString("rtn21");
           String alternateid_1_type = jsonDemography.getString("rtn22");
           String alternateid_1_email = jsonDemography.getString("rtn23");
           String alternateid_1_activeflag = jsonDemography.getString("rtn24");
           String alternateid_2 = jsonDemography.getString("rtn25");
           String alternateid_2_type = jsonDemography.getString("rtn26");
           String alternateid_3 = jsonDemography.getString("rtn27");
           String alternateid_3_type = jsonDemography.getString("rtn28");
           String alternateid_4 = jsonDemography.getString("rtn29");
           String alternateid_4_type = jsonDemography.getString("rtn30");

           demography.setResult(0);
           demography.setRespmessage("Successful lookup");
           demography.setCust_code(Integer.valueOf(cust_code));
           demography.setLast_name(last_name);
           demography.setFirst_name(first_name);
           demography.setCust_group(cust_group);
           demography.setCust_pin(cust_pin);
           demography.setAddress_1(address_1);
           demography.setAddress_2(address_2);
           demography.setAddress_3(address_3);
           demography.setCity(city);
           demography.setState(state);
           demography.setZip(zip);
           demography.setCountry(country);
           demography.setArea_code(area_code);
           demography.setPhone_number(phone_number);
           demography.setEmail_id(email_id);
           demography.setDob(dob);
           demography.setCustattr_1(custattr_1);
           demography.setCustattr_2(custattr_2);
           demography.setCustattr_3(custattr_3);
           demography.setCustattr_4(custattr_4);
           demography.setAlternateid_1(alternateid_1);
           demography.setAlternateid_1_type(alternateid_1_type);
           demography.setAlternateid_1_email(alternateid_1_email);
           demography.setAlternateid_1_activeflag(alternateid_1_activeflag);
           demography.setAlternateid_2(alternateid_2);
           demography.setAlternateid_2_type(alternateid_2_type);
           demography.setAlternateid_3(alternateid_3);
           demography.setAlternateid_3_type(alternateid_3_type);
           demography.setAlternateid_4(alternateid_4);
           demography.setAlternateid_4_type(alternateid_4_type);

           allDemography.add(demography);
       }
       return allDemography;
   }
    @GetMapping({"/balance1"})
     public @ResponseBody Object getStudentBalance1(@RequestParam("cust_code2") String cust_code2) throws Exception {
        String requestUrl = BASE_URL + "tsbGet?cust_code2="+cust_code2+"&$format=json";
        logger.info("requestUrl {}",requestUrl.toString());
        logger.info("cust_code2 {}",cust_code2.toString());

        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
       // httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);
        logger.info("httpEntity {}",httpEntity);

        try
        {
            return restTemplate.exchange(requestUrl,HttpMethod.GET, httpEntity, JsonNode.class);
        }
        catch(HttpClientErrorException ex)
        {
            return ex.getMessage();
        }
    }
   // @GetMapping({"/balance1"})
  // @RequestMapping(path = "/balance", method = RequestMethod.GET,produces = { "application/json" })
   public //@ResponseBody
  // List<Balance> getStudentBalance(@RequestParam("cust_code") String cust_code) throws Exception {
   List<Balance> getStudentBalance(String cust_code2) throws Exception {
       List<Balance> allBalances=new ArrayList<Balance>();
        String requestUrl = BASE_URL + "tsbGet?cust_code2="+cust_code2+"&$format=json";
        logger.info("requestUrl {}",requestUrl.toString());
        logger.info("cust_code {}",cust_code2.toString());

        String encodedCredentials = new String(Base64.encodeBase64(CREDENTIALS.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);
       // httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       // httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> httpEntity = new HttpEntity<>(null,httpHeaders);
        logger.info("httpEntity {}",httpEntity);

       String rawJson = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity,String.class).getBody();

       JSONObject root = new JSONObject(rawJson);

       JSONArray results = root.getJSONObject("d").getJSONArray("results");
       for(int i = 0; i < results.length(); i++) {
           // the JSON data
           JSONObject jsonBalance = results.getJSONObject(i);

           Balance balance = new Balance();

           String item_code = jsonBalance.getString("rtn1");
           String bill_desc = jsonBalance.getString("rtn2");
           String bill_no = String.valueOf(jsonBalance.getInt("rtn3"));
           String balance_due = String.valueOf(jsonBalance.getDouble("rtn4"));
           String minimum_due = String.valueOf(jsonBalance.getDouble("rtn5"));
           String balotherdata = jsonBalance.getString("rtn6");

           balance.setItem_code(item_code);
           balance.setBill_desc(bill_desc);
           balance.setBill_no(Integer.valueOf(bill_no));
           balance.setBalance_due(Double.valueOf(balance_due));
           balance.setMinimum_due(Double.valueOf(minimum_due));
           balance.setBalotherdata(balotherdata);

           allBalances.add(balance);
       }
       return allBalances;
      //  return rawJson;
    }
    @RequestMapping(path = "/pull1", method = RequestMethod.POST,produces = {"application/xml"})
    //path = "/aggregate", method = RequestMethod.GET,produces = { "application/xml" }
    public //@ResponseBody
    String getAllData1(@RequestParam String cust_code2) throws Exception {
        UpperWrapper upperWrapper = new UpperWrapper();
        DataMerged dataMerged = new DataMerged();

        List<UpperWrapper> upperWrapperData = new ArrayList<>();
        List<DataMerged> allData = new ArrayList<>();
        List<Demography> student_demography = new ArrayList<>();
        List<Balance> student_balace = new ArrayList<>();
        List<Note> student_note = new ArrayList<>();


        student_demography = getDemography(cust_code2);
        student_balace = getStudentBalance(cust_code2);
        student_note = getNotes(cust_code2);


        dataMerged.setLookupResult(student_demography);
       // dataMerged.setItems(student_balace);
        dataMerged.setNotes(student_note);

        allData.add(dataMerged);
        upperWrapper.setCustomerLookupResultDS(allData);
        upperWrapperData.add(upperWrapper);

        JSONObject jsonObject = new JSONObject(upperWrapperData.get(0));
        String xml = XML.toString(jsonObject);
        xml.replace("<lookupResult>","<LookupResult>");
        xml.replace("</lookupResult>","</LookupResult>");

        return xml;
    }
    @RequestMapping(path = "/pull", method = RequestMethod.POST,produces = {"application/xml"})
    //path = "/aggregate", method = RequestMethod.GET,produces = { "application/xml" }
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
                flat_xml=flat_xml+"<Course>"+schedule.getJSONObject(k).getString("rtn1")+"</Course>"+"\n";
                flat_xml=flat_xml+"<Class_nbr>"+schedule.getJSONObject(k).getString("rtn2")+"</Class_nbr>"+"\n";
                flat_xml=flat_xml+"<Section>"+schedule.getJSONObject(k).getString("rtn3")+"</Section>"+"\n";
                flat_xml=flat_xml+"<Course_descr>"+schedule.getJSONObject(k).getString("rtn4")+"</Course_descr>"+"\n";
                flat_xml=flat_xml+"<Units>"+schedule.getJSONObject(k).getDouble("rtn5")+"</Units>"+"\n";
                flat_xml=flat_xml+"<CourseFeeFlat>"+schedule.getJSONObject(k).getDouble("rtn6")+"</CourseFeeFlat>"+"\n";
                flat_xml=flat_xml+"<CourseFeePerHr>"+schedule.getJSONObject(k).getDouble("rtn7")+"</CourseFeePerHr>"+"\n";
                flat_xml=flat_xml+"<SurchargeFlat>"+schedule.getJSONObject(k).getDouble("rtn8")+"</SurchargeFlat>"+"\n";
                flat_xml=flat_xml+"<SurchargePerHr>"+schedule.getJSONObject(k).getDouble("rtn9")+"</SurchargePerHr>"+"\n";
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
        for(int i = 0; i < items.length(); i++){
            flat_xml=flat_xml+"<items>"+"\n";
            flat_xml=flat_xml+"<item_code>"+items.getJSONObject(i).getString("rtn1")+"</item_code>"+"\n";
            flat_xml=flat_xml+"<bill_desc>"+items.getJSONObject(i).getString("rtn2")+"</bill_desc>"+"\n";
            flat_xml=flat_xml+"<bill_no>"+items.getJSONObject(i).getInt("rtn3")+"</bill_no>"+"\n";
            flat_xml=flat_xml+"<balance_due>"+items.getJSONObject(i).getDouble("rtn4")+"</balance_due>"+"\n";
            flat_xml=flat_xml+"<minimum_due>"+items.getJSONObject(i).getDouble("rtn5")+"</minimum_due>"+"\n";
            flat_xml=flat_xml+"<balotherdata>"+items.getJSONObject(i).getString("rtn6")+"</balotherdata>"+"\n";
            flat_xml=flat_xml+"</items>"+"\n";
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
    //for test consumes changed to {"application/json"} from {"application/xml"}
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
/*
        Cashnet body=new Cashnet();
        body.setCust_code(cashnet.getCust_code());
        body.setDescription(cashnet.getDescription());
        body.setTerm_code(cashnet.getTerm_code());
        body.setBillno(cashnet.getBillno());
        body.setBusdate(cashnet.getBusdate());
        body.setAmount(cashnet.getAmount());
        body.setBatchno(cashnet.getBatchno());
        body.setCctype(cashnet.getCctype());
        body.setPaymenttype(cashnet.getPaymenttype());
        body.setPaymentcode(cashnet.getPaymentcode());
*/

        //Map<String, String> bodyParamMap = new HashMap<String, String>();
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

        /*
        String cust_code;
        String description;
        String term_code;
        String billno;
        String busdate;
         Double amount;
        String batchno;
        String cctype;
        String paymenttype;
         String paymentcode;
         if(cashnet.getBillno().isEmpty()){
             billno=null;
         }else{
             billno=cashnet.getBillno();
         }
        String json_string="";
        json_string="{"+
                               "\""+"cust_code"+"\""+":"+cashnet.getCust_code()+"\n"+
                                "\""+"description"+"\""+":"+"\""+cashnet.getDescription()+"\""+"\n"+
                                "\""+"term_code"+"\""+":"+"\""+cashnet.getTerm_code()+"\""+"\n"+
                                "\""+"billno"+"\""+":"+"\""+billno+"\""+"\n"+
                                "\""+"busdate"+"\""+":"+"\""+cashnet.getBusdate()+"\""+"\n"+
                                "\""+"amount"+"\""+":"+cashnet.getAmount()+"\n"+
                                "\""+"batchno"+"\""+":"+"\""+cashnet.getBatchno()+"\""+"\n"+
                                "\""+"cctype"+"\""+":"+"\""+cashnet.getCctype()+"\""+"\n"+
                                "\""+"paymenttype"+"\""+":"+"\""+cashnet.getPaymenttype()+"\""+"\n"+
                                "\""+"paymentcode"+"\""+":"+"\""+cashnet.getPaymentcode()+"\""+"\n"+
                "}";
        json_string.replace(" ","");
        */

       // String reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);

      //  Cashnet body = new ObjectMapper().readValue(reqBodyData, Cashnet.class);
        //HttpEntity<String> requestEnty = new HttpEntity<>(reqBodyData, header);

        //ResponseEntity<Object> result = restTmplt.postForEntity(reqUrl, requestEnty, Object.class);
        //return result;
/*
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr ="";
        try {
            // Converting the Java object into a JSON string
            jsonStr = Obj.writeValueAsString(bodyParamMap);
            // Displaying Java object into a JSON string
            //System.out.println(jsonStr);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
      logger.info(jsonStr);
*/
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(bodyParamMap);

       // logger.info(reqBodyData);
        HttpEntity<JsonNode> httpEntity = new HttpEntity<JsonNode>(jsonNode,httpHeaders);
        //HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(bodyParamMap, httpHeaders);
        logger.info(jsonNode.toString());
        logger.info(httpEntity.toString());
        // transactTransactionalImport;
        return restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity,String.class).getBody();
        //ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, httpEntity, String.class);
        // check response
       /*
        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println(response.getBody());
            return  "Request Successful";

        } else {
            System.out.println(response.getStatusCode());
            return "Request Failed";

        }
        */

        //ResponseEntity<Cashnet> result = restTemplate.postForEntity(requestUrl, httpEntity, Cashnet.class);
        // return jsonStr;
       // return jsonNode;
    }

    @RequestMapping(path = "/casetest", method = RequestMethod.POST,produces = {"application/xml"})
    //path = "/aggregate", method = RequestMethod.GET,produces = { "application/xml" }
    public //@ResponseBody
    String casetest() throws Exception {
        UpperWrapper upperWrapper = new UpperWrapper();
        DataMerged dataMerged = new DataMerged();

        List<UpperWrapper> upperWrapperData = new ArrayList<UpperWrapper>();
        List<DataMerged> allData = new ArrayList<DataMerged>();
        List<Demography> student_demography = new ArrayList<Demography>();
        List<Balance> student_balace = new ArrayList<Balance>();
        List<Note> student_note = new ArrayList<Note>();

       DataMerged dataMerged1=new DataMerged();
       List<DataMerged> dataMergedList=new ArrayList<>();

        ObjectMapper Obj = new ObjectMapper();
        String jsonStr;
        dataMerged1 = getObjectData(dataMerged1);
        dataMergedList.add(dataMerged1);
            // get Organisation object as a json string
        jsonStr = Obj.writeValueAsString(dataMergedList.get(0));
        JSONObject root = new JSONObject(jsonStr);
       /*
        JsonNode jsonTree = new ObjectMapper().readTree(file.getInputStream());
        JSONArray arr = new JSONArray(jsonTree.toString());
        */
        //JSONArray results = root.getJSONObject("items").getJSONArray("results");
        JSONArray LookupResult = root.getJSONArray("LookupResult");
        JSONArray items = root.getJSONArray("items");
        JSONArray notes = root.getJSONArray("notes");
       // JSONArray LookupResult = new JSONArray(LookupResult_arr.toString());
      //  JSONArray items = new JSONArray(items_arr.toString());
      //  JSONArray notes = new JSONArray(notes_arr.toString());
        //  String file_string= (jsonArray.getString(0)).toString();

       // JSONArray arr = new JSONArray(student_demography.toString());

        String flat_xml="";
        for(int i = 0; i < LookupResult.length(); i++){
            flat_xml=flat_xml+"<LookupResult>"+"\n";
            flat_xml=flat_xml+"<result>"+LookupResult.getJSONObject(i).getInt("result")+"</result>"+"\n";
            flat_xml=flat_xml+"<respmessage>"+LookupResult.getJSONObject(i).getString("respmessage")+"</respmessage>"+"\n";
            flat_xml=flat_xml+"<cust_code>"+LookupResult.getJSONObject(i).getInt("cust_code")+"</cust_code>"+"\n";
            flat_xml=flat_xml+"<last_name>"+LookupResult.getJSONObject(i).getString("last_name")+"</last_name>"+"\n";
            flat_xml=flat_xml+"<first_name>"+LookupResult.getJSONObject(i).getString("first_name")+"</first_name>"+"\n";
            flat_xml=flat_xml+"<custattr_4>"+LookupResult.getJSONObject(i).getString("custattr_4")+"</custattr_4>"+"\n";
            flat_xml=flat_xml+"<alternateid_1>"+LookupResult.getJSONObject(i).getString("alternateid_1")+"</alternateid_1>"+"\n";
            flat_xml=flat_xml+"</LookupResult>"+"\n";
         }

        String flat_item="";
        for(int i = 0; i < items.length(); i++){
            flat_xml=flat_xml+"<items>"+"\n";
            flat_xml=flat_xml+"<item_code>"+items.getJSONObject(i).getString("item_code")+"</item_code>"+"\n";
            flat_xml=flat_xml+"<bill_desc>"+items.getJSONObject(i).getString("bill_desc")+"</bill_desc>"+"\n";
            flat_xml=flat_xml+"<bill_no>"+items.getJSONObject(i).getInt("bill_no")+"</bill_no>"+"\n";
            flat_xml=flat_xml+"<balance_due>"+items.getJSONObject(i).getDouble("balance_due")+"</balance_due>"+"\n";
            flat_xml=flat_xml+"<minimum_due>"+items.getJSONObject(i).getDouble("minimum_due")+"</minimum_due>"+"\n";
            flat_xml=flat_xml+"<balotherdata>"+items.getJSONObject(i).getString("balotherdata")+"</balotherdata>"+"\n";
            flat_xml=flat_xml+"</items>"+"\n";
        }

        String flat_note="";
        for(int i = 0; i < notes.length(); i++){
            flat_xml=flat_xml+"<notes>"+"\n";
            flat_xml=flat_xml+"<note_code>"+notes.getJSONObject(i).getString("note_code")+"</note_code>"+"\n";
            flat_xml=flat_xml+"<note_exp_date>"+notes.getJSONObject(i).getString("note_exp_date")+"</note_exp_date>"+"\n";
            flat_xml=flat_xml+"</notes>"+"\n";
        }

        return flat_xml;
    }

    public DataMerged getObjectData(DataMerged dataMerged)
    {
        Note note1 = new Note();
        Note note2 = new Note();
        Note note3 = new Note();

        Balance balance=new Balance();
        Balance balance1=new Balance();

        Demography demography=new Demography();
        Demography demography1=new Demography();

        List<Note> list = new ArrayList<>();
        List<Balance> balanceList=new ArrayList<>();
        List<Demography> demographyList=new ArrayList<>();

        balance.setItem_code("test");
        balance.setBill_desc("test");
        balance.setBill_no(5);
        balance.setBalance_due(78.9);
        balance.setMinimum_due(34.0);
        balance.setBalotherdata("test");

        balance1.setItem_code("test");
        balance1.setBill_desc("test");
        balance1.setBill_no(5);
        balance1.setBalance_due(78.9);
        balance1.setMinimum_due(34.0);
        balance1.setBalotherdata("test");


        demography.setResult(7);
        demography.setRespmessage("test");
        demography.setCust_code(8);
        demography.setLast_name("test");
        demography.setFirst_name("test");
        demography.setCust_group("test");
      demography.setCust_pin("test");
      demography.setAddress_1("test");
      demography.setAddress_2("test");
      demography.setAddress_3("test");
      demography.setCity("test");
      demography.setState("test");
      demography.setZip("test");
      demography.setCountry("test");
      demography.setArea_code("test");
      demography.setPhone_number("test");
      demography.setEmail_id("test");
      demography.setDob("tedt");
      demography.setCustattr_1("");
      demography.setCustattr_2("");
      demography.setCustattr_3("");
      demography.setCustattr_4("");
      demography.setAlternateid_1("");
      demography.setAlternateid_1_type("");
      demography.setAlternateid_1_email("");
      demography.setAlternateid_1_activeflag("");
      demography.setAlternateid_2("");
      demography.setAlternateid_2_type("");
      demography.setAlternateid_3("");
      demography.setAlternateid_3_type("");
      demography.setAlternateid_4("");
      demography.setAlternateid_4_type("");


        demography1.setResult(7);
        demography1.setRespmessage("test");
        demography1.setCust_code(8);
        demography1.setLast_name("test");
        demography1.setFirst_name("test");
        demography1.setCust_group("test");
        demography1.setCust_pin("test");
        demography1.setAddress_1("test");
        demography1.setAddress_2("test");
        demography1.setAddress_3("test");
        demography1.setCity("test");
        demography1.setState("test");
        demography1.setZip("test");
        demography1.setCountry("test");
        demography1.setArea_code("test");
        demography1.setPhone_number("test");
        demography1.setEmail_id("test");
        demography1.setDob("tedt");
        demography1.setCustattr_1("");
        demography1.setCustattr_2("");
        demography1.setCustattr_3("");
        demography1.setCustattr_4("");
        demography1.setAlternateid_1("");
        demography1.setAlternateid_1_type("");
        demography1.setAlternateid_1_email("");
        demography1.setAlternateid_1_activeflag("");
        demography1.setAlternateid_2("");
        demography1.setAlternateid_2_type("");
        demography1.setAlternateid_3("");
        demography1.setAlternateid_3_type("");
        demography1.setAlternateid_4("");
        demography1.setAlternateid_4_type("");


        note1.setNote_exp_date("test1");
        note1.setNote_code("test2");
        note2.setNote_code("test3");
        note2.setNote_exp_date("test4");
        note3.setNote_code("test3");
        note3.setNote_exp_date("test4");

        list.add(note1);
        list.add(note2);
        list.add(note3);

        balanceList.add(balance);
        balanceList.add(balance1);

        demographyList.add(demography);
        demographyList.add(demography1);
       // return list;
        // Insert the data
       dataMerged.setNotes(list);
       dataMerged.setItems(balanceList);
       dataMerged.setLookupResult(demographyList);
        // Return the object
        return dataMerged;
    }
}
