package com.bradley.bradleyadapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@Data
public class DataMerged {
    //For Demography
    //@XmlElementWrapper(name="LookupResult")
    @JsonProperty("LookupResult")
    List<Demography> LookupResult;
    List<Balance> items;
    List<Note> notes;
    /*
    private Integer cust_code;
    private String last_name;
    private String first_name;
    private String cust_group;
    private String cust_pin;
    private String address_1;
    private String address_2;
    private String address_3;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String area_code;
    private String phone_number;
    private String email_id;
    private String dob;
    private String custattr_1;
    private String custattr_2;
    private String custattr_3;
    private String custattr_4;

    private String alternateid_1;
    private String alternateid_1_type;
    private String alternateid_1_email;
    private String alternateid_1_activeflag;
    private String alternateid_2;
    private String alternateid_2_type;
    private String alternateid_3;
    private String alternateid_3_type;
    private String alternateid_4;
    private String alternateid_4_type;
*/
    //Balance
    /*
    public String item_code;
    public String bill_desc;
    public Integer bill_no;
    public Double balance_due;
    public Double minimum_due;
    public String balotherdata;
*/
    //Note
    /*
    String note_code;
    String note_exp_date;
    */

}
