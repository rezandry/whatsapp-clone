package rezandry.wacloneapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import rezandry.wacloneapi.model.LoginUserRequest;
import rezandry.wacloneapi.model.TokenResponse;
import rezandry.wacloneapi.model.WebResponse;
import rezandry.wacloneapi.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/api/v1/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse response = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(response).build();
    }

}
