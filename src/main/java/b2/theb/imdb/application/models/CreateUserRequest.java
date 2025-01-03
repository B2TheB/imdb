package b2.theb.imdb.application.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {

    private String username;
    private String password;
    private String email;
}
