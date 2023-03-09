package tn.esprit.realestate.Repositories.Forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Forum.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}