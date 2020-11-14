package com.xiaott.blog.jobs;

import com.xiaott.blog.entity.Blog;
import com.xiaott.blog.mapper.BlogMapper;
import com.xiaott.blog.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SyncArticleViews {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BlogMapper blogMapper;

    @Scheduled(cron = "0 0/3 * * * ? ")//每5分钟
    public void SyncNodesAndShips() {
        logger.info("开始保存浏览数");
        try {
            //先获取这段时间的浏览数
            Map<Object,Object> viewCountItem=redisUtil.hmget("blog_view");
            //然后删除redis里这段时间的浏览数
            redisUtil.remove("blog_view");
            if(!viewCountItem.isEmpty()){
                for(Object item :viewCountItem.keySet()){
                    String articleKey=item.toString();//blog_1
                    Blog blog= (Blog) viewCountItem.get(articleKey);
                    //更新到数据库
                    blogMapper.update(blog);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("结束保存浏览数");
    }
}


