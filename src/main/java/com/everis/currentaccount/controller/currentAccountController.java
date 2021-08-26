package com.everis.currentaccount.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*; 

import com.everis.currentaccount.dto.message;
import com.everis.currentaccount.model.currentAccount;
import com.everis.currentaccount.model.movements;
import com.everis.currentaccount.service.currentAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono; 

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
@RequestMapping
public class currentAccountController {
	@Autowired
	currentAccountService service;

	private Mono<Object> BindingResultErrors(BindingResult bindinResult){
		String msg = "";

		for (int i = 0; i < bindinResult.getAllErrors().size(); i++) {
			msg = bindinResult.getAllErrors().get(0).getDefaultMessage();
		}
		return Mono.just(new message(msg));
	}

	@PostMapping("/save")
	public Mono<Object> created(@RequestBody @Valid currentAccount model, BindingResult bindinResult) {

		if (bindinResult.hasErrors()) {
			return BindingResultErrors(bindinResult);
		}
		return service.save(model);
	}

	@PostMapping("/movememts")
	public Mono<Object> registedMovememts(@RequestBody @Valid movements model, BindingResult bindinResult) {

		if (bindinResult.hasErrors()) {
			return BindingResultErrors(bindinResult);
		}

		return service.saveMovements(model);
	}

	@PostMapping("/addTransfer")
	public Mono<Object> addTransfer(@RequestBody @Valid movements model, BindingResult bindinResult) {

		if (bindinResult.hasErrors()) {
			return BindingResultErrors(bindinResult);
		}

		return service.saveTransfer(model);
	}

	@GetMapping("/")
	public Flux<Object> findAll() {
		return service.getAll();
	}

	@GetMapping("/byNumberAccount/{number}")
	public Mono<Object> findOneByNumberAccount(@PathVariable("number") String number) {
		return service.getOne(number);
	}

	@GetMapping("/verifyByNumberAccount/{number}")
	public Mono<Boolean> verifyByNumberAccount(@PathVariable("number") String number) {
		return service._verifyByNumberAccount(number);
	}

	@GetMapping("/byCustomer/{id}")
	public Flux<Object> findByCustomer(@PathVariable("id") String id) {
		return service.getByCustomer(id);
	}
}