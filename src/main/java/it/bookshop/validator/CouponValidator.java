package it.bookshop.validator;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.bookshop.model.entity.Coupon;
import it.bookshop.services.CouponService;

//Validatore per l'aggiunta di un coupon
@Component("couponValidator")
public class CouponValidator implements Validator {

	@Autowired
	CouponService couponService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Coupon.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Coupon coupon = (Coupon) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "code.required", "Inserisci un codice.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "expireDate", "expireDate.required", "Inserisci una data di scadenza.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "discount", "discount.required", "Inserisci la quantità di sconto.");
		
		//Verifica l'esistenza di uno stesso codice
		//I codici sono case insensitive
		Coupon existing = couponService.findByCode(coupon.getCode().trim().toUpperCase());
		if(existing != null) {
			errors.rejectValue("code", "invalidCode", new Object[] { "'code'" }, "Codice già esistente.");
		}
		
		if(coupon.getDiscount() < 5 || coupon.getDiscount() > 50) {
			errors.rejectValue("discount", "invalidDiscount", new Object[] { "'discount'" }, "Lo sconto deve essere compreso tra 5 e 50 %.");
		}
		
		//La data di scadenza non deve essere precedente alla data odierna
		Date current = new Date(System.currentTimeMillis());
		if(current.after(coupon.getExpireDate())) {
			errors.rejectValue("expireDate", "invalidExpireDate", new Object[] { "'expireDate'" }, "Inserisci una data futura.");
		}
		
	}

}
