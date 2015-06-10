package birch.irc.feature.log;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface IrcLogServerRepository extends
        CrudRepository<IrcLogServer, Long>, Repository<IrcLogServer, Long> {

    IrcLogServer findByServer(String server);
}
