package com.ivanthescientist.projectmanager.domain;

public class DomainException extends RuntimeException {
    public DomainException() {
        super();
    }
    public DomainException(String message) {
        super(message);
    }
}
