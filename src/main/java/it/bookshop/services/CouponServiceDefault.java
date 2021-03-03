package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.CouponDao;
import it.bookshop.model.entity.Coupon;

@Transactional
@Service("couponService")
public class CouponServiceDefault implements CouponService {

	@Autowired
	private CouponDao couponRepository;
	
	@Override
	public Coupon findByID(long couponID) {
		return couponRepository.findByID(couponID);
	}
	
	@Override
	public Coupon findByCode(String code) {
		return couponRepository.findByCode(code);
	}

	@Override
	public List<Coupon> findAll() {
		return couponRepository.findAll();
	}
	
	@Override
	public Coupon create(String code, int discount, Date expireDate) {
		return couponRepository.create(code, discount, expireDate); 
	}
	
	@Override
	public Coupon create(Coupon coupon) {
		return couponRepository.create(coupon);
	}
	
	@Override
	public Coupon update(Coupon coupon) {
		return couponRepository.update(coupon);
	}
	
	@Override
	public void delete(Long couponID) {
		Coupon c = couponRepository.findByID(couponID);
		couponRepository.delete(c);
	}
	
	@Override
	public void delete(Coupon coupon) {
		couponRepository.delete(coupon);
	}
	
	@Override
	public void deleteAll() {
		List<Coupon> coupons = findAll();
		for (Coupon c : coupons) {
			delete(c);
		}
	}
}