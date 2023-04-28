package example.end_course.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import example.end_course.Enum.RoleCustom;
import example.end_course.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nickName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleCustom roleCustom;
    @Column(name = "role_id", insertable = false, updatable = false)
    private int role_id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    @JsonManagedReference
    private List<Register> registers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    @JsonManagedReference
    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    @JsonManagedReference
    private List<Token> tokens;

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleCustom.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
