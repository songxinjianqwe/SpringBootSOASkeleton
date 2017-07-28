package cn.sinjinsong.skeleton.domain.dto.chat;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by SinjinSong on 2017/7/28.
 */
@Data
public class Greeting {
    @NotNull
    private String body;
}
