package com.igt.hibernate;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.SessionFactoryUtil;
import com.igt.hibernate.bean.Costumer;
import com.igt.hibernate.bean.IgtOrder;
import com.igt.hibernate.bean.PeerGroup;
import com.igt.hibernate.bean.Resource;
import com.igt.hibernate.bean.Servlet;
import com.igt.hibernate.bean.Step;
import com.igt.hibernate.bean.Process;

public class DatabaseManager {

	protected static final Logger log = LogManager.getLogger(DatabaseManager.class);

	public DatabaseManager() {
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

	public void flushSession() {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.flush();
		session.clear();

	}

	public boolean isSessionOpen() {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();

		return session.isOpen();
	}

	public void deleteEntity(Object entity) {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.delete(entity);

	}

	public void saveOrUpdateEntity(Object entity) {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(entity);

	}

	public void saveEntity(Object entity) {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.save(entity);

	}

	public void updateEntity(Object entity) {
		Session session = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.merge(entity);
	}

	@SuppressWarnings("unchecked")
	public List<IgtOrder> getAllOrders() {
		Session session = null;
		List<IgtOrder> orders = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		orders = session.createCriteria(IgtOrder.class).list();

		return orders;
	}

	@SuppressWarnings("unchecked")
	public List<Costumer> getAllCostumers() {
		Session session = null;
		List<Costumer> costumer = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		costumer = session.createCriteria(Costumer.class).list();

		return costumer;
	}

	public Costumer getCostumerById(int costumerId) {
		Session session = null;
		Costumer costumer = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		costumer = (Costumer) session.createCriteria(Costumer.class).add(Restrictions.eq("costumerId", costumerId))
				.uniqueResult();

		return costumer;
	}

	public PeerGroup getPeerGroupByCostumerId(int ownerId) {
		Session session = null;
		PeerGroup peerGroup = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		peerGroup = (PeerGroup) session.createCriteria(PeerGroup.class).add(Restrictions.eq("ownerId", ownerId))
				.uniqueResult();

		return peerGroup;
	}

	public Servlet getServletByPath(String path) {
		Session session = null;
		Servlet servlet = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		servlet = (Servlet) session.createCriteria(Servlet.class).add(Restrictions.eq("path", path)).uniqueResult();

		return servlet;

	}

	public Servlet getServletByBaseUrl(String baseUrl) {
		Session session = null;
		Servlet servlet = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		servlet = (Servlet) session.createCriteria(Servlet.class).add(Restrictions.eq("baseUrl", baseUrl))
				.uniqueResult();

		return servlet;

	}

	public Resource getResourceByPath(String path) {
		Session session = null;
		Resource resource = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		resource = (Resource) session.createCriteria(Resource.class).add(Restrictions.eq("path", path)).uniqueResult();

		return resource;
	}

	public Process getProcessByName(String name) {
		Session session = null;
		Process process = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		process = (Process) session.createCriteria(Process.class).add(Restrictions.eq("name", name)).uniqueResult();

		return process;
	}

	public Step getStepByNameAndProcess(Process process, String name) {
		Session session = null;
		Step step = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		step = (Step) session.createCriteria(Step.class).add(Restrictions.eq("name", name))
				.add(Restrictions.eq("process", process)).uniqueResult();

		return step;
	}

	public Servlet getServletById(int id) {
		Session session = null;
		Servlet servlet = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		servlet = (Servlet) session.createCriteria(Servlet.class).add(Restrictions.eq("id", id)).uniqueResult();

		return servlet;
	}

	@SuppressWarnings("unchecked")
	public List<Resource> getResourcesByServlet(Servlet servlet) {
		Session session = null;
		List<Resource> resources = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		resources = session.createCriteria(Resource.class).add(Restrictions.eq("servlet", servlet)).list();

		return resources;
	}

	@SuppressWarnings("unchecked")
	public List<Step> getStepsByProcess(Process process) {
		Session session = null;
		List<Step> resources = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		resources = session.createCriteria(Step.class).add(Restrictions.eq("process", process)).list();

		return resources;
	}

	public Process getProcessById(int bpmnID) {
		Session session = null;
		Process process = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		process = (Process) session.createCriteria(Process.class).add(Restrictions.eq("id", bpmnID)).uniqueResult();

		return process;
	}

	public int getResourceIdByMethodNameAndServletId(String methodName, int servletId) {
		Session session = null;
		int resourceId;

		String sql = "SELECT method.resource_id FROM method JOIN resource ON method.resource_id = resource.id JOIN servlet ON resource.servlet_id = servlet.id WHERE servlet.id = "
				+ servletId + " AND method.name = \"" + methodName + "\"";

		System.out.println(sql);

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		resourceId = (int) query.uniqueResult();

		return resourceId;
	}

	public Resource getResourceById(int resourceId) {
		Session session = null;
		Resource resource = null;

		session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		resource = (Resource) session.createCriteria(Resource.class).add(Restrictions.eq("id", resourceId))
				.uniqueResult();

		return resource;
	}
}
