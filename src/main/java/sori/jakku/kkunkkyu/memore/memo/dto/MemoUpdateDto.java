package sori.jakku.kkunkkyu.memore.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.common.annotation.Memo;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoUpdateDto {

    @Memo
    private String originKey;
    @Memo
    private String newKey;
    private String content;
    private Map<String, Boolean> tag;

}
