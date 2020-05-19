package ua.opu.kurs_gorbik_kozyrevych;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "workers")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Pattern(regexp = "[ A-Za-zА-Яа-яЁё]{2,50}", message = "Фамилия должна содержать только буквы (латинские или русские) и быть в пределах 2-50 символов.")
    private String lastName;
    @Pattern(regexp = "[ A-Za-zА-Яа-яЁё]{2,50}", message = "Имя должно содержать только буквы (латинские или русские) и быть в пределах 2-50 символов.")
    private String firstName;
    @Pattern(regexp = "[ A-Za-zА-Яа-яЁё]{2,50}", message = "Отчество должно содержать только буквы (латинские или русские) и быть в пределах 2-50 символов.")
    private String patronymic;
    @Email(message = "Адрес электронной почты должен быть действительным")
    private String email;
    @Pattern(regexp = "(\\+)?\\d{12}", message = "Номер телефона должен состоять из 12 цифр, включая код страны")
    private String username;
    private String address;
    private String position;
    //200 символов потому что при хеш кодировании Б криптом кол-во символов увеличиывается и пароль в 10 символов = 60 символам
    @Size(max = 200, min = 6, message = "Пароль должен быть в пределах 6-50 символов!")
    private String password = "";
    private String roles;


    public Worker(String lastName, String firstName, String patronymic, String email, String username, String address, String position, String password) {
        if (username.contains("+"))
            username.replace("+", "");
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.username = username;
        this.address = address;
        this.position = position;
        switch (position.toLowerCase()) {
            case "повар":
                roles = "ROLE_COOK";
                break;
            case "официант":
                roles = "ROLE_WAITER";
                break;
            case "директор":
                roles = "ROLE_ADMIN";
                break;
        }
        this.password = password;

    }

    public Worker() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        switch (position.toLowerCase()) {
            case "повар":
                roles = "ROLE_COOK";
                break;
            case "официант":
                roles = "ROLE_WAITER";
                break;
            case "директор":
                roles = "ROLE_ADMIN";
                break;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", position='" + position + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
