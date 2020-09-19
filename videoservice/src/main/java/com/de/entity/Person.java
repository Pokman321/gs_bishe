package com.de.entity;

/**
 * @author gs
 * @date 2020/6/22 - 13:20
 */
public class Person {

    private int id;

    private int personId;

    private int cameraId;

    private String imageUrl;

    private long time;

    private String position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id=").append(id);
        sb.append("personId=").append(personId);
        sb.append(", cameraId=").append(cameraId);
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
