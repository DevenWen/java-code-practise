package com.qpm.learn.thread.immutable;

/**
 * Created by qpm on 2017/10/21.
 */
public class ImmutablePerson {
    private final String name;
    private final String address;

    public ImmutablePerson(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public ImmutablePerson(MutablePerson person) {
        synchronized (person) {
            this.name = person.getName();
            this.address = person.getAddress();
        }
    }

    public MutablePerson getMutablePersion() {
        return new MutablePerson(this);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "[ ImmutablePersion: " + name + ", " + address + " ]";
    }

}
