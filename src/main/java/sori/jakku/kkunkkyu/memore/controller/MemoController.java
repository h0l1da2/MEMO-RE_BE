package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.domain.dto.Response;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final WebService webService;

    // TODO 메모 태그 리스트
    @GetMapping
    public ResponseEntity<String> list(@RequestParam(required = false) String tag, @PageableDefault(size = 12) Pageable pageable, HttpServletRequest request) {

        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);
        List<MemoListDto> memoList = memoService.memoList(id, pageable, tag);

        String memoListJson = webService.objectToJson(memoList);
        jsonObject.addProperty("memoList", memoListJson);
        return webService.okResponse(jsonObject);
    }

    // TODO 메모 태그 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteMemo(@RequestBody String keyword, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        try {
            String key = webService.jsonToString(keyword, "keyword");
            memoService.removeMemo(id, key);
        } catch (MemoNotFoundException e) {
            log.info("해당 메모가 없음");
            jsonObject.addProperty("response", Response.NOT_FOUND);
            return webService.badResponse(jsonObject);
        } catch (UserNotFoundException e) {
            log.info("본인 메모가 아님");
            jsonObject.addProperty("response", Response.BAD);
            return webService.badResponse(jsonObject);
        }

        return webService.okResponse(jsonObject);
    }

    @PutMapping
    public ResponseEntity<String> changeMemo(@RequestBody @Valid MemoUpdateDto memoUpdateDto, HttpServletRequest request) {
        /**
         * 원래 키워드 확인 후,
         * 있다면 전부 수정
         * 태그테이블도 삭제
         */
        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        try {
            memoService.changeMemo(id, memoUpdateDto);
        } catch (MemoNotFoundException e) {
            log.info("없는 키워드 = {}", memoUpdateDto.getOriginKey());
            jsonObject.addProperty("response", Response.NOT_FOUND);
            return webService.badResponse(jsonObject);
        } catch (UserNotFoundException e) {
            log.info("본인 메모가 아님 = {} {}", id, memoUpdateDto.getOriginKey());
            jsonObject.addProperty("response", Response.BAD);
            return webService.badResponse(jsonObject);
        } catch (DuplicateMemoException e) {
            log.info("중복 키워드 = {}", memoUpdateDto.getNewKey());
            jsonObject.addProperty("response", Response.DUPLICATE);
        }

        return webService.okResponse(jsonObject);
    }

    @PostMapping
    public ResponseEntity<String> write(@RequestBody @Valid MemoWriteDto memoWriteDto, HttpServletRequest request) {

        JsonObject jsonObject = new JsonObject();

        Long id = webService.getIdInHeader(request);
        try {
            memoService.write(id, memoWriteDto);
        } catch (DuplicateMemoException e) {
            log.info("키워드가 같음 = {}", memoWriteDto.getKeyword());
            jsonObject.addProperty("response", Response.DUPLICATE);
            return webService.badResponse(jsonObject);
        } catch (UserNotFoundException e) {
            log.info("유저를 찾을 수 없음 = {}", id);
            jsonObject.addProperty("response", Response.DUPLICATE);
            return webService.badResponse(jsonObject);
        }

        return webService.okResponse(jsonObject);
    }

}
