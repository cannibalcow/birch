package birch.irc.feature.urlgrabber;

import java.util.Date;

public class Url {

    private String from;
    private String url;
    private String to;
    private Date date;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Url [from=" + from + ", url=" + url + ", to=" + to + ", date="
                + date + "]";
    }

}
