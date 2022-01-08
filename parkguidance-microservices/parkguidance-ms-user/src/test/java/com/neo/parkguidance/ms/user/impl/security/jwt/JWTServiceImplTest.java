package com.neo.parkguidance.ms.user.impl.security.jwt;

import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPrivateKey;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPublicKey;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.api.security.jwt.KeyService;
import com.neo.parkguidance.ms.user.impl.entity.*;
import com.neo.parkguidance.ms.user.impl.security.TokenType;
import com.neo.parkguidance.ms.user.impl.utils.KeyPairUtils;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

class JWTServiceImplTest {

    private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCf0ggil0KtGRSNKb1r5XaM7dy3qeBNr/37kGUNAAGgnKiXxQdZUe1w4kLyjs/mLbY4noNxXQR6dZMoXpEnVbrHoN+KDIGiolfZ5hQke/qcXTcZ3ZInt0L/xyMPRn08NHswRTF+DODWMBazBWy7QR/5px/MANWkKf0MzOYyzAuuGepwUO5bfAXLMo6VjjTFtglk6fdpi5JzVm1G9N8vaDrugcHGZ3aLfTM4yeYXDScOow9R9qJi5csgfm6H0pb4WGbJnlnhDw8U51ZRzUQN/mUjWqHKnG47cN6Mz0cV3IBjpYkaNRsK/wJOzPtQR49c/mrKF2hQkGan8Rt5h5ZTW5l5AgMBAAECggEAJq+4uGbVBQGlL+aCq7OH1uVdIDhGy4dme5agX105B5/kT5iqBr0GNOUQP5bCTH2UNnBocPCLPPlo/cSmblcaN2iBs/EDEFMr4NsI1SwNpfwKcoBn+KUr2q5hljkB56jsiOq26ERFyJMra3pMr6c8DjqeP8uT64EeeBLBRlqOGT5ZhFbL/prNU8r11lh9QkEBXema4sv8Z0XohD+Rm7/Y/Y71QpfVz00I/P2JVFfs7JQm+Wylpdj3JXdV0ytfRRkvaA5CW4EdHQ1uegpxLIlHpIf1+TmWqCrK7SRlM5d0XjfJ+CkrkHEVbOYqJ/c3ENwh2g7Cp8hGdc9Y6KcjoeyIAQKBgQDtwCQyIw88glbA8jb0W5Q4/ld+KJ55FFrYNcU8qv7zQEHkiaXDrZLeWN2dtBDQN2dBIRZ91xTGayWevqE/O1vfkQ0yeY2WKmDEiMOMXYbJSf12In5as1L0g2A0gWGElYMH18p5TriIEC5tTG2X2MTElvI88THKnb1K2IYIQjuLoQKBgQCsFos6pgxJgDyZlzuRDl80ugzYU8rmtlgmAjWbnxxj/q2NLAVW1ZtyyudneAIgzi3vR2i+IbiJ1UIqoUhIXji+dGBx/n5dMJWd7dDc4saMhZYP4bOztpGJFfeeppulWPn3IEi/h+p3LcddYCJG6P+kCMqYnMXJYkg7po51x4l+2QKBgQCdMIY959QxC3PUSdBvqwTK0c6DCNQN9ZlCjfqD7AJ4TanDMga440/RVivgEqdzIs5Pz1KRjNyde0X6OMtfhR1vKPXyw08LU6V7C3lOmp99AkBkFNY5nYphQ2MeE9Sn1jo1u0SRA60ZuKkZdoiW9qjR2YL7K/Tho3z9uT6Uw0WfQQKBgGMtUuxoCijKGSLDf/bne4Mdvo1mLUetyzuMhmy71FbkzgzQU/tpe/86ZQbCqSdS7DI8y2bYdEwrdcivzsK2WqmY1ISeESGQAIpWJstGfvs5efSJNVHbZCDHYkAy8PzoPJdqKcMIh95LqdxsrRSE7+APmQM/RtX7KHoIUY4bg2hxAoGBAJNwZl6kES/laxNDykshL/14qNNEOdG9SF8nF9lyfycVjP+X/uF4BVBgacz2nmQ9d20waldHeebAXtflCfgqKu3obEGh2313EICtZAAOwTQYWx0fnOzdJkGv6CHy1hiVk7lGnHdakbeGrC6G43OpRc2+5vfx5/NAUdo0IrABX6kM";
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn9IIIpdCrRkUjSm9a+V2jO3ct6ngTa/9+5BlDQABoJyol8UHWVHtcOJC8o7P5i22OJ6DcV0EenWTKF6RJ1W6x6DfigyBoqJX2eYUJHv6nF03Gd2SJ7dC/8cjD0Z9PDR7MEUxfgzg1jAWswVsu0Ef+acfzADVpCn9DMzmMswLrhnqcFDuW3wFyzKOlY40xbYJZOn3aYuSc1ZtRvTfL2g67oHBxmd2i30zOMnmFw0nDqMPUfaiYuXLIH5uh9KW+FhmyZ5Z4Q8PFOdWUc1EDf5lI1qhypxuO3DejM9HFdyAY6WJGjUbCv8CTsz7UEePXP5qyhdoUJBmp/EbeYeWU1uZeQIDAQAB";

