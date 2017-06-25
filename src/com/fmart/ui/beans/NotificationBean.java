package com.fmart.ui.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.pojos.Notifications;


@ManagedBean
@ViewScoped
public class NotificationBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Notifications[] selectedNotifications;
	private Notifications selectedNotification = new Notifications();
	private List<Notifications> notifications;
	
	public List<Notifications> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notifications> notifications) {
		this.notifications = notifications;
	}

	public Notifications getSelectedNotification() {
		return selectedNotification;
	}

	public void setSelectedNotification(Notifications selectedNotification) {
		this.selectedNotification = selectedNotification;
	}

	public Notifications[] getSelectedNotifications() {
		return selectedNotifications;
	}

	public void setSelectedNotifications(Notifications[] selectedNotifications) {
		this.selectedNotifications = selectedNotifications;
	}
	@PostConstruct
	public void populateNotificationList() {
		FMartGenericDAO dao = new FMartGenericDAO();
		notifications= dao.findAll(Notifications.class);
		
	}
	
	public void approveNotification(ActionEvent actionEvent) {
		FMartGenericDAO dao = new FMartGenericDAO();
		for (Notifications notification : selectedNotifications) {
			dao.delete(notification,notification.getId());
		}
		populateNotificationList();
	}

}
