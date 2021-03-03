package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Coupon;

public interface CouponDao {
	Session getSession();
	public void setSession(Session session);
	
	Coupon findByID(long couponID);
	
	Coupon findByCode(String code);
	
	List<Coupon> findAll();
	
	Coupon create(String code, int discount, Date expireDate);
	
	Coupon create(Coupon coupon);
	
	Coupon update(Coupon coupon);
	
	void delete(Coupon rcoupon);

}