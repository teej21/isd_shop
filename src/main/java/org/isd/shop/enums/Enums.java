package org.isd.shop.enums;

public class Enums {
    public enum Role {
        ADMIN("ADMIN"),
        MANAGER("MANAGER"),
        EMPLOYEE("EMPLOYEE"),
        CUSTOMER("CUSTOMER");

        Role(String role) {

        }
    }

    public enum ProductStatus {
        AVAILABLE("AVAILABLE"),
        STOCKOUT("STOCKOUT"),
        ORDERED("ORDERED");

        ProductStatus(String status) {

        }


    }

    public enum Gender {
        MALE("male"),
        FEMALE("female"),
        OTHER("other");

        Gender(String gender) {

        }

        public String getGender() {
            return this.toString();
        }
    }


    public enum OrderStatus {
        INIT("INIT"),
        PENDING("PENDING"),
        ASSIGNED("ASSIGNED"),
        SHIPPED("SHIPPING"),
        DELIVERED("DELIVERED"),
        CANCELLED("CANCELLED");

        OrderStatus(String status) {

        }
    }

}
