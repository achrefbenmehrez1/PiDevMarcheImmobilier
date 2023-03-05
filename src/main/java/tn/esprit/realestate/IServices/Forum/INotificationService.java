package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Dto.Forum.NotificationDto;

import java.util.List;

public interface INotificationService {
    void sendNotification(String message, Long userId);

    List<NotificationDto> getUnreadNotifications(Long userId);

    void markAsRead(Long notificationId);

    public void markAllNotificationsAsRead(Long userId);

    public void deleteNotification(Long notificationId);

    public void deleteAllNotificationsByUser(Long userId);

    public List<NotificationDto> getNotificationsByUser(Long userId);
}