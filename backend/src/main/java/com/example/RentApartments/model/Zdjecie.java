package com.example.RentApartments.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "zdjecia")
public class Zdjecie {

  @Id
  private String id;

  @Field("mieszkanie_id")
  private Long mieszkanie_id;

  @Field("url")
  private String url;

  @Field("opis")
  private String opis;

  public Zdjecie(){}

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public Long getMieszkanie_id() { return mieszkanie_id; }
  public void setMieszkanie_id(Long mieszkanie_id) { this.mieszkanie_id = mieszkanie_id; }

  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }

  public String getOpis() { return opis; }
  public void setOpis(String opis) { this.opis = opis; }
}
