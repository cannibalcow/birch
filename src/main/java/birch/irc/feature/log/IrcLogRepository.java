package birch.irc.feature.log;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface IrcLogRepository extends CrudRepository<IrcLogLine, Long>, Repository<IrcLogLine, Long> {
    List<IrcLogLine> findByIrcLogServerAndChannel(IrcLogServer ircLogServer, String channel);
}
