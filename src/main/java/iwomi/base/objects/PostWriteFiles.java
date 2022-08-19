/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author tebit roger
 */
@Entity
@Table(name = "inputcmt")
public class PostWriteFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String fichi;
    String quest;
    String post;

    public void setId(Long id) {
        this.id = id;
    }

    public void setFichi(String fichi) {
        this.fichi = fichi;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public String getFichi() {
        return fichi;
    }

    public String getQuest() {
        return quest;
    }

    public String getPost() {
        return post;
    }

    public PostWriteFiles() {
    }

}
