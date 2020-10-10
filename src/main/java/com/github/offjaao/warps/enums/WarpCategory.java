package com.github.offjaao.warps.enums;

import lombok.Getter;

@Getter
public enum WarpCategory {

    PRIVATE(0),
    PUBLIC(1),
    SERVER(2);

    private final int id;

    WarpCategory(int id) {
        this.id = id;
    }

    public static WarpCategory byId(int id) {
        for (WarpCategory warpCategory : values()) {
            if (warpCategory.getId() == id) return warpCategory;
        }
        return null;
    }

}
