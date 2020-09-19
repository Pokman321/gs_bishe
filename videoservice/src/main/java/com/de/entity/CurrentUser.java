package com.de.entity;

import java.util.StringJoiner;

/**
 * @author gs
 * @date 2020/7/20 - 4:18
 */
public class CurrentUser {

    private static int personId;

    private static String personName;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        CurrentUser.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        CurrentUser.personName = personName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurrentUser.class.getSimpleName() + "[", "]")
                .add("personId=" + personId)
                .add("personName='" + personName + "'")
                .toString();
    }
}
