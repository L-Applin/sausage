package help.sausage.validation;

import help.sausage.dto.NewUserDto;

public class UsernameAlreadyExistException extends RuntimeException {

    private NewUserDto userDto;

    public UsernameAlreadyExistException(String message, NewUserDto userDto) {
        super(message);
        this.userDto = userDto;
    }

    public UsernameAlreadyExistException(NewUserDto userDto) {
        this.userDto = userDto;
    }
}
