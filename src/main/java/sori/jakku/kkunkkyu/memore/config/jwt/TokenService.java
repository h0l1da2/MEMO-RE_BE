package sori.jakku.kkunkkyu.memore.config.jwt;

public interface TokenService {
    String creatToken(Long id, String username);
    String reCreateToken(String token);
    boolean tokenValid(String token);
    String usernameByToken(String token);
    Long getIdByToken(String token);
}
