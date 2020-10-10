package com.github.offjaao.warps.loader;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Builder
public class ConfigurationLoader {

    //SQL
    public final String SQL_HOST, SQL_USERNAME, SQL_PASSWORD, SQL_DATABASE;
    public final int SQL_PORT;

    //MESSAGES
    public final String CREATED;
    public final String NO_PERMISSION;
    public final String ALREADY_EXISTS;
    public final String UPDATED_ICON;
    public final String CANCELED;
    public final String DELETED_WARP;
    public final String PASSWORD;
    public final String TYPE_PASSWORD;
    public final String SET_PASSWORD;
    public final String SET_LORE;
    public final String SET_NAME;
    public final String SET_MATERIAL;

}