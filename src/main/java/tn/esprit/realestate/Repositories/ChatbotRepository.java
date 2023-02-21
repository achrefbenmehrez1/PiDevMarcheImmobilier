package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Chatbot;

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {
}