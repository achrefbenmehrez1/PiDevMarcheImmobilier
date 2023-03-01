package tn.esprit.realestate.Services.Forum;

import org.springframework.stereotype.Service;
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
    public void sendNotification(String message, Long userId) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadln(false);
        User user = userRepository.findById(userId).orElse(null);
        notification.setUser(user);
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadlnFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        notification.ifPresent(n -> {
            n.setReadln(true);
            notificationRepository.save(n);
        });
    }
}
