package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}