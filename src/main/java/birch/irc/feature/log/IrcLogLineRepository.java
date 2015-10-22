package birch.irc.feature.log;


import java.util.List;

public interface IrcLogLineRepository {
    IrcLogLine save(IrcLogLine ircLogServer);
    List<IrcLogLine> findByIrcLogServerAndChannel(String server, String channel);
}
