package rezandry.wacloneapi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;  
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Message {

    @Id
    private String id;

    @Column(name = "from_number")
    private String fromNumber;

    private String content;

    @Column(name = "send_datetime")
    private Timestamp sendDatetime;

    @Column(name = "conversation_id")
    private String conversationId;
}
