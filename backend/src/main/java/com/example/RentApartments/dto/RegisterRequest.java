package com.example.RentApartments.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterRequest {


  @NotBlank(message = "Pole imie nie moze byc puste")
  private String username; 

  @NotBlank(message = "Pole nazwisko nie moze byc puste")
  private String surname;

  @Email(message="Email musi byc poprawny")
  @NotBlank(message="Pole email nie moze byc puste")
  private String email;

  @NotBlank(message="Pole haslo nie moze byc puste")
  private String password;

  @Pattern(regexp = "^[0-9]{9}$", message="Numer telefonu musi zawierac dokladnie 9 cyfr")
  @NotBlank(message="Pole telefon nie moze byc puste")
  private String phoneNumber;

  public RegisterRequest(){}

  public RegisterRequest(String username, String surname, String email, String password, String phoneNumber){
    this.username=username;
    this.surname=surname;
    this.email=email;
    this.password=password;
    this.phoneNumber=phoneNumber;
  }

  public String getUserName(){
    return username;
  }

  public void setUserName(String username){
    this.username=username;
  }

  public String getSurname(){
    return surname;
  }

  public void setSurname(String surname){
    this.surname=surname;
  }

   public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber(){
      return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
      this.phoneNumber=phoneNumber;
    }
  
}
