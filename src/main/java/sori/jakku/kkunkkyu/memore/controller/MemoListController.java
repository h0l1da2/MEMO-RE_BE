package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MemoListController {

    private final WebService webService;
    private final MemoService memoService;

    @GetMapping("/main")
    public ResponseEntity<String> list(@RequestParam(required = false) String tag, @PageableDefault(size = 12) Pageable pageable, HttpServletRequest request) {

        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);
        List<MemoListDto> memoList = memoService.memoList(id, pageable, tag);

        String memoListJson = webService.objectToJson(memoList);
        jsonObject.addProperty("memoList", memoListJson);
        return webService.okResponse(jsonObject);
    }
}
