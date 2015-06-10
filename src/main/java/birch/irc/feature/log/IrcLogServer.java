package birch.irc.feature.log;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class IrcLogServer {

    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany
    private List<IrcLogLine> log;

    @Column
    private String server;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IrcLogLine> getLog() {
        return log;
    }

    public void setLog(List<IrcLogLine> log) {
        this.log = log;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "IrcLogServer [id=" + id + ", log=" + log + ", server=" + server
                + "]";
    }
}
