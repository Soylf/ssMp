package testSaveMp.testSaveMp.model;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
}