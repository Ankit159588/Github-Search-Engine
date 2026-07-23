package com.grepo.grepobackend.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CodeSearchResultDto {
    public Long id;
    public String repo;
    public String file;
    public int line;
    public String type;
    public String name;
    public String signature;
}
