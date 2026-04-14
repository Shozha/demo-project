package com.technokratos.agona.exception.registration;

public class NicknameAlreadyExistException extends RegistrationConflictException {

    public NicknameAlreadyExistException(String nickname) {
        super("User with nickname %s already exists".formatted(nickname), "nickname_already_exists");
    }
}
