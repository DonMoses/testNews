package com.example.moses.mosesnews.josnbeams.imgs;

/**
 * Created by Moses on 2015
 */
public class ImgData {
    private String id;
    private String title;
    private String source;
    private String pic;
    private String link;
    private String intro;
    private int pubDate;
    private String comments;
    private Imgs pics;
    private String category;
    private int comment;
    private ImgCommentInfo comment_count_info;
    private String long_title;


    public void setCategory(String category) {
        this.category = category;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getCategory() {
        return category;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setComment_count_info(ImgCommentInfo comment_count_info) {
        this.comment_count_info = comment_count_info;
    }

    public ImgCommentInfo getComment_count_info() {
        return comment_count_info;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Imgs getPics() {
        return pics;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPubDate() {
        return pubDate;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getComments() {
        return comments;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setLong_title(String long_title) {
        this.long_title = long_title;
    }

    public String getIntro() {
        return intro;
    }

    public void setPics(Imgs pics) {
        this.pics = pics;
    }

    public String getLink() {
        return link;
    }

    public void setPubDate(int pubDate) {
        this.pubDate = pubDate;
    }

    public String getLong_title() {
        return long_title;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
