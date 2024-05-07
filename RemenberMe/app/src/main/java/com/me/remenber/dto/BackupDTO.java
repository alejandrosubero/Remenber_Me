package com.me.remenber.dto;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.me.remenber.security.Cryptography;

import java.io.Serializable;

public class BackupDTO implements Serializable {

    private String unbral;//key
    private String cheese;
    private String meet; //"name"
    private String paprica; //typeData
    private String panela;//use
    private String soup;// note
    private String recipes;//imageString
    private String recipesLasagna; // byte[] imageFail

    private boolean isFood;


    public BackupDTO() {
        this.isFood = false;
    }


    public BackupDTO(String unbral, String cheese, String meet, String paprica, String panela, String soup, String recipes, String recipesLasagna) {

        if (unbral != null) {
            this.unbral = unbral;
        }
        if (cheese != null) {
            this.cheese = cheese;
        }
        if (meet != null) {
            this.meet = meet;
        }
        if (paprica != null) {
            this.paprica = paprica;
        }
        if (panela != null) {
            this.panela = panela;
        }
        if (soup != null) {
            this.soup = soup;
        }
        if (recipes != null) {
            this.recipes = recipes;
        }
        if (recipesLasagna != null) {
            this.recipesLasagna = recipesLasagna;
        }
        this.isFood = false;
    }


    public String getUnbral() {
        return unbral;
    }

    public void setUnbral(String unbral) {
        this.unbral = unbral;
    }

    public String getCheese() {
        return cheese;
    }

    public void setCheese(String cheese) {
        this.cheese = cheese;
    }

    public String getMeet() {
        return meet;
    }

    public void setMeet(String meet) {
        this.meet = meet;
    }

    public String getPaprica() {
        return paprica;
    }

    public void setPaprica(String paprica) {
        this.paprica = paprica;
    }

    public String getPanela() {
        return panela;
    }

    public void setPanela(String panela) {
        this.panela = panela;
    }

    public String getSoup() {
        return soup;
    }

    public void setSoup(String soup) {
        this.soup = soup;
    }

    public String getRecipes() {
        return recipes;
    }

    public void setRecipes(String recipes) {
        this.recipes = recipes;
    }

    public String getRecipesLasagna() {
        return recipesLasagna;
    }

    public void setRecipesLasagna(String recipesLasagna) {
        this.recipesLasagna = recipesLasagna;
    }

    public boolean isFood() {
        return isFood;
    }

    public void setFood(boolean food) {
        isFood = food;
    }

    public void backupDTOEncrypt() {

        Cryptography cryptography = null;

        if (this.cheese != null && !this.isFood) {
            cryptography = new Cryptography(this.cheese);

            if (this.unbral != null) {
                String encryp = cryptography.encryptAES(this.unbral);
                this.unbral = encryp;
            }

            if (this.meet != null) {
                String encryp = cryptography.encryptAES(this.meet);
                this.meet = encryp;
            }
            if (this.paprica != null) {
                String encryp = cryptography.encryptAES(this.paprica);
                this.paprica = encryp;
            }
            if (this.panela != null) {
                String encryp = cryptography.encryptAES(this.panela);
                this.panela = encryp;
            }
            if (this.soup != null) {
                String encryp = cryptography.encryptAES(this.soup);
                this.soup = encryp;
            }
            if (this.recipes != null) {
                String encryp = cryptography.encryptAES(this.recipes);
                this.recipes = encryp;
            }
            if (this.recipesLasagna != null) {
                String encryp = cryptography.encryptAES(this.recipesLasagna);
                this.recipesLasagna = encryp;
            }
            this.isFood = true;
        }
    }

    public void backupDTODencrypt() {

        Cryptography cryptography = null;

        if (this.cheese != null && this.isFood) {
            cryptography = new Cryptography(this.cheese);
            if (this.unbral != null) {
                String encryp = cryptography.decryptAES(this.unbral);
                this.unbral = encryp.trim();
            }
            if (this.meet != null) {
                String encryp = cryptography.decryptAES(this.meet);
                this.meet = encryp.trim();
            }
            if (this.paprica != null) {
                String encryp = cryptography.decryptAES(this.paprica);
                this.paprica = encryp.trim();
            }
            if (this.panela != null) {
                String encryp = cryptography.decryptAES(this.panela);
                this.panela = encryp;
            }
            if (this.soup != null) {
                String encryp = cryptography.decryptAES(this.soup);
                this.soup = encryp;
            }
            if (this.recipes != null) {
                String encryp = cryptography.decryptAES(this.recipes);
                this.recipes = encryp;
            }
            if (this.recipesLasagna != null) {
                String encryp = cryptography.decryptAES(this.recipesLasagna);
                this.recipesLasagna = encryp;
            }
            this.isFood = false;
        }
    }

}
