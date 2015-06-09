package birch.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import birch.irc.feature.urlgrabber.Url;
import birch.irc.feature.urlgrabber.UrlRepository;

@RestController
public class UrlController {

    private UrlRepository repo;

    @Autowired
    public UrlController(UrlRepository repository) {
        this.repo = repository;
    }

    @RequestMapping("/url")
    public List<Url> getAllUrls() {
        return Lists.newArrayList(repo.findAll());
    }
    
    @RequestMapping("/url/{id}")
    public Url getUrlById(@PathVariable Long id) {
        return repo.findOne(id);
    }
    
    @RequestMapping("/url/nick/{nick}")
    public List<Url> getUrlsByNick(@PathVariable String nick) {
        return repo.findByNick(nick);
    }
    
    @RequestMapping("/url/channel/{channel}")
    public List<Url> getUrlsByChannel(@PathVariable() String channel) {
        return repo.findByChannel(channel);
    }
}
