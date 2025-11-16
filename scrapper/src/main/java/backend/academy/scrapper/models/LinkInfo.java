package backend.academy.scrapper.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "link_info")
@Getter
@Setter
public class LinkInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String link;

    @Column
    private String filters;

    @Column
    private String tegs;

    @ManyToMany(mappedBy = "links", fetch = FetchType.EAGER)
    private List<Users> users = new ArrayList<>();
}
