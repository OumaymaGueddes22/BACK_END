package msg;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Message")
public class Message {

    @Id
    private String id;
    private String txt;
    private Date time ;
    private Boolean isdeleted;
}
