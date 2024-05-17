package rezandry.wacloneapi.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import rezandry.wacloneapi.entity.User;
import rezandry.wacloneapi.model.LoginUserRequest;
import rezandry.wacloneapi.model.TokenResponse;
import rezandry.wacloneapi.repository.UserRepository;
import rezandry.wacloneapi.security.BCrypt;

@Service
@Slf4j
public class AuthService {

    @Autowired
    UserRepository userRepository;

    public TokenResponse login(LoginUserRequest request) {
        if (!userRepository.existsById(request.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number not registered");
        }

        User user = userRepository.findById(request.getPhoneNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(getMilisNext30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

    }

    public void logout()

    private Long getMilisNext30Days() {
        return System.currentTimeMillis() + 60 * 60 * 24 * 30;
    }

}
