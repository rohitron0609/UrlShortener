package in.dexc.UrlShortener.Entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortUrl;

    @Column(nullable = false)
    private String longUrl;

    private LocalDateTime expiryDate;

    private Long usageCount;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    private Boolean isCustomUrl = false;
}