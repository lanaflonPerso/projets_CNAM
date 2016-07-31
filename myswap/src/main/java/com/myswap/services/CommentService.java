package com.myswap.services;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.myswap.models.Comment;
import com.myswap.models.User;

/**
 * Classe effectuant le CRUD pour les objets de type Comment.
 * 
 */
//@Path("comment")
//@Secured
public class CommentService {

	private Session session;
	private static Logger logger = Logger.getLogger(CommentService.class);
	
	/**
	 * UserService.
	 */
	private UserService userService = new UserService();
	public void setUserService(UserService userService){this.userService = userService;}

//	@GET
//	@Path("/get/{id}")
//	@Produces({ "application/json" })
	public Comment findComment(long id) {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
		Comment comment = null;
		try {
			Criteria criteria = session.createCriteria(Comment.class);

			criteria.add(Restrictions.eqOrIsNull("id", id));

			// pour la pagination, on peut ajouter criteria.setMaxResults(10),
			// etc, et utiliser une cl� de reprise � chaque appel.
			// inutilis� dans le cadre de ce projet.

			comment = (Comment) criteria.uniqueResult();
		} catch (RuntimeException e) {
			logger.error("RuntimeException in CommentService/findComment : " + e.getMessage());
		} finally {
			session.close();
		}
		return comment;
	}

	/**
	 * M�thode de test d'insertion en cascade via cascade=CascadeType.PERSIST
	 * 
	 */
//	@POST
//	@Path("/insert")
//	@Consumes({"application/json"})
	public long insertComment(String label, Integer mark,
			 String notingId, String notedId) {

		Comment comment = new Comment();
		comment.setLabel(label);
		comment.setMark(mark);
		
		User noting = new User();
		noting = userService.findUser(notingId);
		User noted = new User();
		noted = userService.findUser(notedId);
		comment.setNoting(noting);
		comment.setNoted(noted);

		try {
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();

			session.beginTransaction();

			session.save(comment);

			session.getTransaction().commit();

		} catch (RuntimeException e) {
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			logger.error("RuntimeException in CommentService/insertComment : " + e.getMessage());
		} finally {
			session.close();
		}

		return comment.getIdComment();

	}

	/**
	 * M�thode pour retour sur l'insertion en cascade.
	 * 
	 */
//	@DELETE
//	@Path("/delete/{id}")
	public void deleteComment(long id) {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();

		// Suppression du comment en base.
		try {

			Criteria commentList = session.createCriteria(Comment.class).add(Restrictions.eqOrIsNull("id_comment", id));
			for (Object comment : commentList.list()) {
				session.delete(comment);
			}

			session.getTransaction().commit();
		} catch (RuntimeException e) {
			if (session.getTransaction() != null)
				session.getTransaction().rollback();
			logger.error("RuntimeException in CommentService/deleteComment : " + e.getMessage());
		} finally {
			session.close();
		}

	}

	/**
	 * Update de la classe comment.
	 */
//	@POST
//	@Path("/update")
//	@Consumes({ "application/json" })
	public void updateComment(Long id, String label, Integer mark,
			String notingId, String notedId) {

		Comment comment = findComment(id);
		comment.setLabel(label);
		comment.setMark(mark);
		User noting = new User();
		noting = userService.findUser(notingId);
		User noted = new User();
		noted = userService.findUser(notedId);
		comment.setNoting(noting);
		comment.setNoted(noted);
		
		try {
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();

			session.beginTransaction();

			session.saveOrUpdate(comment);

			session.getTransaction().commit();

		} catch (RuntimeException e) {
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			logger.error("RuntimeException in CommentService/updateComment : " + e.getMessage());
		} finally {
			session.close();
		}

	}

}