package model;

import jakarta.persistence.*;
import lombok.*;

//É tipo, entiny, isso pertence a tabela Livro
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gera um valor aleatorio no id, um autoincrement
    private long id;
    private String titulo;

    @OneToMany
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Livro(String titulo, Autor autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

}
