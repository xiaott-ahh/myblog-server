package com.xiaott.blog.controller;

import com.xiaott.blog.entity.Blog;
import com.xiaott.blog.entity.BlogIdAndTitle;
import com.xiaott.blog.entity.User;
import com.xiaott.blog.result.Result;
import com.xiaott.blog.result.ResultFactory;
import com.xiaott.blog.service.BlogService;
import com.xiaott.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogServiceImp;

    @Autowired
    private UserService userService;

    @GetMapping("api/blogs")
    List<Blog> list() {
        System.out.println("请求加载所有博客.");
        return blogServiceImp.list();
    }

    @GetMapping("api/getAllInfo/blogs")
    List<BlogIdAndTitle> getInfos() {
        System.out.println("访问了归档");
        return blogServiceImp.getAllBlogsInfo();
    }

    @GetMapping("api/getblog/{id}")
    Blog getById(@PathVariable("id") int id) {
        System.out.printf("返回id=%d的博客.\n",id);
        return blogServiceImp.getById(id);
    }

    @GetMapping("api/blogs/{startIndex}")
    List<Blog> listFromStartIndex(@PathVariable("startIndex") int startIndex) {
        System.out.printf("返回起始下标为:%d的9篇博客",startIndex);
        return blogServiceImp.getByPage(startIndex);
    }

    @GetMapping("api/tag/blogs")
    List<Blog> listByTag(@RequestParam("tag") String tag) {
        System.out.printf("返回标签为:%s的博客",tag);
        return blogServiceImp.getByTag(tag);
    }

    @GetMapping("api/newest/blogs")
    List<BlogIdAndTitle> listByTime() {
        System.out.println("请求最新的五篇博客");
        return blogServiceImp.getTopFive();
    }

    @GetMapping("api/keywords/blogs")
    List<Blog> listByKeywords(@RequestParam("keywords") String keywords) {
        System.out.printf("请求关键词为：%s的博客",keywords);
        return blogServiceImp.getByKeywords(keywords);
    }

    @GetMapping("api/blogs/nums")
    Integer getBlogsNums() {
        int nums = blogServiceImp.getNums();
        System.out.printf("返回博客总数: %d\n",nums);
        return nums;
    }

    @PostMapping("api/blogs")
    Result addBlog(@RequestBody Blog blog) {
        try {
            Blog blog1 = blogServiceImp.getById(blog.getId());
            if (blog1 != null) {
                blogServiceImp.update(blog);
                System.out.println("文章更新成功");
                return ResultFactory.buildSuccessRep("更新成功");
            }else {
                blogServiceImp.add(blog);
                System.out.println("添加文章成功");
                return ResultFactory.buildSuccessRep("发布成功");
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            return ResultFactory.buildFailRep("请求失败");
        }
    }

    @DeleteMapping("api/blogs/delete/{id}")
    Result deleteBlog(@PathVariable("id") int id) {
        try {
            System.out.println("请求删除文章");
            blogServiceImp.deleteById(id);
            return ResultFactory.buildSuccessRep("成功删除文章");
        }catch (RuntimeException e) {
            return ResultFactory.buildFailRep("删除失败");
        }
    }

    /*
    博客图片上传
     */
    @PostMapping("/api/admin/blog/images")
    public String coversUpload(MultipartFile file) throws Exception {
        String folder = "/home/myblog/images";
        File imageFolder = new File(folder);
        //对文件重命名，保留文件的格式png/jpg
        String newName = UUID.randomUUID().toString();
        File f = new File(imageFolder, newName + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            return "http://8.131.110.169/api/file/" + f.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
    博客图片删除
     */
    @PostMapping("api/admin/blog/images/delete")
    public Result deleteBlogImage(@RequestBody Map<String,String> body) {
        String url = body.get("url");
        String fileUrl = url.replace("http://8.131.110.169/api/file/","/home/myblog/images/");
        System.out.println(fileUrl);
        File file = new File(fileUrl);
        if (file.isFile() && file.exists()) {
            try {
                file.delete();
                return ResultFactory.buildSuccessRep("删除成功");
            }catch (Exception e) {
                return ResultFactory.buildFailRep("删除失败");
            }
        }else {
            return ResultFactory.buildFailRep("不是有效的文件路径");
        }
    }

    /*
    登录验证
     */
    @PostMapping("api/login")
    public Result login(@RequestBody User user) {
        User user1 = userService.getUserByName(user.getUsername());
        if (user1 == null || ! user1.getPassword().equals(user.getPassword())) {
            return ResultFactory.buildFailRep("账户或密码错误");
        }
        return ResultFactory.buildSuccessRep("登录成功");
    }
}
