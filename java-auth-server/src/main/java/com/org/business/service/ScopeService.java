package com.org.business.service;

import com.org.business.dto.ScopeOut;
import com.org.business.model.Scope;
import com.org.business.repository.ScopeRepository;
import com.org.commons.config.SettingService;
import com.org.commons.utils.Singletons;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final Singletons singletons;

    private ScopeRepository getScopeRepository() {
        return singletons.getRepositoryService().getScopeRepository();
    }

    private UUID getScopeId() {
        return singletons.getSettingService().getScopeId();
    }

    public Scope getScopeSystem() {
        UUID scopeId = getScopeId();
        return scopeId == null
                ? null
                : getScopeRepository().findById(scopeId).orElse(null);
    }

    public ScopeOut save(Scope scope) {
        return ScopeOut.from(getScopeRepository().save(scope));
    }

    public Scope findById(UUID id) {
        return getScopeRepository().findById(id).orElse(null);
    }

    public List<ScopeOut> list() {
        try {
            List<ScopeOut> __return = new ArrayList<>();
            getScopeRepository()
                    .findAll()
                    .forEach(
                            scope ->
                                    __return.add(ScopeOut.from(scope))
                    );
            return __return;
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    public boolean isMounted() {
        var schemaName = SettingService.getSchemaName();
        final var tableName = "scope";
        if (!singletons.getRepositoryService().getDbTableRepository().existsBySchemaNameAndTableName(schemaName, tableName))
            return false;

        var scopeId = this.getScopeId();

        try {
            if (!singletons.getRepositoryService().getScopeRepository().existsById(scopeId))
                return false;
        } catch (Exception ignored) {
            return false;
        }

        return true;
    }

}
