package ch.eddjos.qualitool.jwt;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ch.eddjos.qualitool.auth.Benutzer;
import ch.eddjos.qualitool.auth.BenutzerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class JwtTokenUtil implements Serializable {

    private Map<String, Date> tokenMap;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${server.disable-no-request-timeout:false}")
    private boolean NO_REQUEST_TIMEOUT;

    @Autowired
    private BenutzerRepository benutzerRepository;

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 6 * 60 * 60;
    public static final long REQUEST_TIMEOUT = 5 * 60;
    @Value("${jwt.secret}")
    private String secret;

    public JwtTokenUtil() {
        tokenMap = Collections.synchronizedMap(new HashMap<String, Date>());
    }

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean isTimeoutReached(String token) {
        Date requestValidity = tokenMap.get(token);
        if(requestValidity == null) {
            return true;
        }
        return requestValidity.before(new Date());
    }

    public void revokeToken(String token) {
        tokenMap.remove(token);
    }


    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String token = doGenerateToken(claims, userDetails.getUsername());
        tokenMap.put(token, new Date(System.currentTimeMillis() + REQUEST_TIMEOUT * 1000));
        return token;
    }
    //while creating the token -
//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//2. Sign the JWT using the HS512 algorithm and secret key.
//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        logger.trace("Credentialstesting {}, {}, {}, {}",username.equals(userDetails.getUsername()) ,!isTokenExpired(token),!isTimeoutReached(token),NO_REQUEST_TIMEOUT);
        if(username.equals(userDetails.getUsername()) && !isTokenExpired(token) && (!isTimeoutReached(token) || NO_REQUEST_TIMEOUT) ) {
            tokenMap.put(token, new Date(System.currentTimeMillis() + REQUEST_TIMEOUT *1000));
            return true;
        } else {
            return false;
        }
    }

    public Benutzer getUser(){
        return benutzerRepository.findBenutzerByUsernameEquals(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
