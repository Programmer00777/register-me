package com.chernikov.registerme.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationRestController {

    private final RegistrationService registrationService;

    @GetMapping(path = "/")
    public String registerResponse() {
        return "You've successfully signed in";
    }

    @PostMapping(path = "/")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
