package com.neo.parkguidance.ms.user.impl.security.jwt;

import com.neo.parkguidance.ms.security.impl.authentication.key.JWTKey;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.api.security.jwt.JWTGeneratorService;
import com.neo.parkguidance.ms.user.api.security.jwt.KeyService;
import com.neo.parkguidance.ms.user.impl.entity.Permission;
import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.impl.entity.Role;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.impl.security.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RequestScoped
public class JWTServiceImpl implements JWTGeneratorService {

    public static final long REFRESH_TOKEN_LIFESPAN = 1000L * 60L * 60L * 24L * 100L; // 100 Days
    public static final long JWT_LIFE_SPAN = 1000 * 60 * 5L; // 5 MIN

    @Inject
    KeyService keyService;

    @Inject
    EntityDao<UserToken> userTokenDao;

    @Override
    public JSONObject generateJWTResponse(RegisteredUser registeredUser, boolean generateToken,
            boolean restricted) {

        JSONObject jsonObject = new JSONObject();

        JWTKey jwtKey = keyService.getCurrentPrivateKey();

        String jwtToken = Jwts.builder().setHeaderParam("kid",jwtKey.getId())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_LIFE_SPAN))
                .setIssuedAt(new Date())
                .setSubject(registeredUser.getUsername())
                .claim("roles", restricted ? new String[] {} : getPermissions(registeredUser))
                .claim("username", registeredUser.getUsername())
                .signWith(jwtKey.getKey(), SignatureAlgorithm.RS256).compact();

        jsonObject.put("jwt", jwtToken);


        if (generateToken) {
            UserToken token = createToken(registeredUser, restricted);
            jsonObject.put("refreshToken", token.getKey());
        }
        return jsonObject;
    }

    protected UserToken createToken(RegisteredUser registeredUser, boolean restricted) {
        TokenType tokenType = TokenType.REFRESH;
        if (restricted) {
            tokenType = TokenType.PARTIAL;
        }

        UserToken userToken = new UserToken(
                "Refresh token",
                tokenType,
                new Date(System.currentTimeMillis() + REFRESH_TOKEN_LIFESPAN),
                registeredUser);
        userTokenDao.create(userToken);
        return userToken;
    }

    protected Set<String> getPermissions(RegisteredUser registeredUser) {
        Set<String> userPermissions = new HashSet<>();

        for (Permission perm: registeredUser.getPermissions()) {
            userPermissions.add(perm.getName());
        }

        for (Role role: registeredUser.getRoles()) {
            for (Permission perm: role.getPermissions()) {
                userPermissions.add(perm.getName());
            }
        }

        return userPermissions;
    }
}
