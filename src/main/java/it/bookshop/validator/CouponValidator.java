package it.bookshop.validator;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.bookshop.model.entity.Coupon;
import it.bookshop.services.CouponService;

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
		
		Coupon existing = couponService.findByCode(coupon.getCode().trim().toUpperCase());
		if(existing != null) {
			errors.rejectValue("code", "invalidCode", new Object[] { "'code'" }, "Codice già esistente.");
		}
		
		//La data di scadenza non deve essere precedente alla data odierna
		Date current = new Date(System.currentTimeMillis());
		if(current.after(coupon.getExpireDate())) {
			errors.rejectValue("expireDate", "invalidExpireDate", new Object[] { "'expireDate'" }, "Inserisci una data futura.");
		}
		
	}

}
