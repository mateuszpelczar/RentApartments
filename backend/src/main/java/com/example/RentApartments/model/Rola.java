package com.example.RentApartments.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="roles")
public class Rola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false,unique = true)
    private String rolename;

    @OneToMany(mappedBy="role", cascade = CascadeType.ALL)
    private List<UserRole> roles = new ArrayList<>();

    public Rola(){}

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public String getRolename(){return rolename;}
    public void setRolename(String rolename){this.rolename=rolename;}

    public List<UserRole> getRoles(){return roles;}
    public void setRoles(List<UserRole> roles){this.roles=roles;}


}
