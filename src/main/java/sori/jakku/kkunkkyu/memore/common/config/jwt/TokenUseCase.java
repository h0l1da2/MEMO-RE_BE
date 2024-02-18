package sori.jakku.kkunkkyu.memore.common.config.jwt;

public interface TokenUseCase {
    String creatToken(Long id, String username);
    String reCreateToken(String token);
    boolean tokenValid(String token);
    String usernameByToken(String token);
    Long getIdByToken(String token);
}
