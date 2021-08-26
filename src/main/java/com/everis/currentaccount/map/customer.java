package com.everis.currentaccount.map; 

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class customer {  
	private String idclient; 
	private String dni;
	private String firstname;
	private String lastname;
	private String type;
}
