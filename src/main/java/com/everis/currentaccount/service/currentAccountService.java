package com.everis.currentaccount.service;

import com.everis.currentaccount.consumer.webclient;
import com.everis.currentaccount.dto.message;
import com.everis.currentaccount.map.customer;
import com.everis.currentaccount.model.*;
import com.everis.currentaccount.repository.currentAccountRepository;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.*;

@Service
@Transactional
public class currentAccountService {
  @Autowired
  currentAccountRepository repository;

  static ScheduledExecutorService executor = null;

  private Boolean verifyCustomer(String id) {
    return webclient.customer
      .get()
      .uri("/verifyId/{id}", id)
      .retrieve()
      .bodyToMono(Boolean.class)
      .block();
  }

  private customer customerFind(String id) {
    return webclient.customer
      .get()
      .uri("/{id}", id)
      .retrieve()
      .bodyToMono(customer.class)
      .block();
  }

  private double getAmountByNumber(String number) {
    return repository.findByAccountNumber(number).getAmount();
  }

  private double getAmountByID(String id) {
    return repository.findById(id).get().getAmount();
  }

  private void setAmountByID( String id ) {
	  double amount =  getAmountByID(id) - 1;
	  currentAccount model =  repository.findById(id).get();
	  model.setAmount( amount ); 
	  repository.save(model);
  }

  private String setAmount(movements movement) {
    double val = getAmountByNumber(movement.getAccountNumber());
    currentAccount model = repository.findByAccountNumber(movement.getAccountNumber());

    if (movement.getType().equals("Deposito")) {
      model.setAmount(movement.getAmount() + val);
      model.getMovements().add(movement);
    } else {
      if (movement.getAmount() > val) return "Cantidad insuficiente."; else {
        model.setAmount(val - movement.getAmount());
        model.getMovements().add(movement);
      }
    }

    repository.save(model);
    return "Movimiento realizado";
  }

  private void chronometer(String id) {
    TimerTask _timerTask = new TimerTask() {

      @Override
      public void run() {
        String datestring = new SimpleDateFormat("dd").format(new Date());
        String timestring = new SimpleDateFormat("HH:mm:ss").format(new Date()); 
        
        System.out.println( datestring + " - " + timestring );
        
        if( datestring.equals("13") && timestring.equals("12:52:20") ) setAmountByID( id ); 
        
      }
    };
    executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(_timerTask, 1, 1, TimeUnit.SECONDS);
  }

  public Mono<Object> saveMovements(movements model) {
    String msg = "Movimiento realizado";

    if (repository.existsByAccountNumber(model.getAccountNumber())) {
      if (model.getType().equals("Deposito") || model.getType().equals("Retiro")) {
        String idaccount = repository
          .findByAccountNumber(model.getAccountNumber())
          .getIdCurrentAccount();

        int count = (int) repository
          .findById(idaccount)
          .get()
          .getMovements()
          .stream()
          .count();

        if (count == 0) {
          chronometer(idaccount);
        } setAmount(model);
      } else msg = "Selecione una operacion correcta.";
    } else msg = "Numero de cuenta incorrecto.";

    return Mono.just(new message(msg));
  }

  public Mono<Object> save(currentAccount model) {
    String msg = "Cuenta creada.";

    if (verifyCustomer(model.getIdCustomer())) {
      String typeCustomer = customerFind(model.getIdCustomer()).getType();

      if (typeCustomer.equals("empresarial")) repository.save(model); else {
        if (!repository.existsByIdCustomer(model.getIdCustomer())) repository.save(
          model
        ); else msg = "Usted ya no puede abrir otra cuenta.";
      }
    } else msg = "Cliente no registrado";

    return Mono.just(new message(msg));
  }

  public Flux<Object> getAll() {
    return Flux.fromIterable(repository.findAll());
  }

  public Mono<Object> getOne(String id) {
    return Mono.just(repository.findByAccountNumber(id));
  }

  public Flux<Object> getByCustomer(String id) {
    return Flux.fromIterable(
      repository.findAll().stream().filter(c -> c.getIdCustomer().equals(id)).toList()
    );
  }
}
