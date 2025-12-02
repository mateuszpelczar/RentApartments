package com.example.RentApartments.dto;

public class AuthResponse {

  private String accessToken;
  private String refreshToken;
  private String type="Bearer";
  private Long id;
  private String email;
  private String username;

  public AuthResponse(){}

  public AuthResponse(String accessToken, String refreshToken, Long id, String email, String username){
    this.accessToken=accessToken;
    this.refreshToken=refreshToken;
    this.id=id;
    this.email=email;
    this.username=username;
  }

   public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
  
}
