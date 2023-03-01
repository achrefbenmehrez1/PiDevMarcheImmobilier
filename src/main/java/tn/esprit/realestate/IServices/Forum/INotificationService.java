package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Forum.Notification;

import java.util.List;

public interface INotificationService {
    void sendNotification(String message, Long userId);

    List<Notification> getUnreadNotifications(Long userId);

    void markAsRead(Long notificationId);
}