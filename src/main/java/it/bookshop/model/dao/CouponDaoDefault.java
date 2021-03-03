package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Coupon;


@Repository("couponDao")
public class CouponDaoDefault extends DefaultDao implements CouponDao {
	
	@Override
	public Coupon findByID(long couponID) {
		return this.getSession().createQuery("FROM Coupon c WHERE c.couponID = :couponID", Coupon.class).setParameter("couponID", couponID)
				.getSingleResult();
		}
	
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
	public List<Coupon> findAll() {
		return this.getSession().createQuery("FROM Coupon c", Coupon.class).getResultList();
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
	public Coupon create(Coupon coupon) {
		coupon.setUsageCounter(0);
		this.getSession().save(coupon);
		return coupon;
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
