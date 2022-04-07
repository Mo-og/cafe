package ua.cafe.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Pattern(regexp = "[ A-Za-zА-Яа-яЁё-]{2,50}",
            message = "Фамилия должна содержать только буквы (латинские или русские) и быть в пределах 2-50 символов.")
    private String lastName;
    @Pattern(regexp = "[ A-Za-zА-Яа-яЁё-]{2,50}",
            message = "Имя должно содержать только буквы (латинские или русские) и быть в пределах 2-50 символов.")
    private String firstName;
    @Pattern(regexp = "[ A-Za-zА-Яа-яЁё-]{2,50}",
            message = "Отчество должно содержать только буквы (латинские или русские) и быть в пределах 2-50 символов.")
    private String patronymic;
    @Email(message = "Адрес электронной почты должен быть действительным")
    private String email;
    private String address;
    @Pattern(regexp = "(\\+)?\\d{12}",
            message = "Номер телефона должен состоять из 12 цифр, включая код страны")
    private String username;
    //200 символов потому что при хеш кодировании Б криптом кол-во символов увеличиывается и пароль в 10 символов = 60 символам
    @Size(max = 200, min = 6,
            message = "Пароль должен быть в пределах 6-50 символов!")
    private String password = "";
    private Authority position;


    public User(String lastName, String firstName, String patronymic, String email, String username, String address, Authority position, String password) {
        if (username.contains("+"))
            username = username.replaceAll("[\\D]", "");
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.username = username;
        this.address = address;
        this.position = position;
//        authorities.add(position);
        this.password = password;
    }

    public User() {
    }


    public String toStringShort() {
        return "{(" + id +
                ") " + lastName + ' ' + firstName + ' ' + patronymic +
                " - " + position.getAuthority() + '\'' + '}';
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", position='" + position + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        if (id != 0)
            return id == user.id;
        else return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, patronymic, email, address, username, password, position);
    }

    @Override
    public List<Authority> getAuthorities() {
        ArrayList<Authority> authorities = new ArrayList<>();
        authorities.add(position);
        return authorities;
    }

    //UserDetails
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
