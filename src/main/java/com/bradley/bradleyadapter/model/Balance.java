package com.bradley.bradleyadapter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonDeserialize(using = BalanaceJsonDeserialiser.class)
public class Balance {

   //@SerializedName("item_code")
   //@Expose
    public String item_code;

  // @SerializedName("bill_desc")
  // @Expose
    public String bill_desc;

  // @SerializedName("bill_no")
  // @Expose
    public Integer bill_no;

  // @SerializedName("balance_due")
  // @Expose
    public Double balance_due;

 // @SerializedName("minimum_due")
 // @Expose
    public Double minimum_due;

 // @SerializedName("balotherdata")
  //@Expose
    public String balotherdata;

}
