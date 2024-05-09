package rezandry.wacloneapi.service;

import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import rezandry.wacloneapi.entity.User;
import rezandry.wacloneapi.model.RegisterUserRequest;
import rezandry.wacloneapi.repository.UserRepository;
import rezandry.wacloneapi.security.BCrypt;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void register(RegisterUserRequest request) {
        if (userRepository.existsById(request.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
    }

}
