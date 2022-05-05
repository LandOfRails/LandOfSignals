
package net.landofrails.signalbox.github;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class GitHubRelease {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("assets_url")
    @Expose
    private String assetsUrl;
    @SerializedName("upload_url")
    @Expose
    private String uploadUrl;
    @SerializedName("html_url")
    @Expose
    private String htmlUrl;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("node_id")
    @Expose
    private String nodeId;
    @SerializedName("tag_name")
    @Expose
    private String tagName;
    @SerializedName("target_commitish")
    @Expose
    private String targetCommitish;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("draft")
    @Expose
    private Boolean draft;
    @SerializedName("prerelease")
    @Expose
    private Boolean prerelease;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("assets")
    @Expose
    private List<Asset> assets = null;
    @SerializedName("tarball_url")
    @Expose
    private String tarballUrl;
    @SerializedName("zipball_url")
    @Expose
    private String zipballUrl;
    @SerializedName("body")
    @Expose
    private String body;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GitHubRelease() {
    }

    /**
     * 
     * @param tarballUrl
     * @param publishedAt
     * @param author
     * @param htmlUrl
     * @param tagName
     * @param targetCommitish
     * @param body
     * @param zipballUrl
     * @param url
     * @param createdAt
     * @param assets
     * @param uploadUrl
     * @param prerelease
     * @param draft
     * @param name
     * @param id
     * @param nodeId
     * @param assetsUrl
     */
    public GitHubRelease(String url, String assetsUrl, String uploadUrl, String htmlUrl, Integer id, Author author, String nodeId, String tagName, String targetCommitish, String name, Boolean draft, Boolean prerelease, String createdAt, String publishedAt, List<Asset> assets, String tarballUrl, String zipballUrl, String body) {
        super();
        this.url = url;
        this.assetsUrl = assetsUrl;
        this.uploadUrl = uploadUrl;
        this.htmlUrl = htmlUrl;
        this.id = id;
        this.author = author;
        this.nodeId = nodeId;
        this.tagName = tagName;
        this.targetCommitish = targetCommitish;
        this.name = name;
        this.draft = draft;
        this.prerelease = prerelease;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.assets = assets;
        this.tarballUrl = tarballUrl;
        this.zipballUrl = zipballUrl;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAssetsUrl() {
        return assetsUrl;
    }

    public void setAssetsUrl(String assetsUrl) {
        this.assetsUrl = assetsUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTargetCommitish() {
        return targetCommitish;
    }

    public void setTargetCommitish(String targetCommitish) {
        this.targetCommitish = targetCommitish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Boolean getPrerelease() {
        return prerelease;
    }

    public void setPrerelease(Boolean prerelease) {
        this.prerelease = prerelease;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public String getTarballUrl() {
        return tarballUrl;
    }

    public void setTarballUrl(String tarballUrl) {
        this.tarballUrl = tarballUrl;
    }

    public String getZipballUrl() {
        return zipballUrl;
    }

    public void setZipballUrl(String zipballUrl) {
        this.zipballUrl = zipballUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
