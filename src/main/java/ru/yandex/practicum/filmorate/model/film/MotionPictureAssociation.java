package ru.yandex.practicum.filmorate.model.film;

public enum MotionPictureAssociation {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    private String mpa;

    MotionPictureAssociation(String mpa) {
        this.mpa = mpa;
    }

    public String getMpa() {
        return mpa;
    }

    public void setMpa(String mpa) {
        this.mpa = mpa;
    }
}
