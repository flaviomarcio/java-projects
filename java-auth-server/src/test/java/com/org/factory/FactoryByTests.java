package com.org.factory;

import com.org.business.domain.AuthMode;
import com.org.business.domain.SetupMode;
import com.org.business.model.Scope;
import com.org.business.model.User;
import com.org.business.repository.*;
import com.org.business.service.RepositoryService;
import com.org.core.utils.HashUtil;
import com.org.core.utils.PasswordUtil;
import com.org.core.utils.Singletons;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Service
public class FactoryByTests {
    private static final int USER_COUNTER = 0;
    private static final String SCOPE_NAME = "service";
    private static Long DOCUMENT_SEED = 11122233344L;
    private static Long PHONE_SEED = 5511977886611L;
    private final List<Scope> scopeList = new ArrayList<>();
    private final List<User> userList = new ArrayList<>();
    private final List<User> userInvalidList = new ArrayList<>();
    private List<User> userSaved;

    private Environment mockEnvironment;
    private ScopeRepository scopeRepository;
    private UserRepository userRepository;
    private GrantCodeRepository grantCodeRepository;
    private TokenRepository tokenRepository;
    private DbTableRepository dbTableRepository;
    private RepositoryService repositoryService;
    private Singletons singletons;

    public FactoryByTests() {
        this.postConstruct();
    }

    @PostConstruct
    public void postConstruct() {
        setupMockClasses();
        mockEnvironment();
        setupMockServices();
        setupAuthServer();
        setupScope();
        setupUser();
    }

    private UUID makeUserId(UUID scopeId, String userName) {
        return HashUtil.toMd5Uuid("[%s].[%s]", scopeId, userName.toLowerCase());
    }


    private void setupMockClasses() {
        mockEnvironment = Mockito.mock(Environment.class);
        scopeRepository = Mockito.mock(ScopeRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        grantCodeRepository = Mockito.mock(GrantCodeRepository.class);
        tokenRepository = Mockito.mock(TokenRepository.class);
        dbTableRepository = Mockito.mock(DbTableRepository.class);
    }

    private void mockEnvironment() {
        var list = List.of(

                Map.of(
                        "little-code.setup.auto-start", false,
                        "little-code.database.auto-apply", false,
                        "little-code.database.ddl.auto-start", false,
                        "little-code.database.ddl.auto-save", false
                ),

                Map.of(
                        "service.debug", "false",
                        "service.context.path", "/api",
                        "service.config.path", "/tmp/auth-server",
                        "service.scope.setup", SetupMode.DISABLED.name(),
                        "service.scope.name", SCOPE_NAME,
                        "service.scope.admin.username", "admin",
                        "service.scope.admin.password", "admin"
                ),
                Map.of(
                        "service.token.secret", "",
                        "service.token.expires.days", "7",
                        "service.token.expires.minutes", "7",
                        "service.auth.mode", AuthMode.USERNAME.name()
                )
        );

        for (var envMap : list) {
            envMap.forEach((key, value) -> {
                Mockito.when(mockEnvironment.getProperty(key)).thenReturn(value.toString());
                Mockito.when(mockEnvironment.getProperty(key, "")).thenReturn(value.toString());
                Mockito.when(mockEnvironment.getProperty(key, value.toString())).thenReturn(value.toString());
            });
        }
    }

    private void setupMockServices() {
        repositoryService = new RepositoryService(scopeRepository, userRepository, grantCodeRepository, tokenRepository, dbTableRepository);
        singletons = Singletons.make(mockEnvironment, repositoryService);
    }

    private void setupAuthServer() {

    }

    private void setupScope() {
        scopeList.clear();
        scopeList.add(Scope
                .builder()
                .id(HashUtil.toMd5Uuid(SCOPE_NAME))
                .dt(LocalDateTime.now())
                .name(SCOPE_NAME)
                .enabled(true)
                .build()
        );
        for (var scope : scopeList) {
            Mockito.when(scopeRepository.existsById(scope.getId())).thenReturn(true);
            Mockito.when(scopeRepository.findById(scope.getId())).thenReturn(Optional.of(scope));
            Mockito.when(scopeRepository.save(scope)).thenReturn(scope);
        }
        Mockito.when(scopeRepository.findAll()).thenReturn(scopeList);
        Mockito.when(dbTableRepository.existsBySchemaNameAndTableName(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

    }

    private User createUser(Scope scope) {
        var userSeed = ++DOCUMENT_SEED;
        var userName = String.format("user-%d", userSeed);
        return User
                .builder()
                .id(makeUserId(scope.getId(), userName))
                .dt(LocalDateTime.now())
                .enabled(true)
                .scopeId(scope.getId())
                .username(userName)
                .name(userName)
                .document(userSeed.toString())
                .email(String.format("%s@admin.com", userName))
                .phoneNumber((++PHONE_SEED).toString())
                .dtBirth(LocalDate.MIN)
                .password(userName)
                .deleted(false)
                .build();
    }

    private void setupUser() {
        userList.clear();
        userSaved = null;
        scopeList.forEach(scope -> {
            for (int i = 1; i <= 10; i++) {
                userList.add(createUser(scope));
                userInvalidList.add(createUser(scope));
            }
        });
    }

    public void setupUserInject() {
        this.setupUser();
        userSaved = List.copyOf(userList);
        userList.clear();
        for (var user : userSaved) {
            userList.add(User.from(user));
            user.setPasswordCrypt(PasswordUtil.encrypt(user.getPassword()));
        }
        this.mockUsers(userSaved);
    }

    private void mockUsers(List<User> userList) {
        for (var user : userList) {
            Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
            Mockito.when(userRepository.existsByScopeIdAndId(user.getScopeId(), user.getId())).thenReturn(true);
            Mockito.when(userRepository.existsByScopeIdAndUsername(user.getScopeId(), user.getUsername())).thenReturn(true);
            Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            Mockito.when(userRepository.findByScopeIdAndDocument(user.getScopeId(), user.getDocument())).thenReturn(Optional.of(user));
            Mockito.when(userRepository.findByScopeIdAndEmail(user.getScopeId(), user.getEmail())).thenReturn(Optional.of(user));
            Mockito.when(userRepository.findByScopeIdAndId(user.getScopeId(), user.getId())).thenReturn(Optional.of(user));
            Mockito.when(userRepository.findByScopeIdAndPhoneNumber(user.getScopeId(), user.getPhoneNumber())).thenReturn(Optional.of(user));
            Mockito.when(userRepository.findByScopeIdAndUsername(user.getScopeId(), user.getUsername())).thenReturn(Optional.of(user));
            Mockito.when(userRepository.save(user)).thenReturn(user);
        }
    }

}
