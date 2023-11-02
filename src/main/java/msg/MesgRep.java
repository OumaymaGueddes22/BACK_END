package msg;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesgRep extends MongoRepository<Message,String> {
    List<Message> findMessagesById(String id );
}
