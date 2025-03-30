package backend.academy.scrapper.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "link_info")
@Getter
@Setter
public class LinkInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String link;

    @Column
    private String filters;

    @Column
    private String tegs;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private Users user;
}
