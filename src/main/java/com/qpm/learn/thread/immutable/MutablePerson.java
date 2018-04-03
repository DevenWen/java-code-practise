package com.qpm.learn.thread.immutable;

/**
 * Created by qpm on 2017/10/21.
 */
public final class MutablePerson {

    private String name;
    private String address;

    public MutablePerson(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public MutablePerson(ImmutablePerson person) {
        this.name = person.getName();
        this.name = person.getAddress();
    }

    public void setPersion(String newName, String newAddress) {
        this.name = newName;
        this.address = newAddress;
    }

    String getName() {
        return name;
    }

    String getAddress() {
        return address;
    }

    @Override
    public synchronized String toString() {
        return "[ MutablePerson : " + name + "," + address + " ]";
    }

}
