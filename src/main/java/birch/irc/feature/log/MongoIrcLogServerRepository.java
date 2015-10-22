package birch.irc.feature.log;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoIrcLogServerRepository implements IrcLogLineRepository {


    private final MongoOperations operations;

    @Autowired
    public MongoIrcLogServerRepository(MongoOperations operations) {
        this.operations = operations;
    }

    @Override
    public IrcLogLine save(IrcLogLine ircLogServer) {
        this.operations.save(ircLogServer);
        return ircLogServer;
    }

    @Override
    public List<IrcLogLine> findByIrcLogServerAndChannel(String server, String channel) {
        Query query = query(where("server").is(server).and("channel").is(channel));
        return this.operations.find(query, IrcLogLine.class);
    }
}

