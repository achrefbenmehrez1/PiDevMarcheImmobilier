package tn.esprit.realestate.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.TokenRepository;
import tn.esprit.realestate.Security.Token;
import tn.esprit.realestate.Security.TokenType;

@Service
public class CostumerOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private TokenRepository tokenRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return new CustomerOauth2User(oAuth2User);
    }
    public void saveUserToken(User user, String jwtToken) {
        System.out.println(jwtToken);
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
