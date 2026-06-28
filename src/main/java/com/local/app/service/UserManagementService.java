package com.local.app.service;

import com.local.app.dto.request.UserManagementRequest;
import com.local.app.dto.response.AuditLogResponse;
import com.local.app.dto.response.UserManagementResponse;
import com.local.app.exception.UserAlreadyExistsException;
import com.local.app.exception.UserNotFoundException;
import com.local.app.model.AuditLogModel;
import com.local.app.model.UserManagementModel;
import com.local.app.repository.AuditLogRepository;
import com.local.app.repository.UserManagementRepository;
import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class that handles business logic for user management operations.
 * This implementation is integrated with a centralized audit logging system to ensure
 * traceability of all modifications made to user records.
 */
@Singleton
public class UserManagementService {

    private final UserManagementRepository repository;
    private final AuditLogRepository logRepository;

    /**
     * Constructs the service with necessary repositories for data persistence and audit logging.
     *
     * @param repository    The primary repository for user entity operations.
     * @param logRepository The dedicated repository for centralized audit trail storage.
     */
    public UserManagementService(UserManagementRepository repository, AuditLogRepository logRepository) {
        this.repository = repository;
        this.logRepository = logRepository;
    }

    /**
     * Searches for a user based on multiple identity criteria.
     *
     * @return The found user DTO.
     * @throws UserNotFoundException if no criteria matches an existing user.
     */
    public UserManagementResponse searchUser(String companyEmail, String personalEmail, String login, String companyPhone, String personalPhone) {
        return repository.findByCompanyEmail(companyEmail)
                .or(() -> repository.findByPersonalEmail(personalEmail))
                .or(() -> repository.findByLogin(login))
                .or(() -> repository.findByCompanyPhone(companyPhone))
                .or(() -> repository.findByPersonalPhone(personalPhone))
                .map(this::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException("No user found with the provided criteria."));
    }

    /**
     * Persists a new user record and initializes the audit trail by saving a creation log.
     *
     * @param request Data containing user profile information.
     * @return The created user DTO.
     */
    public UserManagementResponse createUser(UserManagementRequest request) {
        validateConstraints(request, null);
        LocalDateTime now = LocalDateTime.now();
        String userHash = generateUserHash();

        UserManagementModel model = new UserManagementModel(
                userHash, request.name(), request.login(), request.companyPhone(),
                request.personalPhone(), request.companyEmail(), request.personalEmail(),
                request.birthDate(), "TL-" + System.currentTimeMillis(),
                request.department(), request.company(), true, request.type(),
                now, now
        );

        // Records the initial 'create' event in the central audit collection
        logRepository.save(new AuditLogModel(
                generateLogHash(), userHash, "User created", Collections.emptyMap(), now, "System"
        ));

        return mapToResponse(repository.save(model));
    }

