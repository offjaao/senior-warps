package com.github.offjaao.warps.loader;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
public class ConfigurationLoader {

    //SQL
    public final String SQL_HOST, SQL_USERNAME, SQL_PASSWORD, SQL_DATABASE;
    public final int SQL_PORT;

}