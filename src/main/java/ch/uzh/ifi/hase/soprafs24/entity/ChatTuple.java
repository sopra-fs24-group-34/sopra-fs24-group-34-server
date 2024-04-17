package ch.uzh.ifi.hase.soprafs24.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CHATTUPLE")
public class ChatTuple{
  private String message;
  private Long userid;

  @Id
  @GeneratedValue
  private Long id;

    //public ChatTuple(String message, Long userid) {
  //  this.message = message;
  //  this.userid = userid;
  //}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getUserid() {
    return userid;
  }

  public void setUserid(Long userid) {
    this.userid = userid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

}
