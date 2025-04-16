package com.venyou.service.impl;

import com.venyou.model.Notification;
import com.venyou.model.User;
import com.venyou.repository.NotificationRepository;
import com.venyou.repository.UserRepository;
import com.venyou.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public void notifyAdmins(String message) {
        List<User> admins = userRepository.findAll().stream()
                .filter(user -> "ADMIN".equals(user.getRole()))
                .toList();

        for (User admin : admins) {
            Notification notification = new Notification();
            notification.setUser(admin);
            notification.setMessage(message);
            notification.setStatus(Notification.Status.UNREAD); // Use enum
            notificationRepository.save(notification);
        }
    }
}