package birch.irc.feature.urlgrabber;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface UrlRepository extends CrudRepository<Url, Long>, Repository<Url, Long>{
    List<Url> findByNick(String nick);
    List<Url> findByChannel(String channel);
    List<Url> findLastUrlByChannel(String channel, Pageable pageable);
}
