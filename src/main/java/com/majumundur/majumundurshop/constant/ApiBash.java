package com.majumundur.majumundurshop.constant;

public class ApiBash {
    //Api Name
    public static final String PRODUCT = "/api/v1/product";
    public static final String CUSTOMER = "/api/v1/customer";
    public static final String TRANSACTION = "/api/v1/transactions";

    //Method Name
    public static final String GET_BY_ID = "/{id}";
    public static final String DELETED = "/{id}";
    public static final String UPDATE = "/{id}";

    //massage
    public static final String SUCCESS_DELETE_CUSTOMER = "Successfully delete customer";
    public static final String SUCCESS_CREATE_CUSTOMER = "Successfully create customer";
    public static final String SUCCESS_UPDATE_CUSTOMER = "Successfully update customer";

    public static final String AUTH = "/api/auth";
    public static final String REGISTER = "/register/admin";
}
