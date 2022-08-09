package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.entity.security.Page;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Role;
import com.springsecurity.rbac.springsecurityrbac.repository.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageService {
    private Logger logger = LoggerFactory.getLogger(PageService.class);
    private PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public Page findByName(String name) {
        Optional<Page> page = pageRepository.findByName(name);
        if (page.isEmpty()) {
            logger.info("Page with name {} not found!", name);
            return null;
        }
        return page.get();
    }

    public Page save(Page page) {
        return pageRepository.save(page);
    }
}
