package sori.jakku.kkunkkyu.memore.tag.service;

import jakarta.servlet.http.HttpServletRequest;
import sori.jakku.kkunkkyu.memore.tag.dto.MainTagSaveDto;
import sori.jakku.kkunkkyu.memore.tag.dto.TagDto;

import java.util.List;

public interface TagUseCase {
    MainTagSaveDto writeForMain(HttpServletRequest request, MainTagSaveDto mainTagSaveDto);
    void writeTag(HttpServletRequest request, TagDto dto);
    void deleteTag(HttpServletRequest request, TagDto tagDto);
    List<String> tagList(HttpServletRequest request);
}
