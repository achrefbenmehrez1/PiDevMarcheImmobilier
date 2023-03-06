package tn.esprit.realestate.Services.User;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.UserRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
private final UserService userService;
    public StripeService(UserService userService,
                         UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @Value("sk_test_51MhZwlHuC3uyR4hUqlYfHDzNoVmGtt7yhGIhBiphbAFo89rc94JWECHQbSDAWf3nKEExtEzf6nJ9s37zIvJobE3o00Qdt22rLg")
    private String stripeSecretKey;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }



    public void upgradeToPremium(@NonNull HttpServletRequest request) throws StripeException {
        User user = userService.getUserByToken(request);
        Token token = Token.create(Map.of(
                "card", Map.of(
                        "number","4242424242424242",
                        "exp_month","10",
                        "exp_year", 2028,
                        "cvc", "314"
                )
        ));
        // Create a customer
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", user.getEmail());
        customerParams.put("source", token.getId());
        Customer customer = Customer.create(customerParams);
        List<Object> items = new ArrayList<>();;
        Map<String, Object> item1 = new HashMap<>();
        item1.put(
                "price",
                "price_1MiK7kHuC3uyR4hUC1jzfC5P"
        );
        items.add(item1);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customer.getId());
        params.put("items", items);

        Subscription subscription =
                Subscription.create(params);
        // Update the user to Premium
        user.setPremium(true);
        userRepository.save(user);
    }
}
