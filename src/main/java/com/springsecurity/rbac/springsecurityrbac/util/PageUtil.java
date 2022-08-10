package com.springsecurity.rbac.springsecurityrbac.util;

import com.springsecurity.rbac.springsecurityrbac.dto.PageDto;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Page;

public class PageUtil {

    public static Page toPage(PageDto pageDto){
        return new Page(pageDto.getName());
    }
}
