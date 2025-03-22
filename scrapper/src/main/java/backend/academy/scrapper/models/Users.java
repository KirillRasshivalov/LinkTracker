package backend.academy.scrapper.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "active_users")
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "used_id")
    private Long userId;

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL)
    private List<LinkInfo> links = new ArrayList<>();
}
