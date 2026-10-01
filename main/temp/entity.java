@Data
@Builder
public class TokenEntity {

    private Long id;
    private String tokenOwnerId;
    private String sourcePciToken;
    private String status;
    private Timestamp createdDate;
    // ... остальные колонки
}
