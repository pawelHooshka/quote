package com.zopa.bank.business;

public interface BankResponse {
    /*
    This is basically an empty interface to serve as a common type for the bank responses.
    This interface is empty because the only method we need to implement is toString, which exists
    on the Object class already but it is a very poor design practice to use Object as a type.
     */
    @Override
    String toString();
}
