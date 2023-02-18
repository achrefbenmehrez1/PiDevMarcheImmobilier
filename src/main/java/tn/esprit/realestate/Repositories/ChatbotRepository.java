package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Chatbot;

public interface ChatbotRepository extends JpaRepository<Chatbot, Integer> {
}