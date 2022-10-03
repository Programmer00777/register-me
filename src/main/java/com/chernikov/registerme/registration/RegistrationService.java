package com.chernikov.registerme.registration;

import com.chernikov.registerme.appuser.User;
import com.chernikov.registerme.appuser.UserRole;
import com.chernikov.registerme.appuser.UserService;
import com.chernikov.registerme.email.EmailSender;
import com.chernikov.registerme.registration.token.ConfirmationToken;
import com.chernikov.registerme.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        String token = userService.signUpUser(
                new User(request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.USER_ROLE)
        );

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link));

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" >\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>Email Confirmation</title>\n" +
                "  <link rel='stylesheet' href='https://unpkg.com/tailwindcss@0.7.4/dist/tailwind.min.css'>\n" +
                "<link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.0.4/css/all.css'><link rel=\"stylesheet\" href=\"./style.css\">\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<!-- partial:index.partial.html -->\n" +
                "<div class=\"app font-sans min-w-screen min-h-screen bg-grey-lighter py-8 px-4\">\n" +
                "\n" +
                "  <div class=\"mail__wrapper max-w-md mx-auto\">\n" +
                "\n" +
                "    <div class=\"mail__content bg-white p-8 shadow-md\">\n" +
                "\n" +
                "      <div class=\"content__header text-center tracking-wide border-b\">\n" +
                "        <div class=\"text-red text-sm font-bold\">REGISTER.ME</div>\n" +
                "        <h1 class=\"text-3xl h-48 flex items-center justify-center\">Email Confirmation</h1>\n" +
                "      </div>\n" +
                "\n" +
                "      <div class=\"content__body py-8 border-b\">\n" +
                "        <p>\n" +
                "          Hey " + name + ", <br><br>It looks like you just signed up for REGISTER.ME, that’s awesome! Can we ask you for email confirmation? Just click the button bellow.\n" +
                "        </p>\n" +
                "          <a href=\"" + link + "\"><button class=\"text-white text-sm tracking-wide bg-red rounded w-full my-8 p-4 \">CONFIRM EMAIL ADRESS</button></a>\n" +
                "        <p class=\"text-sm\">\n" +
                "          Keep Rockin'!<br> Your Register.me team\n" +
                "        </p>\n" +
                "      </div>\n" +
                "\n" +
                "      <div class=\"content__footer mt-8 text-center text-grey-darker\">\n" +
                "        <h3 class=\"text-base sm:text-lg mb-4\">Thanks for using REGISTER.ME!</h3>\n" +
                "        <p>https://github.com/Programmer00777/register-me.git</p>\n" +
                "      </div>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"mail__meta text-center text-sm text-grey-darker mt-8\">\n" +
                "\n" +
                "      <div class=\"meta__social flex justify-center my-4\">\n" +
                "        <a href=\"#\" class=\"flex items-center justify-center mr-4 bg-black text-white rounded-full w-8 h-8 no-underline\"><i class=\"fab fa-facebook-f\"></i></a>\n" +
                "        <a href=\"#\" class=\"flex items-center justify-center mr-4 bg-black text-white rounded-full w-8 h-8 no-underline\"><i class=\"fab fa-instagram\"></i></a>\n" +
                "        <a href=\"#\" class=\"flex items-center justify-center bg-black text-white rounded-full w-8 h-8 no-underline\"><i class=\"fab fa-twitter\"></i></a>\n" +
                "      </div>\n" +
                "\n" +
                "      <div class=\"meta__help\">\n" +
                "        <p class=\"leading-loose\">\n" +
                "          Questions or concerns? <a href=\"#\" class=\"text-grey-darkest\">help@register.me</a>\n" +
                "\n" +
                "          <br> Want to quit getting updates? <a href=\"#\" class=\"text-grey-darkest\">Unsubscribe</a>\n" +
                "        </p>\n" +
                "      </div>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "  </div>\n" +
                "\n" +
                "</div>\n" +
                "<!-- partial -->\n" +
                "  \n" +
                "</body>\n" +
                "</html>";
    }
}
