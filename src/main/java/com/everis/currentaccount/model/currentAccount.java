package com.everis.currentaccount.model;

import com.everis.currentaccount.consumer.webclient;  
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter 
@Document(collection = "current-account")
public class currentAccount {
  @Id
  private String idCurrentAccount;

  private String accountNumber = webclient.logic
    .get()
    .uri("/generatedNumberLong/12")
    .retrieve()
    .bodyToMono(String.class)
    .block();
  private Date dateCreated = new Date();
  private double amount = 0.0;
  private List<movements> movement = new ArrayList<movements>();
  private String profile;

  @NotBlank(message = "Debe seleccionar un cliente.")
  private String idCustomer;

  public currentAccount() {
    this.profile = ""; 
  }

  public currentAccount(String idCustomer) {
    this.profile = "";
    this.idCustomer = idCustomer;
  }

  public currentAccount(String idCustomer, String profile) {
    this.profile = profile;
    this.idCustomer = idCustomer;
  }
}
