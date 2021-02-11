package it.bookshop.model.dao;

import java.sql.Date;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Coupon;

@Transactional
@Repository("couponDao")
public class CouponDaoDefault extends DefaultDao implements CouponDao {
	
	@Override
	public Coupon findByCode(String code) {
		try {
			return this.getSession().createQuery("FROM Coupon c WHERE c.code = :code", Coupon.class)
					.setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Coupon create(String code, int discount, Date expireDate) {
		Coupon c = new Coupon();
		c.setCode(code);
		c.setDiscount(discount);
		c.setExpireDate(expireDate);
		c.setUsageCounter(0);
		this.getSession().save(c);
		return c;
	}

	@Override
	public Coupon update(Coupon coupon) {
		return (Coupon) this.getSession().merge(coupon);
	}

	@Override
	public void delete(Coupon coupon) {
		this.getSession().delete(coupon);
	}

}