    /**
     * Retrieves a user by their unique hash.
     */
    public UserManagementResponse findByHash(String userHash) {
        return repository.findByUserHash(userHash)
                .map(this::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with hash: " + userHash));
    }

    /**
     * Fetches all registered users.
     */
    public List<UserManagementResponse> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all historical audit logs for a given user entity hash.
     *
     * @param userHash The entity identifier to filter logs.
     * @return A list of structured audit log responses.
     */
    public List<AuditLogResponse> findLogsByEntityHash(String userHash) {
        return logRepository.findByEntityHash(userHash).stream()
                .map(log -> new AuditLogResponse(
                        log.logHash(), log.entityHash(), log.action(),
                        log.changes(), log.timestamp(), log.executedBy()))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing user and captures granular changes (from/to values) for auditing.
     *
     * @param userHash Identifier of the user to be updated.
     * @param request  Data with new values.
     * @param executor Identifier of the person/process performing the update.
     */
    public UserManagementResponse updateUser(String userHash, UserManagementRequest request, String executor) {
        return repository.findByUserHash(userHash).map(existing -> {
            validateConstraints(request, existing.userHash());

            // Calculates field-level deltas for the audit log
            Map<String, AuditLogModel.ChangeDetail> changes = calculateChanges(existing, request);

            // Persists changes to the audit collection if any field was modified
            if (!changes.isEmpty()) {
                logRepository.save(new AuditLogModel(
                        generateLogHash(), userHash, "Update user details", changes, LocalDateTime.now(), executor
                ));
            }

            Boolean updatedStatus = request.active() != null ? request.active() : existing.active();

            UserManagementModel updated = new UserManagementModel(
                    existing.userHash(),
                    request.name() != null ? request.name() : existing.name(),
                    request.login() != null ? request.login() : existing.login(),
                    request.companyPhone() != null ? request.companyPhone() : existing.companyPhone(),
                    request.personalPhone() != null ? request.personalPhone() : existing.personalPhone(),
                    request.companyEmail() != null ? request.companyEmail() : existing.companyEmail(),
                    request.personalEmail() != null ? request.personalEmail() : existing.personalEmail(),
                    request.birthDate() != null ? request.birthDate() : existing.birthDate(),
                    existing.registration(),
                    request.department() != null ? request.department() : existing.department(),
                    request.company() != null ? request.company() : existing.company(),
                    updatedStatus,
                    request.type() != null ? request.type() : existing.type(),
                    existing.createdAt(),
                    LocalDateTime.now()
            );

            return mapToResponse(repository.update(updated));
        }).orElseThrow(() -> new UserNotFoundException("User not found: " + userHash));
    }

    /**
     * Compares the current model state with the incoming request to detect specific changes.
     * Generates a ChangeDetail object for each modified field.
     */
    private Map<String, AuditLogModel.ChangeDetail> calculateChanges(UserManagementModel existing, UserManagementRequest request) {
        Map<String, AuditLogModel.ChangeDetail> changes = new HashMap<>();

        if (request.name() != null && !request.name().equals(existing.name()))
            changes.put("name", new AuditLogModel.ChangeDetail(existing.name(), request.name()));
        if (request.login() != null && !request.login().equals(existing.login()))
            changes.put("login", new AuditLogModel.ChangeDetail(existing.login(), request.login()));
        if (request.companyPhone() != null && !request.companyPhone().equals(existing.companyPhone()))
            changes.put("companyPhone", new AuditLogModel.ChangeDetail(existing.companyPhone(), request.companyPhone()));
        if (request.active() != null && request.active() != existing.active())
            changes.put("active", new AuditLogModel.ChangeDetail(String.valueOf(existing.active()), String.valueOf(request.active())));

        return changes;
    }

    public void deleteUser(String userHash) {
        if (repository.findByUserHash(userHash).isEmpty()) {
            throw new UserNotFoundException("User not found with hash: " + userHash);
        }
        repository.deleteByUserHash(userHash);
    }

    private void validateConstraints(UserManagementRequest request, String currentHash) {
        if (request.companyEmail() != null) {
            repository.findByCompanyEmail(request.companyEmail()).ifPresent(user -> {
                if (currentHash == null || !user.userHash().equals(currentHash)) {
                    throw new UserAlreadyExistsException("A user with the company email " + request.companyEmail() + " already exists.");
                }
            });
        }
        if (request.login() != null) {
            repository.findByLogin(request.login()).ifPresent(user -> {
                if (currentHash == null || !user.userHash().equals(currentHash)) {
                    throw new UserAlreadyExistsException("A user with the login " + request.login() + " already exists.");
                }
            });
        }
    }

    private UserManagementResponse mapToResponse(UserManagementModel model) {
        return new UserManagementResponse(
                model.userHash(), model.name(), model.login(), model.companyPhone(),
                model.personalPhone(), model.companyEmail(), model.personalEmail(),
                model.birthDate(), model.registration(), model.department(),
                model.company(), model.active(), model.type(),
                model.createdAt(), model.updatedAt()
        );
    }

    private String generateUserHash() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart1 = Integer.toHexString(new Random().nextInt(0x10000)).toUpperCase();
        String randomPart2 = Integer.toHexString(new Random().nextInt(0x10000)).toUpperCase();
        return String.format("USR-%s-%s-%s", datePart, randomPart1, randomPart2);
    }

    private String generateLogHash() {
        return "LOG-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}