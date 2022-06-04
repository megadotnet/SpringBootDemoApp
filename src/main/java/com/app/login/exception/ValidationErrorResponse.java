package com.app.login.exception;


import java.util.ArrayList;
import java.util.List;

/**
 * ValidationErrorResponse
 * @author peterliu
 */
public class ValidationErrorResponse {

    private List<Violation> violations = new ArrayList<>();

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }
}


