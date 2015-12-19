package com.community.tsinghua.model;

import java.sql.Date;

/**
 * Created by CJKIM on 2015. 11. 27..
 */
public class ArticleClass {
    private String title;
    private String author;
    private String contents;
    private Date releasetime;

    public ArticleClass(String title,String author,String contents,Date releasetime){
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.releasetime = releasetime;
    }

    public String getTitle(){return title;}
    public String getAuthor(){return author;}
    public String getContents(){return contents;}
    public Date getReleasetime(){return releasetime;}

}
