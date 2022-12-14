package com.spring.service;

import com.spring.dao.BlogDao;
import org.springframework.stereotype.Service;

/**
 * BlogDaoServiceS
 *
 * @author liujunjie
 */
@Service

public class BlogDaoServiceS {

    private final BlogDao blogDao;

    public BlogDaoServiceS(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    //    @Transactional
    public int add() {

        for (int i = 0; i < 10; i++) {
            blogDao.getAllBlogIndexCount();
        }

        return 1;
    }

}
