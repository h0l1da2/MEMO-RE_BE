package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.domain.dto.Response;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final WebService webService;

    @PostMapping
    public ResponseEntity<String> write(@RequestBody @Valid MemoWriteDto memoWriteDto, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();

        Long id = webService.getIdInHeader(request);
        try {
            memoService.write(id, memoWriteDto);
        } catch (DuplicateMemoException e) {
            jsonObject.addProperty("response", Response.DUPLICATE);
            return webService.badResponse(jsonObject);
        }

        return webService.okResponse(jsonObject);
    }

}
