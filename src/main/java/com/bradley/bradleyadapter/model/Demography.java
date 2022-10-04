package com.bradley.bradleyadapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class Demography {
    //@SerializedName("cust_code")
    //@Expose
    private Integer result;
    private String respmessage;
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


}
