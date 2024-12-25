package com.datn.warehousemgmt.service.impl;

import com.datn.warehousemgmt.dto.IntrospectDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.UserDTO;
import com.datn.warehousemgmt.entities.Permission;
import com.datn.warehousemgmt.entities.Users;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.repository.UsersRepository;
import com.datn.warehousemgmt.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);;

    private final UsersRepository usersRepository;

    @Override
    public ServiceResponse login(UserDTO dto) {
        try{
            Optional<Users> user = usersRepository.findByUsernameAndStatus(dto.getUsername(), true);
            if(!user.isPresent()){
                throw new AppException(ErrorCode.INVALID_ACCOUNT);
            }
            if(!passwordEncoder.matches(dto.getPassword(), user.get().getPassword())){
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            var token = generateToken(user.get());

            return new ServiceResponse(token, "Đăng nhập thành công", 200);
        }catch (NoResultException e) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT);
        }catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình đăng nhập");
        }
    }

    @Override
    public ServiceResponse logout(UserDTO dto) {

        return null;
    }

    @Override
    public ServiceResponse changePassword() {
        return null;
    }

    private String generateToken(Users users) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(users.getId()))
                .issuer("DATN")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("id", users.getId())
                .claim("username", users.getUsername())
                .claim("role", getRole(users))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    public ServiceResponse introspect(IntrospectDTO request){
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }

        return new ServiceResponse(isValid, "Thành công", 200);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(30, ChronoUnit.MINUTES).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String getRole(Users users){
        return Optional.ofNullable(users.getPermissions())
                .filter(permissions -> !permissions.isEmpty())
                .map(permissions -> permissions.stream()
                        .map(Permission::getName)
                        .collect(Collectors.joining(" ")))
                .orElse("");
    }
}
