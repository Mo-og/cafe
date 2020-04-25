package ua.opu.kurs_gorbik_kozyrevych;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EntranceForm {
    @Pattern(regexp = "(\\+)?\\d{12}", message = "Номер телефона должен состоять из 12 цифр, включая код страны")
    private String username;
    @Size(max=200, min=6, message = "Пароль должен быть в пределах 6-50 символов!")
    private String password;

    public EntranceForm() {}

    public EntranceForm(String username, String password) {
        if (username.contains("+"))
            username.replace("+","");
        this.username = username;
        this.password = password;
    }


    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
