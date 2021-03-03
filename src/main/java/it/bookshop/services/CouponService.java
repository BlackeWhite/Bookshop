package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.Coupon;

public interface CouponService {
	Coupon findByID(long couponID);
	Coupon findByCode(String code);
	List <Coupon> findAll();
	Coupon create(String code, int discount, Date expireDate);
	Coupon create(Coupon coupon);
	Coupon update(Coupon coupon);
	void delete(Long couponID);
	void delete(Coupon coupon);
	void deleteAll();
};