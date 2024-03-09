package com.org.business.service;

import com.org.business.domain.AuthMode;
import com.org.business.dto.SignupIn;
import com.org.business.dto.UserOut;
import com.org.business.exceptions.*;
import com.org.business.model.User;
import com.org.business.repository.UserRepository;
import com.org.core.helper.MsgHelper;
import com.org.core.utils.HashUtil;
import com.org.core.utils.PasswordUtil;
import com.org.core.utils.Singletons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final Singletons singletons;

    private UUID getScopeId() {
        return singletons.getScopeSystem();
    }

    private UserRepository userRepository() {
        return singletons.getRepositoryService().getUserRepository();
    }

    public UUID makeUserId(String userKey) {
        var userId = HashUtil.toUuid(userKey);
        if (userId != null)
            return userId;
        var bytes = String.format("[%s].[%s]", getScopeId(), userKey.toLowerCase());
        return HashUtil.toMd5Uuid(bytes);
    }

    public UserOut register(SignupIn signupIn) {
        if (signupIn == null)
            throw InvalidObjectException.e(MsgHelper.OBJECT_INVALID, SignupIn.class);

        if (signupIn.getUsername() == null || signupIn.getUsername().isEmpty())
            throw UserInvalidException.e(MsgHelper.OBJECT_INVALID, SignupIn.class);

        if (signupIn.getPassword() == null || signupIn.getPassword().isEmpty())
            throw UserInvalidException.e(MsgHelper.OBJECT_INVALID, SignupIn.class);

        var userId = this.makeUserId(signupIn.getUsername());

        if (this.existsUser(userId))
            throw AuthGenericException.e(MsgHelper.o(MsgHelper.USER_EXISTS, userId));

        return UserOut.from(this.createUser(User.from(signupIn)));
    }

    public User createUser(User user) {
        if (user == null)
            throw InvalidObjectException.e(MsgHelper.o(MsgHelper.OBJECT_INVALID, User.class));

        if (String.valueOf(user.getUsername()).isEmpty())
            throw InvalidInformationException.e(MsgHelper.INVALID_ATTRIBUTE, "username");

        if (String.valueOf(user.getDocument()).isEmpty())
            throw InvalidInformationException.e(MsgHelper.INVALID_ATTRIBUTE, "document");

        if (String.valueOf(user.getEmail()).isEmpty())
            throw InvalidInformationException.e(MsgHelper.INVALID_ATTRIBUTE, "e-mail");

        if (String.valueOf(user.getPhoneNumber()).isEmpty())
            throw InvalidInformationException.e(MsgHelper.INVALID_ATTRIBUTE, "phone number");

        user.setScopeId(getScopeId());
        user.setEnabled(true);
        user.setDeleted(false);
        return updateUser(encryptPassword(user));
    }

    public User encryptPassword(User user) {
        user.setPasswordCrypt(PasswordUtil.encrypt(user.getPassword()));
        return user;
    }


    public User updateUser(User user) {
        if (user == null)
            throw InvalidObjectException.e(MsgHelper.o(MsgHelper.OBJECT_INVALID, User.class));
        user.setUsername(user.getUsername().toLowerCase());
        user.setDocument(user.getDocument().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        user.setPhoneNumber(user.getPhoneNumber().toLowerCase());
        user.setScopeId(getScopeId());

        user.setId(this.makeUserId(user.getUsername()));
        if (user.getDt() == null)
            user.setDt(LocalDateTime.now());
        if (user.getName() == null || user.getName().trim().isEmpty())
            user.setName(user.getUsername());

        userRepository().save(user);
        return user;
    }

    public User deleteUser(UUID userId) {
        return this.deleteUser(String.valueOf(userId));
    }

    public User deleteUser(String userKey) {
        var user = findUser(userKey);
        if (user == null)
            throw UserNotFoundException.e(MsgHelper.USER_IS_NOT_IN_THIS_SCOPE);
        user.setEnabled(false);
        user.setDeleted(false);
        return userRepository().save(user);
    }

    public User findUser(AuthMode authMode, String userKey) {
        User user = null;
        switch (authMode) {
            case UNKNOWN:
                var userId = makeUserId(userKey);
                user = userRepository().findByScopeIdAndId(getScopeId(), userId).orElse(null);
                break;
            case USERID:
                user = userRepository().findByScopeIdAndId(getScopeId(), HashUtil.toUuid(userKey)).orElse(null);
                break;
            case USERNAME:
                user = userRepository().findByScopeIdAndUsername(getScopeId(), userKey.toLowerCase().trim()).orElse(null);
                break;
            case DOCUMENT:
                user = userRepository().findByScopeIdAndDocument(getScopeId(), userKey.toLowerCase().trim()).orElse(null);
                break;
            case EMAIL:
                user = userRepository().findByScopeIdAndEmail(getScopeId(), userKey.toLowerCase().trim()).orElse(null);
                break;
            case PHONE_NUMBER:
                user = userRepository().findByScopeIdAndPhoneNumber(getScopeId(), userKey.toLowerCase().trim()).orElse(null);
                break;
            default:
                break;
        }
        if (user == null)
            throw UserNotFoundException.e(MsgHelper.USER_IS_NOT_IN_THIS_SCOPE);
        if (String.valueOf(user.getPasswordCrypt()).isEmpty())
            return encryptPassword(user);
        return user;
    }

    public User findUser(UUID userId) {
        return findUser(AuthMode.USERID, userId.toString());
    }

    public User findUser(String userKey) {
        return findUser(AuthMode.UNKNOWN, userKey);
    }

    public boolean existsUsers() {
        return userRepository().existsByScopeId(getScopeId());
    }

    public boolean existsUser(UUID userId) {
        return this.existsUser(String.valueOf(userId));
    }

    public boolean existsUser(String userKey) {
        var userId = makeUserId(userKey);
        return userRepository().existsByScopeIdAndId(getScopeId(), userId);
    }

}
