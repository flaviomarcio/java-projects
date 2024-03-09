package com.org.business.service;

import com.org.business.repository.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Getter
public class RepositoryService {
    private final ScopeRepository scopeRepository;
    private final UserRepository userRepository;
    private final GrantCodeRepository grantCodeRepository;
    private final TokenRepository tokenRepository;
    private final DbTableRepository dbTableRepository;
}
