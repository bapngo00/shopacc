package com.example.web.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msCode;
    private String title;
    private double price;
    private String server;
    private String className;

    private int level;

    private String ttgt; // Trạng thái giao dịch (VD: Không, Có)

    @Column(name = "logo_image", columnDefinition = "LONGTEXT", nullable = true)
    private String logoImage; // Ảnh logo (chỉ 1 ảnh) - hiển thị ngoài danh sách

    @Column(columnDefinition = "LONGTEXT")
    private String imgData; // Lưu JSON array: ["img1", "img2", ...] - ảnh chi tiết (nhiều ảnh)

    private String facebookLink; // Link Facebook để liên hệ mua hàng

    @Column(columnDefinition = "TEXT")
    private String highlightInfo; // Thông tin nổi bật

    private boolean sold = false;

    /* ===== Getter & Setter ===== */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMsCode() { return msCode; }
    public void setMsCode(String msCode) { this.msCode = msCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getTtgt() { return ttgt; }
    public void setTtgt(String ttgt) { this.ttgt = ttgt; }

    public String getLogoImage() { return logoImage; }
    public void setLogoImage(String logoImage) { this.logoImage = logoImage; }

    public String getImgData() { return imgData; }
    public void setImgData(String imgData) { this.imgData = imgData; }

    public boolean isSold() { return sold; }
    public void setSold(boolean sold) { this.sold = sold; }

    public String getFacebookLink() { return facebookLink; }
    public void setFacebookLink(String facebookLink) { this.facebookLink = facebookLink; }

    public String getHighlightInfo() { return highlightInfo; }
    public void setHighlightInfo(String highlightInfo) { this.highlightInfo = highlightInfo; }
}
