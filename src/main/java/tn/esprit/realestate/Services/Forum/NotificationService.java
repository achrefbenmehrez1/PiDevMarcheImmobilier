package tn.esprit.realestate.Services.Forum;

import org.springframework.stereotype.Service;
import tn.esprit.realestate.Dto.Forum.NotificationDto;
import tn.esprit.realestate.Entities.Forum.Notification;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.Forum.INotificationService;
import tn.esprit.realestate.Repositories.Forum.NotificationRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<NotificationDto> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId).stream().map(n -> new NotificationDto(n.getMessage(), n.getReadByte(), n.getUser().getUsername(), n.getUser().getEmail(), LocalDateTime.now())).toList();
    }

    @Override
    public void sendNotification(String message, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> {
            Notification notification = new Notification();
            notification.setCreatedAt(LocalDateTime.now());
            notification.setMessage(message);
            notification.setUser(u);
            notification.setReadByte(false);
            notificationRepository.save(notification);
        });
    }

    @Override
    public List<NotificationDto> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadByteFalse(userId).stream().map(n -> new NotificationDto(n.getMessage(), n.getReadByte(), n.getUser().getUsername(), n.getUser().getEmail(), LocalDateTime.now())).toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        notification.ifPresent(n -> {
            n.setReadByte(true);
            notificationRepository.save(n);
        });
    }

    @Override
    public void markAllNotificationsAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification notification : notifications) {
            notification.setReadByte(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public void deleteAllNotificationsByUser(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }
}
