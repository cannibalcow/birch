package birch.irc.feature.urlgrabber;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UrlRepository extends MongoRepository<Url, Long> {
    List<Url> findByNick(String nick);
    List<Url> findByChannel(String channel);
    List<Url> findLastUrlByChannel(String channel, Pageable pageable);
}
