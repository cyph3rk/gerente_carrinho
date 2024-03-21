package br.com.fiap.gerente_carrinho.security;

import br.com.fiap.gerente_carrinho.dominio.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null){
            var login = tokenService.validateToken(token);
            logger.info(" login: " + login);

            ResponseEntity<String> responseAPI;
            try {
                responseAPI = restTemplate.getForEntity(
                        "http://localhost:8082/users/filter/{login}",
                        String.class,
                        login
                );

                UserDetails user = modelMapper.map(responseAPI.getBody(), Usuario.class);

//                JsonNode userJson = objectMapper.readTree(responseAPI.getBody());
//                UserDetails user2 = (UserDetails) userJson.get(responseAPI.getBody());
//                logger.info("GET - user2: " + user2.getUsername() + " - " + user2.getPassword());


                logger.info("GET - responseAPI.getBody(): " + responseAPI.getBody());
                logger.info("GET - --->>> 1: " + user.toString());
                logger.info("GET - --->>> 2: " + user.toString());
                logger.info("GET - --->>> doFilterInternal: " + user.getUsername() + " - " + user.getPassword());

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (HttpClientErrorException e) {
                throw new NoSuchElementException();
            }


        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}

