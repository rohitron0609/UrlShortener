package in.dexc.UrlShortener.Service;

import in.dexc.UrlShortener.Entity.Url;
import in.dexc.UrlShortener.Repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }

    public Url shortenUrl(String longUrl, Optional<String> customShortUrl, Optional<LocalDateTime> expiryDate){
        Url url = new Url();
        url.setLongUrl(longUrl);
        url.setExpiryDate(expiryDate.orElse(null));
        url.setUsageCount(0L);

        StringBuilder shortUrl = new StringBuilder("http://");
        if(customShortUrl.isPresent()){
            shortUrl.append(customShortUrl.get()).append(".decx.in");
            url.setIsCustomUrl(true);
        }else{
            shortUrl.append(generateShortUrl()).append(".decx.in");
        }

        url.setShortUrl(shortUrl.toString());
        return urlRepository.save(url);
    }

    private String generateShortUrl() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    public Optional<Url> getOriginalUrl(String shortUrl){
        Optional<Url> urlOpt = urlRepository.findByShortUrl(shortUrl);
        if(urlOpt.isPresent() && (urlOpt.get().getExpiryDate() == null || urlOpt.get().getExpiryDate().isAfter(LocalDateTime.now()))){
            Url url = urlOpt.get();
            url.setUsageCount(url.getUsageCount()+1);
            urlRepository.save(url);
            return Optional.of(url);
        }
        return Optional.empty();
    }
}
