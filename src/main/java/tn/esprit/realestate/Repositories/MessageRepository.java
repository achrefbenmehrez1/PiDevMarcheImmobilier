package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}