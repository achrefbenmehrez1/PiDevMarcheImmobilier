package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}