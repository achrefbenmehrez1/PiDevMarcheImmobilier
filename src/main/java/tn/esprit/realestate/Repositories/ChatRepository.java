package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}