package org.dice.qa.models;

import org.dice.qa.constants.AppConstants;

public class Triple {
    private String subject;
    private String predicate;
    private String object;

    public Triple(String subject, String predicate, String object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public boolean isSubjectVariable() {
        return this.subject.contains(AppConstants.VARIABLE_PREFIX);
    }

    public boolean isPredicateVariable() {
        return this.predicate.contains(AppConstants.VARIABLE_PREFIX);
    }

    public boolean isObjectVariable() {
        return this.object.contains(AppConstants.VARIABLE_PREFIX);
    }

    @Override
    public String toString() {
        return String.join(" ", subject, predicate, object);
    }
}