    private static JWTPrivateKey jwtPrivateKey;
    private static JWTPublicKey jwtPublicKey;
    private static JwtParser parser;

    JWTServiceImpl subject;

    KeyService keyService;

    EntityDao<UserToken> userTokenDao;

    @BeforeAll
    static void initKeys() {
        KeyPair keyPair = new KeyPair();
        keyPair.setPublicKey(PUBLIC_KEY);
        keyPair.setPrivateKey(PRIVATE_KEY);
        jwtPrivateKey = new JWTPrivateKey("0",KeyPairUtils.getPrivateKey(keyPair),null);
        jwtPublicKey = new JWTPublicKey("0", KeyPairUtils.getPublicKey(keyPair), null);

        parser = Jwts.parserBuilder().setSigningKey(jwtPublicKey.getKey()).build();
    }

    @BeforeEach
    public void init() {
        subject = Mockito.spy(JWTServiceImpl.class);

        keyService = Mockito.mock(KeyService.class);
        subject.keyService = keyService;

        Mockito.doReturn(jwtPrivateKey).when(keyService).getCurrentPrivateKey();
        List<JWTPublicKey> jwtPublicKeyList = new ArrayList<>();
        jwtPublicKeyList.add(jwtPublicKey);
        Mockito.doReturn(jwtPublicKeyList).when(keyService).getActivePublicKeys();

        userTokenDao = Mockito.mock(EntityDao.class);
        subject.userTokenDao = userTokenDao;
    }

    @Test
    void generateJWTResponseRestrictedTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("TestUser");
        registeredUser.getPermissions().add(new Permission("ADMIN"));
        //Act

        String jwtToken = subject.generateJWTResponse(registeredUser,false,true).getString("jwt");
        Jws<Claims> jws = parser.parseClaimsJws(jwtToken);
        Header header = jws.getHeader();
        Claims body = jws.getBody();

        //Assert
        Assertions.assertEquals(jwtPrivateKey.getId(), header.get("kid"));
        Assertions.assertNotNull(body.getExpiration());
        Assertions.assertEquals(Collections.emptyList() ,body.get("roles", List.class));
        Assertions.assertEquals(registeredUser.getUsername(), body.get("username", String.class));
    }

    @Test
    void generateJWTResponseTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("TestUser");
        registeredUser.getPermissions().add(new Permission("ADMIN"));
        //Act

        String jwtToken = subject.generateJWTResponse(registeredUser,false,false).getString("jwt");
        Jws<Claims> jws = parser.parseClaimsJws(jwtToken);
        Header header = jws.getHeader();
        Claims body = jws.getBody();

        //Assert
        Assertions.assertEquals(jwtPrivateKey.getId(), header.get("kid"));
        Assertions.assertNotNull(body.getExpiration());
        Assertions.assertEquals(Arrays.asList("ADMIN") ,body.get("roles", List.class));
        Assertions.assertEquals(registeredUser.getUsername(), body.get("username", String.class));
    }

    @Test
    void generateJWTResponseAndTokenTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("TestUser");
        registeredUser.getPermissions().add(new Permission("ADMIN"));
        //Act

        subject.generateJWTResponse(registeredUser,true,false).getString("jwt");

        //Assert
        Mockito.verify(subject).createToken(registeredUser,false);
    }

    @Test
    void generateJWTResponseAndTokenRestrictedTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("TestUser");
        registeredUser.getPermissions().add(new Permission("ADMIN"));
        //Act

        subject.generateJWTResponse(registeredUser,true,true).getString("jwt");

        //Assert
        Mockito.verify(subject).createToken(registeredUser,true);
    }

    @Test
    void createTokenRestrictedTest() {
        //Act
        UserToken userToken = subject.createToken(null, true);
        //Assert

        Mockito.verify(userTokenDao).create(userToken);
        Assertions.assertEquals(TokenType.PARTIAL, userToken.getType());
    }

    @Test
    void createTokenTest() {
        //Act

        UserToken userToken = subject.createToken(null, false);
        //Assert

        Mockito.verify(userTokenDao).create(userToken);
        Assertions.assertEquals(TokenType.REFRESH, userToken.getType());
    }

    @Test
    void getPermissionsTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        Permission admin = new Permission("ADMIN");
        Permission user = new Permission("USER");
        Permission premium = new Permission("PREMIUM");
        Role role = new Role();
        role.getPermissions().add(admin);
        role.getPermissions().add(premium);
        registeredUser.getPermissions().add(admin);
        registeredUser.getPermissions().add(user);
        registeredUser.getRoles().add(role);

        Set<String> expected = new HashSet<>(Arrays.asList("ADMIN", "USER", "PREMIUM"));
        //Act

        Set<String> result = subject.getPermissions(registeredUser);
        //Assert

        Assertions.assertEquals(expected,result);
    }
}
