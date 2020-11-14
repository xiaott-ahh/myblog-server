package com.xiaott.blog.service;

import com.xiaott.blog.entity.Blog;
import com.xiaott.blog.entity.BlogIdAndTitle;
import com.xiaott.blog.mapper.BlogMapper;
import com.xiaott.blog.utils.CastUtil;
import com.xiaott.blog.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class BlogServiceImp implements BlogService{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<Blog> list() {
        try {
            return blogMapper.selectAll();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
    @Override
    public Blog getById(int id) {
        try {
            return blogMapper.selectById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    */
    @Override
    public Blog getById(int id) {
        try {
            String key = "blogs_" + id;
            Map<Object,Object> blogItems = redisUtil.hmget("blog_view");
            Blog blog;
            if (!blogItems.isEmpty()) {
                if (blogItems.containsKey(key)) {
                    //logger.info("从redis请求");
                    blog = (Blog) blogItems.get(key);
                }else {
                    //logger.info("redis不包含此键");
                    blog = blogMapper.selectById(id);
                    //System.out.printf("浏览量为%d",blog.getVisitedNum()+1);
                }
            }else {
                //logger.info("从数据库请求");
                blog = blogMapper.selectById(id);
                //System.out.printf("浏览量为%d",blog.getVisitedNum()+1);
            }
            blog.setVisitedNum(blog.getVisitedNum()+1);
            redisUtil.hset("blog_view",key,blog);
            return blog;
        }catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param startIndex 起始的下标
     * @return 以startIndex为始的10篇博客
     */
    @Override
    public List<Blog> getByPage(int startIndex) {
        try {
            String key = "startIndex_" + startIndex;
            if (redisUtil.hasKey(key)) {
                //logger.info("请求redis的博客列表");
                return CastUtil.object2List(redisUtil.get(key),Blog.class);
            } else {
                //logger.info("请求数据库的博客列表");
                List<Blog> res = blogMapper.selectFromStartIndex(startIndex);
                //200s失效
                redisUtil.set(key,res,200);
                return res;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return 返回总的博客数，使用缓存
     */
    @Override
    public Integer getNums() {
        String key = "blogNums";
        if (redisUtil.hasKey(key)) {
            return (Integer) redisUtil.get(key);
        } else {
            int num = blogMapper.selectNums();
            redisUtil.set(key,num,getRemainSecondsOneDay(new Date(),1));
            return num;
        }
    }
    /*
    @Override
    public List<Blog> getTopFive() {
        try {
            return blogMapper.selectAll()
                             .stream()
                             .sorted(Comparator.comparing(Blog::getCreatedAt).reversed())
                             .limit(5)
                             .collect(Collectors.toList());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    */

    /**
     *
     * @return 最新的五篇博客
     */
    @Override
    public List<BlogIdAndTitle> getTopFive() {
        //查询redis中是否存在键
        String key = "all_info";
        if (redisUtil.hasKey(key)) {
            //存在则返回
            return CastUtil.object2List(redisUtil.lGet(key,0,4),BlogIdAndTitle.class);
        }else {
            //不存在则查询数据库，并存入缓存
            List<BlogIdAndTitle> res = blogMapper.selectBlogIdAndTitle()
                                                 .stream().sorted(Comparator.comparing(BlogIdAndTitle::getCreatedAt).reversed())
                                                 .collect(Collectors.toList());
            //当天24点失效
            redisUtil.lSet(key,res,getRemainSecondsOneDay(new Date(),1));
            return res;
        }
    }

    @Override
    public List<BlogIdAndTitle> getAllBlogsInfo() {
        String key = "all_info";
        if (redisUtil.hasKey(key)) {
            return CastUtil.object2List(redisUtil.lGet(key,0,-1),BlogIdAndTitle.class);
        } else {
            List<BlogIdAndTitle> res = blogMapper.selectBlogIdAndTitle()
                                                 .stream().sorted(Comparator.comparing(BlogIdAndTitle::getCreatedAt).reversed())
                                                 .collect(Collectors.toList());
            //24点失效
            redisUtil.lSet(key,res,getRemainSecondsOneDay(new Date(),1));
            return res;
        }
    }

    @Override
    public List<Blog> getByKeywords(String keywords) {
        try {
            String key = "keywords_" + keywords;
            if (redisUtil.hasKey(key)) {
                return CastUtil.object2List(redisUtil.get(key),Blog.class);
            } else {
                List<Blog> res = blogMapper.selectByKeywords(keywords);
                redisUtil.set(key,res,200);
                return res;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Blog> getByTag(String tag) {
        try {
            String key = "tag_" + tag;
            if (redisUtil.hasKey(key)) {
                return CastUtil.object2List(redisUtil.get(key),Blog.class);
            }else {
                List<Blog> res = blogMapper.selectByTag(tag);
                redisUtil.set(key,res,200);
                return res;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(Blog blog) {
        try {
            blogMapper.insert(blog);
            //更新topFive
            redisUtil.remove("all_info");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("添加文章失败.");
        }
    }

    @Override
    public void update(Blog blog) {
        try {
            blogMapper.update(blog);
            //更新缓存
            String key = "blogs_" + blog.getId();
            redisUtil.hset("blog_view",key,blog);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新文章失败.");
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            blogMapper.deleteById(id);
            //删除缓存
            String key = "blogs_" + id;
            redisUtil.hdel("blog_view",key);
            redisUtil.remove("all_info");
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除文章失败.");
        }
    }

    /**
     *
     * @param currentDate 当前时间
     * @param days 几天后失效
     * @return 秒数
     */
    public static Long getRemainSecondsOneDay(Date currentDate,int days) {
        //使用plusDays加传入的时间加1天，将时分秒设置成0
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(days).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        //使用ChronoUnit.SECONDS.between方法，传入两个LocalDateTime对象即可得到相差的秒数
        return ChronoUnit.SECONDS.between(currentDateTime, midnight);
    }

}
