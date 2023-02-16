package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Chatbot;

public interface ChatbotRepository extends JpaRepository<Chatbot, Integer> {
}