package in.dexc.UrlShortener.Controller;

import in.dexc.UrlShortener.Entity.Url;
import in.dexc.UrlShortener.Service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestBody Map<String, Object> requestBody) {
        String longUrl = (String) requestBody.get("longUrl");
        Optional<String> customShortUrl = Optional.ofNullable((String) requestBody.get("customShortUrl"));
        Optional<LocalDateTime> expiryDate = Optional.ofNullable(requestBody.containsKey("expiryDate")
                ? LocalDateTime.parse((String) requestBody.get("expiryDate"))
                : null);
        Url url = urlService.shortenUrl(longUrl, customShortUrl, expiryDate);
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String shortUrl) {
        Optional<Url> urlOpt = urlService.getOriginalUrl(shortUrl);
        if (urlOpt.isPresent()) {
            Url url = urlOpt.get();
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getLongUrl())).build();
        }
        return ResponseEntity.notFound().build();
    }
}
