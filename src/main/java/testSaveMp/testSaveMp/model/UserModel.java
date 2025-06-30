package testSaveMp.testSaveMp.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Setter
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long userId;
}
