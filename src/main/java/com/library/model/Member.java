package com.library.model;

import java.time.LocalDate;

public class Member 
{
  public enum Status
  {
	 ACTIVE, INACTIVE  
  }
  
  private int memberId;
  private String name;
  private String email;
  private String phone;
  private String address;
  private LocalDate membershipDate;
  private Status status;
  
  public Member()
  {
	  
  }
  
  public Member(String name, String email, String phone, String address)
  {
	  this.name=name;
	  this.email=email;
	  this.phone=phone;
	  this.address=address;
	  this.status=Status.ACTIVE;
  }
  
  public Member(int memberId, String name, String email, String phone, String address,LocalDate membershipDate, Status status)
  {
	  this.memberId=memberId;
	  this.name=name;
	  this.email=email;
	  this.phone=phone;
	  this.address=address;
	  this.membershipDate=membershipDate;
	  this.status=status;
  }
  
  public void setMemberId(int memberId) {this.memberId=memberId;}
  public int getMemberId() {return memberId;}
  
  public void setName(String name) {this.name=name;}
  public String getName() {return name;}
  
  public void setEmail(String email) {this.email=email;}
  public String getEmail() {return email;}
  
  public void setPhone(String phone) {this.phone=phone;}
  public String getPhone() {return phone;}
  
  public void setAddress(String address) {this.address=address;}
  public String getAddress() {return address;}
  
  public void setMembershipDate(LocalDate membershipDate) {this.membershipDate=membershipDate;}
  public LocalDate getMembershipDate() {return membershipDate;}
  
  public void setStatus(Status status) {this.status=status;}
  public Status getStatus() {return status;}
  
  @Override
  public String toString() {
      return String.format("[%d] %-20s | %-25s | %-12s | Since: %s | %s",
              memberId, name, email, phone, membershipDate, status);
  }
  
  
  }


