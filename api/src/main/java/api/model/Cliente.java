package api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "cliente")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 150, nullable = false)
    private String nome;

    @Column(unique = true, length = 11, nullable = false)
    private String cpf;

    @Column(length = 20)
    private String telefone;

    @Column(length = 250)
    private String endereco;
}