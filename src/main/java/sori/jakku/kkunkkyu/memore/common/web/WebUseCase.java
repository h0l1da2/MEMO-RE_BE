package sori.jakku.kkunkkyu.memore.common.web;

import jakarta.servlet.http.HttpServletRequest;

public interface WebUseCase {
    Long getIdInHeader(HttpServletRequest request);
}
