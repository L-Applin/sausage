package help.sausage.exceptions;

import help.sausage.dto.NewUserDto;

public class UsernameAlreadyExistException extends RuntimeException {

    private NewUserDto userDto;

    public UsernameAlreadyExistException(String message, NewUserDto userDto) {
        super(message);
        this.userDto = userDto;
    }

    public UsernameAlreadyExistException(NewUserDto userDto) {
        this("Username '%s' already exist".formatted(userDto.username()), userDto);
    }
}
