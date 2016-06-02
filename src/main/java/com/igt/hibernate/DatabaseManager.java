package com.igt.hibernate;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.SessionFactoryUtil;
import com.igt.hibernate.bean.Costumer;
import com.igt.hibernate.bean.IgtOrder;
import com.igt.hibernate.bean.PeerGroup;

public class DatabaseManager {
	
protected static final Logger log = LogManager.getLogger(DatabaseManager.class);
	
	public DatabaseManager(){
		log.debug("Database Manager initialized");
	}

	public void beginTransaction() throws Exception {
		Transaction transaction = null;
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		transaction = session.getTransaction();

		try {
			transaction.begin();
		} catch (RuntimeException runtimeException) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (HibernateException hibernateException) {
					log.fatal("Error rolling back transaction", hibernateException);
				}
				throw runtimeException;
			}
		}
	}

	public void commitTransaction() throws Exception {
		Transaction transaction = null;
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		transaction = session.getTransaction();

		try {
			transaction.commit();
		} catch (RuntimeException runtimeException) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (HibernateException hibernateException) {
					log.fatal("Error rolling back transaction", hibernateException);
				}
				throw runtimeException;
			}
			throw runtimeException;
		}
	}

	public void endTransaction() {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		if (session.isOpen())
			session.close();
	}

	public void flushSession() throws Exception {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.flush();
		session.clear();

	}
	
	public boolean isSessionOpen(){
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		
		return session.isOpen();
	}

	public void deleteEntity(Object entity) throws Exception {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.delete(entity);

	}
	
	public void saveOrUpdateEntity(Object entity) throws Exception {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(entity);

	}

	public void saveEntity(Object entity) throws Exception {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.save(entity);

	}

	public void updateEntity(Object entity) throws Exception {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.merge(entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<IgtOrder> getAllOrders(){
		Session session = null;
		List<IgtOrder> orders = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		orders = session.createCriteria(IgtOrder.class)
				.list();
		
		return orders;
	}
	
	@SuppressWarnings("unchecked")
	public List<Costumer> getAllCostumers(){
		Session session = null;
		List<Costumer> costumer = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		costumer = session.createCriteria(Costumer.class)
				.list();
		
		return costumer;
	}
	
	public Costumer getCostumerById(int costumerId){
		Session session = null;
		Costumer costumer = null;
		
		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		costumer = (Costumer) session.createCriteria(Costumer.class)
				.add(Restrictions.eq("costumerId", costumerId))
				.uniqueResult();
		
		return costumer;
	}
	
	public PeerGroup getPeerGroupByCostumerId(int ownerId){
		Session session = null;
		PeerGroup peerGroup = null;
		
		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		peerGroup = (PeerGroup) session.createCriteria(PeerGroup.class)
				.add(Restrictions.eq("ownerId", ownerId))
				.uniqueResult();
		
		return peerGroup;
	}
}
