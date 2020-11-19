/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kisoft;

import lombok.Data;

/**
 * A Class representing the state of the plugin, so the start and stop goals can communicate
 * @author tareq
 */
@Data
public class State {

    private static State instance = instance();

    private State() {

    }
    
    public static final State instance() {
        if (instance == null) {
            instance = new State();
        }
        return instance;
    }

    private int port;
    private String name;
    private String containerId;
}
