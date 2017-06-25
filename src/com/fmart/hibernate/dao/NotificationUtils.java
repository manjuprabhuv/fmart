package com.fmart.hibernate.dao;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.framework.hibernate.HibernateUtil;
import com.fmart.hibernate.pojos.Notifications;

public class NotificationUtils {
	private static final Logger log = LoggerFactory
			.getLogger(FMartGenericDAO.class);

	private static Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public static <T> String getJsonVal(T instance) {

		return toObjectString(instance);
	}

	public enum NotificationType {
		UPDATE, DELETE;
	}

	public static void saveNotification(String fromValue, String toValue,
			Class page, String userName, NotificationType type) {
		Notifications notification = new Notifications(fromValue, toValue, page
				.getSimpleName(), userName, type.toString());
		NotificationsDao dao = new NotificationsDao();
		notification.setDate(new Date());
		dao.save(notification);

	}

	private static String toObjectString(Object obj) {
		try {
			ObjectWriter ow = new ObjectMapper().writer();
			String retString = ow.writeValueAsString(obj);
			retString = retString.replaceAll("  ", "");
			return retString;
			// System.out.println(test);
		} catch (JsonGenerationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}

}
