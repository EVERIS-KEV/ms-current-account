package com.everis.currentaccount.model;

import com.everis.currentaccount.consumer.*;
import com.everis.currentaccount.dto.*;

import java.util.*;
import javax.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
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
  private String dateCreated = new Date().toString();
  private double amount = 0.0;
  private List<movements> movements = new ArrayList<movements>(); 

  @NotBlank(message = "Debe seleccionar un cliente.")
  private String idCustomer;
}
