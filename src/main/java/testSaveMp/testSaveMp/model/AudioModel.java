package testSaveMp.testSaveMp.model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Entity
@Table(name = "audios")
public class AudioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String audioLink;
}
