package br.com.vitor.cybercrimes.tracker.core.legalprocess.service;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SingletonTest {

    private static SingletonTest instance;

    private SingletonTest() {
    }

    public SingletonTest getInstance() {
        if (SingletonTest.instance == null) {
            SingletonTest.instance = new SingletonTest();
        }

        return SingletonTest.instance;
    }

    public void doSomething() {
        log.info("Doing something with this object ... {}", this.toString());
    }
}
