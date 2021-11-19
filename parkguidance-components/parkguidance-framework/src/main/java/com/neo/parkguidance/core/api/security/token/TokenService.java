package com.neo.parkguidance.core.api.security.token;

import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.security.token.TokenType;

import java.util.Date;

public interface TokenService {

    /**
     * Checks if the token exists and hasn't expired yet. Invalidates the token if it has expires
     *
     * @param token the token
     * @return the {@link UserToken} if the token is valid else null
     */
    UserToken validateToken(String token);

    /**
     * Invalidates the token
     *
     * @param clientId the id of the client
     * @param token the token
     */
    void inValidateToken(long clientId ,String token);

    /**
     * Invalidates the token
     *
     * @param user the user of the token
     * @param token the token
     */
    void inValidateToken(RegisteredUser user ,String token);

    /**
     * Creates a token attached to the user
     *
     * @param registeredUser the registered user token should be attached too
     * @param expirationDate the date the token expires
     * @param tokenType the type of token
     *
     * @return the created token
     */
    UserToken generateToken(RegisteredUser registeredUser, Date expirationDate, TokenType tokenType);

    /**
     * Creates a token attached to the user
     *
     * @param registeredUser the registered user token should be attached too
     * @param tokenType the type of token
     *
     * @return the created token
     */
    UserToken generateToken(RegisteredUser registeredUser, TokenType tokenType);

    /**
     * Creates a token attached to the user
     *
     * @param registeredUser the registered user token should be attached too
     * @param expirationDate the date the token expires
     * @param tokenType the type of token
     * @param description the description of the token
     *
     * @return the created token
     */
    UserToken generateToken(RegisteredUser registeredUser, Date expirationDate, TokenType tokenType, String description);

    /**
     * Creates a token attached to the user
     *
     * @param userId the user id
     * @param tokenType the type of token
     *
     * @return  created token or null when invalid id
     */
    UserToken generateToken(long userId, TokenType tokenType);

    /**
     * Creates a token attached to the user
     *
     * @param userId the user id
     * @param expirationDate the date the token expires
     * @param tokenType the type of token
     *
     * @return  created token or null when invalid id
     */
    UserToken generateToken(long userId, Date expirationDate, TokenType tokenType);

    /**
     * Creates a token attached to the user
     *
     * @param userId the user id
     * @param expirationDate the date the token expires
     * @param tokenType the type of token
     * @param description the description of the token
     *
     * @return the created token or null when invalid id
     */
    UserToken generateToken(long userId, Date expirationDate, TokenType tokenType, String description);
}
