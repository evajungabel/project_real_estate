package com.realestate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentForm {
    @NotBlank
    private String agentName;
    @NotBlank
    private String userName;
    @NotBlank
    private String comment;
}
